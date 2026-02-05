package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

public class CategoryApiHelper {
    private static final String BASE_URI = utils.ConfigLoader.getProperty("api.base.url");

    public static Response getAllCategories(String token) {
        Response resp = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .get(BASE_URI + "/categories");
        resp.then().log().ifError();
        return resp;
    }

    public static Response getCategoriesWithParams(String token, Map<String, Object> params) {
        Response resp = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .queryParams(params)
                .get(BASE_URI + "/categories");
        resp.then().log().ifError();
        return resp;
    }

    public static Response createCategory(String token, String body) {
        Response resp = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(body)
                .post(BASE_URI + "/categories");
        resp.then().log().ifError();
        return resp;
    }

    public static Response updateCategory(String token, int id, String body) {
        Response resp = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(body)
                .put(BASE_URI + "/categories/" + id);
        resp.then().log().ifError();
        return resp;
    }

    public static Response deleteCategory(String token, int id) {
        Response resp = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .delete(BASE_URI + "/categories/" + id);
        resp.then().log().ifError();
        return resp;
    }

    public static Response getCategoriesNoAuth() {
        Response resp = RestAssured.given()
                .get(BASE_URI + "/categories");
        resp.then().log().ifError();
        return resp;
    }
}
