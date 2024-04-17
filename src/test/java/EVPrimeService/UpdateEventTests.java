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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import util.DateBuilder;

import java.sql.SQLException;

import static objectbuilder.EventsObjectBuilderClass.createBody;
import static objectbuilder.SignUpLoginObjectBuilderClass.createBodySignUpLogin;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UpdateEventTests {

    DbClient dbClient = new DbClient();
    private static String id;
    private static SignUpLoginRequest signUpRequest;
    private static LoginResponse loginResponse;
    private static PostPutEventsRequests requestBody;
    private static PostPutDeleteEventsResponse postResponse;
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

        Response responsePost = new EVPrimeClient()
                .postNewEvent(requestBody, loginResponse.getToken());

        postResponse = responsePost.body().as(PostPutDeleteEventsResponse.class);

        id = postResponse.getMessage().substring(39);

    }

    @Test
    public void updateEventTest() throws SQLException {
        requestBody.setLocation(RandomStringUtils.randomAlphanumeric(15));

        Response responsePut = new EVPrimeClient()
                .updateEvent(requestBody, id, loginResponse.getToken());

        PostPutDeleteEventsResponse putResponse = responsePut.body().as(PostPutDeleteEventsResponse.class);

        assertEquals(201, responsePut.statusCode());
        assertEquals("Successfully updated the event with id: " + id, putResponse.getMessage());
        assertEquals(requestBody.getDate(), dbClient.getEventFromDB(postResponse.getMessage().substring(39)).getDate());

    }

    @AfterClass
    public static void deleteEvent() throws SQLException {
        assertTrue(new DbClient()
                .isEventDeletedFromDb(id));

    }

}
