package EVPrimeService;

import client.EVPrimeClient;
import data.SignUpLoginDataFactory;
import io.restassured.response.Response;
import model.request.SignUpLoginRequest;
import model.responsePositiveScenarios.SignUpResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import util.DateBuilder;

import static objectbuilder.SignUpLoginObjectBuilderClass.createBodySignUpLogin;
import static org.junit.Assert.*;

public class SignUpTests {

    DateBuilder dateBuilder = new DateBuilder();

    @Test
    public void signUpNewUserTest(){
        SignUpLoginRequest requestBody = new SignUpLoginDataFactory(createBodySignUpLogin())
                .setEmail(RandomStringUtils.randomAlphanumeric(10) + dateBuilder.currentTime() + "@mail.com")
                .setPassword(RandomStringUtils.randomAlphanumeric(10))
                .createRequest();

        Response responseSignUpPost = new EVPrimeClient()
                .signUpNewUser(requestBody);

        SignUpResponse signUpResponse = responseSignUpPost.body().as(SignUpResponse.class);

        assertEquals(201, responseSignUpPost.statusCode());
        assertEquals("User created.", signUpResponse.getMessage());
        assertNotNull(signUpResponse.getUser().getId());
        assertEquals(requestBody.getEmail(), signUpResponse.getUser().getEmail());
        assertNotNull( signUpResponse.getToken());

    }
}