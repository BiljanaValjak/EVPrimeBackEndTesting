package EVPrimeService;

import client.EVPrimeClient;
import io.restassured.response.Response;
import model.responsePositiveScenarios.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class GetEventsTest {

    @Test
    public void getAllEventsTest(){
        Response responseGetAll = new EVPrimeClient()
                .getAllEvents();

        GetResponse getAllResponse = responseGetAll.body().as(GetResponse.class);

        assertEquals(200, responseGetAll.statusCode());
        assertFalse(getAllResponse.getEvents().isEmpty());

    }
}
