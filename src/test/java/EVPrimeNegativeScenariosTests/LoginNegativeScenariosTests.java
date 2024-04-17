package EVPrimeNegativeScenariosTests;

import client.EVPrimeClient;
import data.SignUpLoginDataFactory;
import io.restassured.response.Response;
import model.request.SignUpLoginRequest;
import model.responseNegativeScenarios.LoginResponseModelInvalidEmail;
import model.responseNegativeScenarios.LoginResponseModelInvalidPassword;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import util.DateBuilder;

import static objectbuilder.SignUpLoginObjectBuilderClass.createBodySignUpLogin;
import static org.junit.Assert.assertEquals;

public class LoginNegativeScenariosTests {

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
    public void loginUserInvalidEmailTest(){
        signUpRequest.setEmail(RandomStringUtils.randomAlphanumeric(10) + dateBuilder.currentTime() + "mail.com");

        Response responseLogin = new EVPrimeClient()
                .loginUser(signUpRequest);


        LoginResponseModelInvalidEmail responseBody = responseLogin.body().as(LoginResponseModelInvalidEmail.class);

        assertEquals(401, responseLogin.statusCode());
        assertEquals("Authentication failed.", responseBody.getMessage());

    }

    @Test
    public void loginUserInvalidPasswordTest(){
        signUpRequest.setPassword(RandomStringUtils.randomAlphanumeric(10) + "@");

        Response responseLogin = new EVPrimeClient()
                .loginUser(signUpRequest);

        LoginResponseModelInvalidPassword responseBody = responseLogin.body().as(LoginResponseModelInvalidPassword.class);

        assertEquals(422, responseLogin.statusCode());
        assertEquals("Invalid credentials.", responseBody.getMessage());
        assertEquals("Invalid email or password entered.", responseBody.getErrors().getCredentials());

    }

}
