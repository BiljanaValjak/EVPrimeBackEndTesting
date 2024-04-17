package data;

import model.request.PostPutEventsRequests;

public class EventsDataFactory {

    private PostPutEventsRequests request;

    public EventsDataFactory(PostPutEventsRequests requestBody){
        request = requestBody;
    }

    public EventsDataFactory setTitle(String value){
        request.setTitle(value);
        return this;
    }

    public EventsDataFactory setImage(String value){
        request.setImage(value);
        return this;
    }

    public EventsDataFactory setDate(String value){
        request.setDate(value);
        return this;
    }

    public EventsDataFactory setLocation(String value){
        request.setLocation(value);
        return this;
    }

    public EventsDataFactory setDescription(String value){
        request.setDescription(value);
        return this;
    }

    public PostPutEventsRequests createRequest(){
        return request;
    }

}
