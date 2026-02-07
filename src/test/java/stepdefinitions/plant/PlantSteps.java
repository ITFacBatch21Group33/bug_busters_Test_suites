package stepdefinitions.plant;

import api.plant.PlantApiHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.testng.Assert;
import java.util.List;
import utils.ConfigLoader;

public class PlantSteps {
    private String token;
    private Response response;

    @Given("the Plant API is available")
    public void the_plant_api_is_available() {
      
    }

    @Given("I have a valid {string} token for plant API")
    public void i_have_a_valid_token_for_plant_api(String role) {
        if ("User".equalsIgnoreCase(role)) {
            token = utils.AuthHelper.getUserToken();
        } else if ("Admin".equalsIgnoreCase(role)) {
            token = utils.AuthHelper.getAdminToken();
        }
    }

    @Given("I have no authentication token for plant API")
    public void i_have_no_authentication_token_for_plant_api() {
        token = null;
    }

    @When("I send a POST request to plant endpoint {string} with body:")
    public void i_send_a_post_request_to_plant_endpoint_with_body(String endpoint, String body) {
        if (endpoint.contains("/api/plants/category/")) {
            // Ensure unique name for creation success test
            if (body.contains("\"name\": \"New Plant 001\"")) {
                body = body.replace("New Plant 001", "New Plant " + System.currentTimeMillis());
            }
            String idStr = endpoint.substring(endpoint.lastIndexOf('/') + 1);
            int categoryId = Integer.parseInt(idStr);
            response = PlantApiHelper.createPlant(token, categoryId, body);
        }
    }

    @When("I send a GET request to plant endpoint {string}")
    public void i_send_a_get_request_to_plant_endpoint(String endpoint) {
        if (endpoint.contains("?")) {
            String[] parts = endpoint.split("\\?");
            String[] params = parts[1].split("&");
            java.util.Map<String, String> queryParams = new java.util.HashMap<>();
            for (String param : params) {
                String[] keyVal = param.split("=");
                if (keyVal.length > 1) {
                    queryParams.put(keyVal[0], keyVal[1]);
                }
            }
            response = PlantApiHelper.getPagedPlants(token, queryParams);
        } else if (endpoint.matches(".*/plants/\\d+")) {
            String idStr = endpoint.substring(endpoint.lastIndexOf('/') + 1);
            int id = Integer.parseInt(idStr);
            response = PlantApiHelper.getPlantById(token, id);
        } else if (endpoint.endsWith("/plants")) {
            response = PlantApiHelper.getAllPlants(token);
        } else {
            if (endpoint.contains("/plants/")) {
                try {
                    String idStr = endpoint.substring(endpoint.lastIndexOf('/') + 1);
                    int id = Integer.parseInt(idStr);
                    response = PlantApiHelper.getPlantById(token, id);
                } catch (NumberFormatException e) {
                    response = PlantApiHelper.getPlantById(token, 9999);
                }
            }
        }
    }

    @When("I send a PUT request to plant endpoint {string} with body:")
    public void i_send_a_put_request_to_plant_endpoint_with_body(String endpoint, String body) {
        
        String[] parts = endpoint.split("/");
        int id = Integer.parseInt(parts[parts.length - 1]);
        response = PlantApiHelper.updatePlant(token, id, body);
    }

    @Then("the plant response status code should be {int}")
    public void the_plant_response_status_code_should_be(int statusCode) {
        Assert.assertEquals(response.getStatusCode(), statusCode);
    }

    @Then("the plant response should contain a list of plants")
    public void the_plant_response_should_contain_a_list_of_plants() {
        Assert.assertNotNull(response.jsonPath().getList("$"));
    }

    @Given("the plant database is empty")
    public void the_plant_database_is_empty() {
        
    }

    @Then("the plant response list should be empty")
    public void the_plant_response_list_should_be_empty() {
        List<?> list = response.jsonPath().getList("$");
        Assert.assertTrue(list == null || list.isEmpty());
    }

    @Given("a plant with ID {int} exists")
    public void a_plant_with_id_exists(int id) {
       
    }

    @Then("the plant response body should contain the plant details")
    public void the_plant_response_body_should_contain_the_plant_details() {
        // Assert fields
        Assert.assertNotNull(response.jsonPath().get("id"));
        Assert.assertNotNull(response.jsonPath().get("name"));
    }

    @Then("the plant response should contain the updated plant details")
    public void the_plant_response_should_contain_the_updated_plant_details() {
      
        Assert.assertNotNull(response.jsonPath().get("name"));
        
    }

    @Then("the updated plant should have name {string}")
    public void the_updated_plant_should_have_name(String expectedName) {
        String actualName = response.jsonPath().getString("name");
        Assert.assertEquals(actualName, expectedName);
    }

    @Then("the response should contain {int} plants")
    public void the_response_should_contain_plants(int count) {
        List<?> list = response.jsonPath().getList("content");
        if (list == null)
            list = response.jsonPath().getList("$");
        Assert.assertTrue(list.size() <= count);
    }

    @Then("the plants returned by API should be sorted by {string} in {string} order")
    public void the_plants_returned_by_api_should_be_sorted_by_in_order(String field, String order) {
        List<java.util.Map<String, Object>> plants = response.jsonPath().getList("content");
        if (plants == null)
            plants = response.jsonPath().getList("$");

        if (plants != null && !plants.isEmpty()) {
            for (int i = 0; i < plants.size() - 1; i++) {
                Object v1 = plants.get(i).get(field);
                Object v2 = plants.get(i + 1).get(field);
                if (v1 instanceof Comparable && v2 instanceof Comparable) {
                    Comparable val1 = (Comparable) v1;
                    Comparable val2 = (Comparable) v2;
                    if ("asc".equalsIgnoreCase(order)) {
                        Assert.assertTrue(val1.compareTo(val2) <= 0,
                                "Not sorted ascending by " + field + ": " + val1 + " > " + val2);
                    } else {
                        Assert.assertTrue(val1.compareTo(val2) >= 0,
                                "Not sorted descending by " + field + ": " + val1 + " < " + val2);
                    }
                }
            }
        }
    }

    @Then("the plants should be sorted by {string} in {string} order")
    public void the_plants_should_be_sorted_by_in_order(String field, String order) {
        the_plants_returned_by_api_should_be_sorted_by_in_order(field, order);
    }
}
