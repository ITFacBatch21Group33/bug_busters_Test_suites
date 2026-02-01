package api.plant;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.Map;

public class PlantApiHelper {
    private static final String BASE_URI = "http://localhost:8080/api";

    public static Response getAllPlants(String token) {
        if (token != null && !token.isEmpty()) {
            return RestAssured.given()
                    .header("Authorization", "Bearer " + token)
                    .get(BASE_URI + "/plants");
        } else {
             return RestAssured.given()
                    .get(BASE_URI + "/plants");
        }
    }

    public static Response getPlantById(String token, int id) {
        if (token != null && !token.isEmpty()) {
            return RestAssured.given()
                    .header("Authorization", "Bearer " + token)
                    .get(BASE_URI + "/plants/" + id);
        } else {
             return RestAssured.given()
                    .get(BASE_URI + "/plants/" + id);
        }
    }

    public static Response updatePlant(String token, int id, String body) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(body)
                .put(BASE_URI + "/plants/" + id);
    }
}
