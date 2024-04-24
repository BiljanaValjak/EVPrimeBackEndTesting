package model.responsePositiveScenarios;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse {

    String message;
    SignUpResponseUser user;
    String token;

}
