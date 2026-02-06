package api.plant;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.Map;

public class PlantApiHelper {
    private static final String BASE_URI = utils.ConfigLoader.getProperty("api.base.url");
    public static Response getAllPlants(String token) {
        if (token != null && !token.isEmpty()) {
            return RestAssured.given()
                    .log().all()
                    .header("Authorization", "Bearer " + token)
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
                    .header("Authorization", "Bearer " + token)
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
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(body)
                .put(BASE_URI + "/plants/" + id);
        
        resp.then().log().ifError();
        return resp;
    }

    public static Response createPlant(String token, int categoryId, String body) {
        return RestAssured.given()
                .log().all()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(body)
                .post(BASE_URI + "/plants/category/" + categoryId);
    }

    public static Response getPagedPlants(String token, Map<String, String> queryParams) {
        if (token != null && !token.isEmpty()) {
            return RestAssured.given()
                    .log().all()
                    .header("Authorization", "Bearer " + token)
                    .queryParams(queryParams)
                    .get(BASE_URI + "/plants/paged");
        } else {
             return RestAssured.given()
                    .log().all()
                    .queryParams(queryParams)
                    .get(BASE_URI + "/plants/paged");
        }
    }

    public static Response deletePlant(String token, int id) {
        Response resp = RestAssured.given()
                .log().all()
                .header("Authorization", "Bearer " + token)
                .delete(BASE_URI + "/plants/" + id);
        
        resp.then().log().ifError();
        return resp;
    }
    public static void deleteAllPlants(String token) {
        Response response = getAllPlants(token);
        if (response.statusCode() == 200) {
            java.util.List<Integer> ids = response.jsonPath().getList("id");
            if (ids != null) {
                for (Integer id : ids) {
                    deletePlant(token, id);
                }
            }
        }
    }

    public static void createMultiplePlants(String token, int count) {
        for (int i = 0; i < count; i++) {
             String body = String.format("{\"name\": \"BulkPlant%d\", \"price\": 10.0, \"quantity\": 10}", i);
             createPlant(token, 1, body); // Assuming category 1 exists
        }
    }
}
