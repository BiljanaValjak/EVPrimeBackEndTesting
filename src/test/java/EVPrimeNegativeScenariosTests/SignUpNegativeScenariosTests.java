package EVPrimeNegativeScenariosTests;

import client.EVPrimeClient;
import data.SignUpLoginDataFactory;
import io.restassured.response.Response;
import model.request.SignUpLoginRequest;
import model.responseNegativeScenarios.SignInResponseModelInvalidEmail;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import util.DateBuilder;

import static objectbuilder.SignUpLoginObjectBuilderClass.createBodySignUpLogin;
import static org.junit.Assert.assertEquals;

public class SignUpNegativeScenariosTests {

    static DateBuilder dateBuilder = new DateBuilder();

    private static SignUpLoginRequest signUpRequest;

    @BeforeClass
    public static void setUpTest() {
        signUpRequest = new SignUpLoginDataFactory(createBodySignUpLogin())
                .setEmail(RandomStringUtils.randomAlphanumeric(10) + dateBuilder.currentTime() + "@mail.com")
                .setPassword(RandomStringUtils.randomAlphanumeric(10))
                .createRequest();

        new EVPrimeClient()
                .signUpNewUser(signUpRequest);
    }

    @Test
    public void signUpNewUserEmailAlreadyExistsTest(){
        Response secondResponseSignUpPost = new EVPrimeClient()
                .signUpNewUser(signUpRequest);

        SignInResponseModelInvalidEmail secondSignUpResponse = secondResponseSignUpPost.body().as(SignInResponseModelInvalidEmail.class);

        assertEquals(422, secondResponseSignUpPost.statusCode());
        assertEquals("User signup failed due to validation errors.", secondSignUpResponse.getMessage() );
        assertEquals("Email exists already.", secondSignUpResponse.getErrors().getEmail());

    }

    @Test
    public void signUpNewUserInvalidEmailTest(){
        signUpRequest.setEmail(RandomStringUtils.randomAlphanumeric(10) + dateBuilder.currentTime() + "mail.com");

        Response responseSignUpPost = new EVPrimeClient()
                .signUpNewUser(signUpRequest);

        SignInResponseModelInvalidEmail signUpResponse = responseSignUpPost.body().as(SignInResponseModelInvalidEmail.class);

        assertEquals(422, responseSignUpPost.statusCode());
        assertEquals("User signup failed due to validation errors.", signUpResponse.getMessage());
        assertEquals("Invalid email.", signUpResponse.getErrors().getEmail());

    }
}
