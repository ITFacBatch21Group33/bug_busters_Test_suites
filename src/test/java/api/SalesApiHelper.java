package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class SalesApiHelper {
    private String baseUrl;
    private String token;

    public SalesApiHelper(String baseUrl) {
        this.baseUrl = baseUrl;
        RestAssured.baseURI = baseUrl;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private RequestSpecification getRequestSpec() {
        RequestSpecification spec = given()
                .contentType("application/json")
                .accept("application/json");
        
        if (token != null && !token.isEmpty()) {
            spec.header("Authorization", "Bearer " + token);
        }
        
        return spec;
    }

    public Response getAllSales() {
        return getRequestSpec()
                .when()
                .get("/api/sales")
                .then()
                .extract()
                .response();
    }

    public Response getSalesWithPagination(int page, int size, String sortBy) {
        return getRequestSpec()
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sort", sortBy)
                .when()
                .get("/api/sales")
                .then()
                .extract()
                .response();
    }

    public Response getSaleById(int saleId) {
        return getRequestSpec()
                .when()
                .get("/api/sales/" + saleId)
                .then()
                .extract()
                .response();
    }

    public Response createSale(int plantId, int quantity) {
        return getRequestSpec()
                .queryParam("quantity", quantity)
                .when()
                .post("/api/sales/plant/" + plantId)
                .then()
                .extract()
                .response();
    }

    public Response createSaleWithInvalidQuantity(int plantId, int quantity) {
        return getRequestSpec()
                .queryParam("quantity", quantity)
                .when()
                .post("/api/sales/plant/" + plantId)
                .then()
                .extract()
                .response();
    }

    public Response deleteSale(int saleId) {
        return getRequestSpec()
                .when()
                .delete("/api/sales/" + saleId)
                .then()
                .extract()
                .response();
    }

    public Response searchSales(String searchTerm) {
        return getRequestSpec()
                .queryParam("search", searchTerm)
                .when()
                .get("/api/sales")
                .then()
                .extract()
                .response();
    }
}
