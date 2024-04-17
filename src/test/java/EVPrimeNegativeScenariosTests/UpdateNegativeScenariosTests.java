package EVPrimeNegativeScenariosTests;

import client.EVPrimeClient;
import data.EventsDataFactory;
import data.SignUpLoginDataFactory;
import database.DbClient;
import io.restassured.response.Response;
import jdk.jfr.Description;
import model.request.PostPutEventsRequests;
import model.request.SignUpLoginRequest;
import model.responseNegativeScenarios.*;
import model.responsePositiveScenarios.LoginResponse;
import model.responsePositiveScenarios.PostPutDeleteEventsResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.*;
import org.junit.jupiter.api.Disabled;
import util.DateBuilder;

import java.sql.SQLException;

import static objectbuilder.EventsObjectBuilderClass.createBody;
import static objectbuilder.SignUpLoginObjectBuilderClass.createBodySignUpLogin;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UpdateNegativeScenariosTests {

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
    public void updateEventEmptyTitleTest(){
        requestBody.setTitle("");

        Response responsePut = new EVPrimeClient()
                .updateEvent(requestBody, id, loginResponse.getToken());

        PostPutResponseEmptyTitle putResponse = responsePut.body().as(PostPutResponseEmptyTitle.class);

        assertEquals(422, responsePut.statusCode());
        assertEquals("Updating the event failed due to validation errors.", putResponse.getMessage());
        assertEquals("Invalid title.", putResponse.getErrors().getTitle());

    }

    @Test
    public void updateEventEmptyImageTest(){
        requestBody.setImage("");

        Response responsePut = new EVPrimeClient()
                .updateEvent(requestBody, id, loginResponse.getToken());

        PostPutResponseEmptyImage putResponse = responsePut.body().as(PostPutResponseEmptyImage.class);

        assertEquals(422, responsePut.statusCode());
        assertEquals("Updating the event failed due to validation errors.", putResponse.getMessage());
        assertEquals("Invalid image.", putResponse.getErrors().getImage());

    }

    @Test
    public void updateEventInvalidImageFormatTest(){
        requestBody.setImage(":https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQB2WS8x2o6tcVYvm1p2hwJtS5iSp5fGGeIjA&s");

        Response responsePut = new EVPrimeClient()
                .updateEvent(requestBody, id, loginResponse.getToken());

        PostPutResponseEmptyImage putResponse = responsePut.body().as(PostPutResponseEmptyImage.class);

        assertEquals(422, responsePut.statusCode());
        assertEquals("Updating the event failed due to validation errors.", putResponse.getMessage());
        assertEquals("Invalid image.", putResponse.getErrors().getImage());

    }

    @Test
    public void updateEventEmptyDateTest(){
        requestBody.setDate("");

        Response responsePut = new EVPrimeClient()
                .updateEvent(requestBody, id, loginResponse.getToken());

        PostPutResponseEmptyDate putResponse = responsePut.body().as(PostPutResponseEmptyDate.class);

        assertEquals(422, responsePut.statusCode());
        assertEquals("Updating the event failed due to validation errors.", putResponse.getMessage());
        assertEquals("Invalid date.", putResponse.getErrors().getDate());

    }

    @Test
    @Disabled
    @Description("BUG - 002")
    public void updateEventEmptyLocationTest(){
        requestBody.setLocation("");

        Response responsePut = new EVPrimeClient()
                .updateEvent(requestBody, id, loginResponse.getToken());

        PostPutResponseEmptyLocation putResponse = responsePut.body().as(PostPutResponseEmptyLocation.class);

        assertEquals(422, responsePut.statusCode());
        assertEquals("Updating the event failed due to validation errors.", putResponse.getMessage());
        assertEquals("Invalid location.", putResponse.getErrors().getLocation());

    }

    @Test
    public void updateEventEmptyDescriptionTest(){
        requestBody.setDescription("");

        Response responsePut = new EVPrimeClient()
                .updateEvent(requestBody, id, loginResponse.getToken());

        PostPutResponseEmptyDescription putResponse = responsePut.body().as(PostPutResponseEmptyDescription.class);

        assertEquals(422, responsePut.statusCode());
        assertEquals("Updating the event failed due to validation errors.", putResponse.getMessage());
        assertEquals("Invalid description.", putResponse.getErrors().getDescription());

    }

    @Test
    public void updateEventNoTokenTest(){
        requestBody.setLocation(RandomStringUtils.randomAlphanumeric(15));

        Response responseBody = new EVPrimeClient()
                .updateEventNoToken(requestBody, id);

        PostPutDeleteResponseMissingToken responseDelete = responseBody.body().as(PostPutDeleteResponseMissingToken.class);

        assertEquals(401, responseBody.statusCode());
        assertEquals("Not authenticated.", responseDelete.getMessage());

    }

    @AfterClass
    public static void deleteEvent() throws SQLException {
        assertTrue(new DbClient()
                .isEventDeletedFromDb(id));

    }
}
