package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

public class CategoryApiHelper {
    private static final String BASE_URI = "http://localhost:8080/api";

    public static Response getAllCategories(String token) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NzAyMTI0NTgsImV4cCI6MTc3MDIxNjA1OH0.Gnlc3hZYGx55-2Fy-nYS7wgZZGCTe8MFqgCvgKjqgWA")
                .get(BASE_URI + "/categories");
    }

    public static Response getCategoriesWithParams(String token, Map<String, Object> params) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NzAyMTI0NTgsImV4cCI6MTc3MDIxNjA1OH0.Gnlc3hZYGx55-2Fy-nYS7wgZZGCTe8MFqgCvgKjqgWA")
                .queryParams(params)
                .get(BASE_URI + "/categories");
    }

    public static Response createCategory(String token, String body) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NzAyMTI0NTgsImV4cCI6MTc3MDIxNjA1OH0.Gnlc3hZYGx55-2Fy-nYS7wgZZGCTe8MFqgCvgKjqgWA")
                .contentType("application/json")
                .body(body)
                .post(BASE_URI + "/categories");
    }

    public static Response updateCategory(String token, int id, String body) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NzAyMTI0NTgsImV4cCI6MTc3MDIxNjA1OH0.Gnlc3hZYGx55-2Fy-nYS7wgZZGCTe8MFqgCvgKjqgWA")
                .contentType("application/json")
                .body(body)
                .put(BASE_URI + "/categories/" + id);
    }

    public static Response getCategoriesNoAuth() {
        return RestAssured.given()
                .get(BASE_URI + "/categories");
    }
}
