package EVPrimeService;

import client.EVPrimeClient;
import data.SignUpLoginDataFactory;
import io.restassured.response.Response;
import model.request.SignUpLoginRequest;
import model.responsePositiveScenarios.LoginResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import util.DateBuilder;

import static objectbuilder.SignUpLoginObjectBuilderClass.createBodySignUpLogin;
import static org.junit.Assert.*;

public class LoginTests {

    private static SignUpLoginRequest signUpRequest;
    static DateBuilder dateBuilder = new DateBuilder();

    @BeforeClass
    public static void userSetUp(){
        signUpRequest = new SignUpLoginDataFactory(createBodySignUpLogin())
                .setEmail(RandomStringUtils.randomAlphanumeric(10) + dateBuilder.currentTime() + "@mail.com")
                .setPassword(RandomStringUtils.randomAlphanumeric(10))
                .createRequest();

        new EVPrimeClient()
                .signUpNewUser(signUpRequest);

    }

    @Test
    public void loginUserTest(){
        Response responseLogin = new EVPrimeClient()
                .loginUser(signUpRequest);

        LoginResponse responseBody = responseLogin.body().as(LoginResponse.class);

        assertEquals(200, responseLogin.statusCode());
        assertNotNull(responseBody.getToken());
        assertTrue(responseBody.getExpirationTime().contains(dateBuilder.currentTime()));
        System.out.println(responseBody.getToken());

    }

}
