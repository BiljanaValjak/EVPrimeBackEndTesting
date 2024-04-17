package model.responsePositiveScenarios;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Events {

    String id;
    String title;
    String image;
    String date;
    String location;
    String description;

}
