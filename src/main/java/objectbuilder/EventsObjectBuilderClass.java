package objectbuilder;

import model.request.PostPutEventsRequests;

public class EventsObjectBuilderClass {

    public static PostPutEventsRequests createBody(){
        return PostPutEventsRequests.builder()
                .title("Default title")
                .image("Default image")
                .date("Default date")
                .location("Default location")
                .description("Default description")
                .build();
    }

}
