package stepdefinitions;

import api.SalesApiHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.testng.Assert;
import utils.ConfigLoader;

import static io.restassured.RestAssured.given;

public class SalesAPISteps {
    private SalesApiHelper salesApiHelper;
    private Response response;
    private int createdSaleId;

    public SalesAPISteps() {
        String apiBaseUrl = ConfigLoader.getProperty("base.url");
        this.salesApiHelper = new SalesApiHelper(apiBaseUrl);
    }

    @Given("I have a valid {string} token for sales API")
    public void i_have_a_valid_token_for_sales_api(String role) {
        String token = getTokenByLogin(role);
        salesApiHelper.setToken(token);
    }

    private String getTokenByLogin(String role) {
        String baseUrl = ConfigLoader.getProperty("base.url");
        String username;
        String password;

        if (role.equalsIgnoreCase("Admin")) {
            username = ConfigLoader.getProperty("admin.username");
            password = ConfigLoader.getProperty("admin.password");
            if (username == null) username = "admin";
            if (password == null) password = "admin123";
        } else {
            username = ConfigLoader.getProperty("user.username");
            password = ConfigLoader.getProperty("user.password");
            if (username == null) username = "testuser";
            if (password == null) password = "password123";
        }

        try {
            Response loginResponse = given()
                .contentType("application/json")
                .body(String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password))
                .post(baseUrl + "/api/auth/login");

            if (loginResponse.getStatusCode() == 200) {
                return loginResponse.jsonPath().getString("token");
            } else {
                String token = ConfigLoader.getProperty(role.toLowerCase() + ".token");
                return token != null ? token : "";
            }
        } catch (Exception e) {
            String token = ConfigLoader.getProperty(role.toLowerCase() + ".token");
            return token != null ? token : "";
        }
    }

    @Given("I have no authentication token for sales API")
    public void i_have_no_authentication_token_for_sales_api() {
        salesApiHelper.setToken(null);
    }

    @When("I send a GET request to retrieve all sales")
    public void i_send_a_get_request_to_retrieve_all_sales() {
        response = salesApiHelper.getAllSales();
    }

    @When("I send a GET request to retrieve sales with page {int} size {int} sorted by {string}")
    public void i_send_a_get_request_to_retrieve_sales_with_pagination(int page, int size, String sortBy) {
        response = salesApiHelper.getSalesWithPagination(page, size, sortBy);
    }

    @When("I send a GET request to retrieve sale with id {int}")
    public void i_send_a_get_request_to_retrieve_sale_with_id(int saleId) {
        response = salesApiHelper.getSaleById(saleId);
    }

    @When("I send a POST request to create a sale for plant {int} with quantity {int}")
    public void i_send_a_post_request_to_create_a_sale(int plantId, int quantity) {
        response = salesApiHelper.createSale(plantId, quantity, "Test Buyer");
        if (response.getStatusCode() == 201) {
            try {
                createdSaleId = response.jsonPath().getInt("id");
            } catch (Exception e) {
                // Sale created but ID extraction failed
            }
        }
    }

    @When("I send a POST request to create a sale for plant {int} with invalid quantity {int}")
    public void i_send_a_post_request_to_create_a_sale_with_invalid_quantity(int plantId, int quantity) {
        response = salesApiHelper.createSaleWithInvalidQuantity(plantId, quantity);
    }

    @When("I send a DELETE request to delete sale with id {int}")
    public void i_send_a_delete_request_to_delete_sale(int saleId) {
        response = salesApiHelper.deleteSale(saleId);
    }

    @When("I send a GET request to search sales with term {string}")
    public void i_send_a_get_request_to_search_sales(String searchTerm) {
        response = salesApiHelper.searchSales(searchTerm);
    }

    @Then("the sales API response status code should be {int}")
    public void the_sales_api_response_status_code_should_be(int statusCode) {
        Assert.assertEquals(response.getStatusCode(), statusCode, 
            "Expected status code " + statusCode + " but got " + response.getStatusCode());
    }

    @Then("the sales API response should contain sales list")
    public void the_sales_api_response_should_contain_sales_list() {
        try {
            Object salesList = response.jsonPath().getList("$");
            Assert.assertNotNull(salesList, "Response should contain a sales list");
        } catch (Exception e) {
            try {
                Object content = response.jsonPath().getList("content");
                Assert.assertNotNull(content, "Response should contain sales list in 'content' field");
            } catch (Exception e2) {
                Assert.fail("Response should contain sales list. Body: " + response.getBody().asString());
            }
        }
    }

    @Then("the sales API response should contain sale details")
    public void the_sales_api_response_should_contain_sale_details() {
        try {
            Integer saleId = response.jsonPath().getInt("id");
            Assert.assertNotNull(saleId, "Response should contain sale ID");
            Assert.assertTrue(saleId > 0, "Sale ID should be positive");
        } catch (Exception e) {
            Assert.fail("Response should contain 'id' field. Body: " + response.getBody().asString());
        }

        try {
            Object plant = response.jsonPath().get("plant");
            Assert.assertNotNull(plant, "Response should contain plant object");
            
            String plantName = response.jsonPath().getString("plant.name");
            Assert.assertNotNull(plantName, "Plant should have a name");
        } catch (Exception e) {
            Assert.fail("Response should contain 'plant' object. Body: " + response.getBody().asString());
        }

        try {
            Integer quantity = response.jsonPath().getInt("quantity");
            Assert.assertNotNull(quantity, "Response should contain quantity");
            Assert.assertTrue(quantity > 0, "Quantity should be positive");
        } catch (Exception e) {
            Assert.fail("Response should contain 'quantity' field. Body: " + response.getBody().asString());
        }

        try {
            Object totalPrice = response.jsonPath().get("totalPrice");
            Assert.assertNotNull(totalPrice, "Response should contain totalPrice");
        } catch (Exception e) {
            Assert.fail("Response should contain 'totalPrice' field. Body: " + response.getBody().asString());
        }

        try {
            String soldAt = response.jsonPath().getString("soldAt");
            Assert.assertNotNull(soldAt, "Response should contain soldAt timestamp");
        } catch (Exception e) {
            Assert.fail("Response should contain 'soldAt' field. Body: " + response.getBody().asString());
        }
    }

    @Then("the sales API response should contain error message")
    public void the_sales_api_response_should_contain_error_message() {
        try {
            String errorMessage = response.jsonPath().getString("message");
            Assert.assertNotNull(errorMessage, "Response should contain error message");
        } catch (Exception e) {
            try {
                String error = response.jsonPath().getString("error");
                Assert.assertNotNull(error, "Response should contain error field");
            } catch (Exception e2) {
                Assert.fail("Response should contain error message. Body: " + response.getBody().asString());
            }
        }
    }

    @Then("the sales API response should have no content")
    public void the_sales_api_response_should_have_no_content() {
        String body = response.getBody().asString();
        Assert.assertTrue(body == null || body.isEmpty(), 
            "Response should have no content but got: " + body);
    }
}
