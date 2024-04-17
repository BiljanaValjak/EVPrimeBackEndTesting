package client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.request.PostPutEventsRequests;
import model.request.SignUpLoginRequest;
import util.Configuration;

public class EVPrimeClient {
    public Response signUpNewUser(SignUpLoginRequest request){
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when().log().all()
                .body(request)
                .post(Configuration.SIGNUP_URL)
                .thenReturn();

    }

    public Response loginUser(SignUpLoginRequest request){
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when().log().all()
                .body(request)
                .post(Configuration.LOGIN_URL)
                .thenReturn();

    }

    public Response getAllEvents(){
        return RestAssured
                .given()
                .when().log().all()
                .get(Configuration.EVENTS_URL)
                .thenReturn();

    }

    public Response getByIdEvent(String id){
        return RestAssured
                .given()
                .when().log().all()
                .get(Configuration.EVENTS_URL + "/" + id)
                .thenReturn();

    }

    public Response postNewEvent(PostPutEventsRequests request, String token){
        return RestAssured
                .given()
                .header("Authorization", "bearer " + token)
                .contentType(ContentType.JSON)
                .when().log().all()
                .body(request)
                .post(Configuration.EVENTS_URL)
                .thenReturn();

    }

    public Response postNewEventNoToken(PostPutEventsRequests request){
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when().log().all()
                .body(request)
                .post(Configuration.EVENTS_URL)
                .thenReturn();

    }

    public Response updateEvent(PostPutEventsRequests request, String id, String token){
        return RestAssured
                .given()
                .header("Authorization", "bearer " + token)
                .contentType(ContentType.JSON)
                .when().log().all()
                .body(request)
                .put(Configuration.EVENTS_URL + "/" + id)
                .thenReturn();

    }

    public Response updateEventNoToken(PostPutEventsRequests request, String id){
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when().log().all()
                .body(request)
                .put(Configuration.EVENTS_URL + "/" + id)
                .thenReturn();

    }

    public Response deleteEvent(String id, String token){
        return RestAssured
                .given()
                .header("Authorization", "bearer " + token)
                .when().log().all()
                .delete(Configuration.EVENTS_URL + "/" + id)
                .thenReturn();

    }

    public Response deleteEventNoToken(String id){
        return RestAssured
                .given()
                .when().log().all()
                .delete(Configuration.EVENTS_URL + "/" + id)
                .thenReturn();

    }
}
