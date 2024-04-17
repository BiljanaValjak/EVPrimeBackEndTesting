package EVPrimeNegativeScenariosTests;

import client.EVPrimeClient;
import data.EventsDataFactory;
import data.SignUpLoginDataFactory;
import io.restassured.response.Response;
import model.request.PostPutEventsRequests;
import model.request.SignUpLoginRequest;
import model.responseNegativeScenarios.PostPutDeleteResponseMissingToken;
import model.responsePositiveScenarios.LoginResponse;
import model.responsePositiveScenarios.PostPutDeleteEventsResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import util.DateBuilder;

import static objectbuilder.EventsObjectBuilderClass.createBody;
import static objectbuilder.SignUpLoginObjectBuilderClass.createBodySignUpLogin;
import static org.junit.Assert.assertEquals;

public class DeleteNegativeScenariosTests {

    private static SignUpLoginRequest signUpRequest;
    private static LoginResponse loginResponse;
    private static PostPutEventsRequests requestBody;
    private static DateBuilder dateBuilder = new DateBuilder();
    private static String id;

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

        PostPutDeleteEventsResponse postResponse = responsePost.body().as(PostPutDeleteEventsResponse.class);
        id = postResponse.getMessage().substring(39);

    }

    @Test
    public void deleteEventNoTokenTest(){
        Response responseBody = new EVPrimeClient()
                .deleteEventNoToken(id);

        PostPutDeleteResponseMissingToken responseDelete = responseBody.body().as(PostPutDeleteResponseMissingToken.class);

        assertEquals(401, responseBody.statusCode());
        assertEquals("Not authenticated.", responseDelete.getMessage());

    }

}
