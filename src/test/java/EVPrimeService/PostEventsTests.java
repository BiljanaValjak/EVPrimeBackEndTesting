package EVPrimeService;

import client.EVPrimeClient;
import data.EventsDataFactory;
import data.SignUpLoginDataFactory;
import database.DbClient;
import io.restassured.response.Response;
import model.request.PostPutEventsRequests;
import model.request.SignUpLoginRequest;
import model.responsePositiveScenarios.LoginResponse;
import model.responsePositiveScenarios.PostPutDeleteEventsResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.*;
import util.DateBuilder;

import java.sql.SQLException;

import static objectbuilder.EventsObjectBuilderClass.createBody;
import static objectbuilder.SignUpLoginObjectBuilderClass.createBodySignUpLogin;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PostEventsTests {

    DbClient dbClient = new DbClient();
    private static String id;
    private static SignUpLoginRequest signUpRequest;
    private static LoginResponse loginResponse;
    private static PostPutEventsRequests requestBody;
    static DateBuilder dateBuilder = new DateBuilder();

    @BeforeClass
    public static void setUpTest(){
        signUpRequest = new SignUpLoginDataFactory(createBodySignUpLogin())
                .setEmail(RandomStringUtils.randomAlphanumeric(10) + dateBuilder.currentTime() + "@mail.com")
                .setPassword(RandomStringUtils.randomAlphanumeric(10))
                .createRequest();

        new EVPrimeClient()
                .signUpNewUser(signUpRequest);

        Response responseLogin = new EVPrimeClient()
                .loginUser(signUpRequest);

        loginResponse = responseLogin.body().as(LoginResponse.class);

        requestBody = new EventsDataFactory(createBody())
                .setTitle(RandomStringUtils.randomAlphanumeric(10))
                .setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQB2WS8x2o6tcVYvm1p2hwJtS5iSp5fGGeIjA&s")
                .setDate(dateBuilder.currentDate())
                .setLocation(RandomStringUtils.randomAlphanumeric(10))
                .setDescription(RandomStringUtils.randomAlphanumeric(100))
                .createRequest();

    }

    @Test
    public void postNewEventTest() throws SQLException {
        Response responsePost = new EVPrimeClient()
                .postNewEvent(requestBody, loginResponse.getToken());

        PostPutDeleteEventsResponse postResponse = responsePost.body().as(PostPutDeleteEventsResponse.class);
        id = postResponse.getMessage().substring(39);

        assertEquals(201, responsePost.statusCode());
        assertTrue(postResponse.getMessage().contains("Successfully created an event with id: " + id));
        assertEquals(requestBody.getTitle(), dbClient.getEventFromDB(id).getTitle());
        assertEquals(requestBody.getImage(), dbClient.getEventFromDB(id).getImage());
        assertEquals(requestBody.getDate(), dbClient.getEventFromDB(id).getDate());
        assertEquals(requestBody.getLocation(), dbClient.getEventFromDB(id).getLocation());
        assertEquals(requestBody.getDescription(), dbClient.getEventFromDB(id).getDescription());

    }

    @AfterClass
    public static void deleteEvent() throws SQLException {
        assertTrue(new DbClient()
                .isEventDeletedFromDb(id));

    }
}



