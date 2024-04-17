package EVPrimeNegativeScenariosTests;

import client.EVPrimeClient;
import data.EventsDataFactory;
import data.SignUpLoginDataFactory;
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

import static objectbuilder.EventsObjectBuilderClass.createBody;
import static objectbuilder.SignUpLoginObjectBuilderClass.createBodySignUpLogin;
import static org.junit.Assert.*;

public class PostNegativeScenariosTests {

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
    public void postNewEventEmptyTitleTest() {
        requestBody.setTitle("");

        Response responsePost = new EVPrimeClient()
                .postNewEvent(requestBody, loginResponse.getToken());

        PostPutResponseEmptyTitle postResponse = responsePost.body().as(PostPutResponseEmptyTitle.class);

        assertEquals(422, responsePost.statusCode());
        assertEquals("Adding the event failed due to validation errors.", postResponse.getMessage());
        assertEquals("Invalid title.", postResponse.getErrors().getTitle());

    }

    @Test
    public void postNewEventEmptyDateTest() {
        requestBody.setDate("");

        Response responsePost = new EVPrimeClient()
                .postNewEvent(requestBody, loginResponse.getToken());

        PostPutResponseEmptyDate postResponse = responsePost.body().as(PostPutResponseEmptyDate.class);

        assertEquals(422, responsePost.statusCode());
        assertEquals("Adding the event failed due to validation errors.", postResponse.getMessage());
        assertEquals("Invalid date.", postResponse.getErrors().getDate());

    }

    @Test
    public void postNewEventEmptyImageTest() {
        requestBody.setImage("");

        Response responsePost = new EVPrimeClient()
                .postNewEvent(requestBody, loginResponse.getToken());

        PostPutResponseEmptyImage postResponse = responsePost.body().as(PostPutResponseEmptyImage.class);

        assertEquals(422, responsePost.statusCode());
        assertEquals("Adding the event failed due to validation errors.", postResponse.getMessage());
        assertEquals("Invalid image.", postResponse.getErrors().getImage());

    }

    @Test
    public void postNewEvenInvalidImageFormatTest() {
        requestBody.setImage("://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQB2WS8x2o6tcVYvm1p2hwJtS5iSp5fGGeIjA&s");

        Response responsePost = new EVPrimeClient()
                .postNewEvent(requestBody, loginResponse.getToken());

        PostPutResponseEmptyImage postResponse = responsePost.body().as(PostPutResponseEmptyImage.class);

        assertEquals(422, responsePost.statusCode());
        assertEquals("Adding the event failed due to validation errors.", postResponse.getMessage());
        assertEquals("Invalid image.", postResponse.getErrors().getImage());
    }

    @Test
    @Disabled
    @Description("BUG - 001")
    public void postNewEvenEmptyLocationTest() {
        requestBody.setLocation("");

        Response responsePost = new EVPrimeClient()
                .postNewEvent(requestBody, loginResponse.getToken());

        PostPutResponseEmptyLocation postResponse = responsePost.body().as(PostPutResponseEmptyLocation.class);

        assertEquals(422, responsePost.statusCode());
        assertEquals("Adding the event failed due to validation errors.", postResponse.getMessage());
        assertEquals("Invalid location.", postResponse.getErrors().getLocation());

    }

    @Test
    public void postNewEvenEmptyDescriptionTest() {
        requestBody.setDescription("");

        Response responsePost = new EVPrimeClient()
                .postNewEvent(requestBody, loginResponse.getToken());

        PostPutResponseEmptyDescription postResponse = responsePost.body().as(PostPutResponseEmptyDescription.class);

        assertEquals(422, responsePost.statusCode());
        assertEquals("Adding the event failed due to validation errors.", postResponse.getMessage());
        assertEquals("Invalid description.", postResponse.getErrors().getDescription());

    }

    @Test
    public void postNewEventNoTokenTest() {
        Response responsePost = new EVPrimeClient()
                .postNewEventNoToken(requestBody);

        PostPutDeleteEventsResponse postResponse = responsePost.body().as(PostPutDeleteEventsResponse.class);

        assertEquals(401, responsePost.statusCode());
        assertEquals("Not authenticated.", postResponse.getMessage());

    }
}
