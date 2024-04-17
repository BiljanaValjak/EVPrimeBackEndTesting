package model.responseNegativeScenarios;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostPutResponseEmptyDescription {

    String message;
    PostPutResponseEmptyDescriptionErrors errors;

}
