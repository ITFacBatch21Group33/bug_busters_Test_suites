package api.plant;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.Map;

public class PlantApiHelper {
    private static final String BASE_URI = "http://localhost:8080/api";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzcwMjg1MDU3LCJleHAiOjE3NzAyODg2NTd9.YBfwPzCIqKGT1MxuSPbkoaCmqvu8cPL6d9v_uW_fcww";

    public static Response getAllPlants(String token) {
        if (token != null && !token.isEmpty()) {
            return RestAssured.given()
                    .log().all()
                    .header("Authorization", "Bearer " + TOKEN)
                    .get(BASE_URI + "/plants");
        } else {
             return RestAssured.given()
                    .log().all()
                    .get(BASE_URI + "/plants");
        }
    }

    public static Response getPlantById(String token, int id) {
        if (token != null && !token.isEmpty()) {
            return RestAssured.given()
                    .log().all()
                    .header("Authorization", "Bearer " + TOKEN)
                    .get(BASE_URI + "/plants/" + id);
        } else {
             return RestAssured.given()
                    .log().all()
                    .get(BASE_URI + "/plants/" + id);
        }
    }

    public static Response updatePlant(String token, int id, String body) {
        Response resp = RestAssured.given()
                .log().all()
                .header("Authorization", "Bearer " + TOKEN)
                .contentType("application/json")
                .body(body)
                .put(BASE_URI + "/plants/" + id);
        
        resp.then().log().ifError();
        return resp;
    }
    public static Response createPlant(String token, int categoryId, String body) {
        return RestAssured.given()
                .log().all()
                .header("Authorization", "Bearer " + TOKEN)
                .contentType("application/json")
                .body(body)
                .post(BASE_URI + "/plants/category/" + categoryId);
    }

    public static Response getPagedPlants(String token, Map<String, String> queryParams) {
        if (token != null && !token.isEmpty()) {
            return RestAssured.given()
                    .log().all()
                    .header("Authorization", "Bearer " + TOKEN)
                    .queryParams(queryParams)
                    .get(BASE_URI + "/plants/paged");
        } else {
             return RestAssured.given()
                    .log().all()
                    .queryParams(queryParams)
                    .get(BASE_URI + "/plants/paged");
        }
    }
}
