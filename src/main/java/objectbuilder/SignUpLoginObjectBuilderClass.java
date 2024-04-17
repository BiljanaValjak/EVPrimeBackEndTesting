package objectbuilder;

import model.request.SignUpLoginRequest;

public class SignUpLoginObjectBuilderClass {

    public static SignUpLoginRequest createBodySignUpLogin(){
        return SignUpLoginRequest.builder()
                .email("Default email")
                .password("Default password")
                .build();
    }
}
