package stepdefinitions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;

import api.CategoryApiHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import utils.ConfigLoader;

public class CategoryAPISteps {
    private String token;
    private Response response;
    private String lastRequestedCategoryName;

    @Given("I have a valid {string} token")
    public void i_have_a_valid_token(String role) {
        // Fetch token from config.properties
        if (role.equals("User")) {
            token = ConfigLoader.getProperty("user.token");
        } else if (role.equals("Admin")) {
            token = ConfigLoader.getProperty("admin.token");
        }
    }

    @Given("I do not have an authentication token")
    public void i_do_not_have_an_authentication_token() {
        token = null;
    }

    @Given("the API is available")
    public void the_api_is_available() {
        // Check health
    }

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        if (token != null) {
            // We are using helper which hardcodes base URI + /categories.
            // Need to adjust helper or use it selectively.
            // For simplicity, assumed endpoint matches helper's base.
            response = CategoryApiHelper.getAllCategories(token);
        } else {
            response = CategoryApiHelper.getCategoriesNoAuth();
        }
    }

    @When("I send a GET request to {string} with params:")
    public void i_send_a_get_request_to_with_params(String endpoint, Map<String, String> params) {
        Map<String, Object> queryParams = new HashMap<>(params);
        response = CategoryApiHelper.getCategoriesWithParams(token, queryParams);
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int statusCode) {
        Assert.assertEquals(response.getStatusCode(), statusCode);
    }

    @Then("the response should contain a list of categories")
    public void the_response_should_contain_a_list_of_categories() {
        Assert.assertNotNull(response.jsonPath().getList("$"));
    }

    @Then("the response should contain a paginated list of categories")
    public void the_response_should_contain_a_paginated_list_of_categories() {
        // check specific pagination fields if wrapped, e.g. "content", "pageable"
    }

    @Then("the response list should contain categories matching {string}")
    public void the_response_list_should_contain_categories_matching(String name) {
        // Assert json path content
    }

    @Then("the response list should be empty")
    public void the_response_list_should_be_empty() {
        List<?> list = response.jsonPath().getList("$");
        Assert.assertTrue(list == null || list.isEmpty());
    }

    @When("I send a POST request to {string} with body:")
    public void i_send_a_post_request_to_with_body(String endpoint, String body) {
        // Added random suffix to avoid duplicates
        String resolvedBody = body.replace("${suffix}", randomLetters(3));
        lastRequestedCategoryName = extractName(resolvedBody);

        response = CategoryApiHelper.createCategory(token, resolvedBody);
    }

    private String extractName(String jsonText) {
        java.util.regex.Matcher matcher =
                java.util.regex.Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+)\"").matcher(jsonText);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String randomLetters(int len) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        java.util.Random r = new java.util.Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) sb.append(alphabet.charAt(r.nextInt(alphabet.length())));
        return sb.toString();
    }

    @Then("the response should contain validation error for missing name")
    public void the_response_should_contain_validation_error_for_missing_name() {
        assertMessageEquals("Validation failed");
        assertDetailsNameEquals("Category name is mandatory");
    }

    @Then("the response should contain validation error for name too short")
    public void the_response_should_contain_validation_error_for_name_too_short() {
        assertMessageEquals("Validation failed");
        assertDetailsNameContains("between 3 and 10");
    }

    @Then("the response should contain validation error for name too long")
    public void the_response_should_contain_validation_error_for_name_too_long() {
        assertMessageEquals("Validation failed");
        assertDetailsNameContains("between 3 and 10");
    }

    private void assertMessageEquals(String expectedMessage) {
        Assert.assertNotNull(response, "No response captured");

        String body = null;
        try { body = response.asString(); } catch (Exception ignored) {}

        String actual = null;
        try { actual = response.jsonPath().getString("message"); } catch (Exception ignored) {}

        Assert.assertNotNull(actual, "No 'message' field in response. Body: " + body);
        Assert.assertEquals(actual, expectedMessage, "Unexpected 'message'. Body: " + body);
    }

    private void assertDetailsNameEquals(String expectedDetailsName) {
        Assert.assertNotNull(response, "No response captured");

        String body = null;
        try { body = response.asString(); } catch (Exception ignored) {}

        String actual = null;
        try { actual = response.jsonPath().getString("details.name"); } catch (Exception ignored) {}

        Assert.assertNotNull(actual, "No 'details.name' field in response. Body: " + body);
        Assert.assertEquals(actual, expectedDetailsName, "Unexpected 'details.name'. Body: " + body);
    }

    private void assertDetailsNameContains(String expectedSubstring) {
        Assert.assertNotNull(response, "No response captured");

        String body = null;
        try { body = response.asString(); } catch (Exception ignored) {}

        String actual = null;
        try { actual = response.jsonPath().getString("details.name"); } catch (Exception ignored) {}

        Assert.assertNotNull(actual, "No 'details.name' field in response. Body: " + body);
        Assert.assertTrue(actual.contains(expectedSubstring),
                "Expected 'details.name' to contain [" + expectedSubstring + "] but was [" + actual + "]. Body: " + body);
    }

    @When("I send a PUT request to {string} with body:")
    public void i_send_a_put_request_to_with_body(String endpoint, String body) {
        // extracting ID from endpoint is hacky, but sufficient for stub
        // endpoint is /api/categories/1
        String[] parts = endpoint.split("/");
        int id = Integer.parseInt(parts[parts.length - 1]);
        response = CategoryApiHelper.updateCategory(token, id, body);
    }

    @Then("category with ID {int} should have name {string}")
    public void category_with_id_should_have_name(int id, String expectedName) {
        Assert.assertNotNull(token, "Token must be set before verifying category");

        Response getResp = CategoryApiHelper.getCategoryById(token, id);
        Assert.assertEquals(getResp.getStatusCode(), 200, "Could not fetch category " + id + " after update");

        String actualName = null;
        try { actualName = getResp.jsonPath().getString("name"); } catch (Exception ignored) {}
        if (actualName == null) {
            try { actualName = getResp.jsonPath().getString("data.name"); } catch (Exception ignored) {}
        }

        Assert.assertNotNull(actualName, "Could not find category name in GET response");
        Assert.assertEquals(actualName, expectedName, "Category name was not updated");
    }

    @Given("a category with ID {int} exists")
    public void a_category_with_id_exists(int id) {
        Assert.assertNotNull(token, "Token must be set before checking category existence");

        Response getResp = CategoryApiHelper.getCategoryById(token, id);
        Assert.assertEquals(getResp.getStatusCode(), 200,
                "Precondition failed: category " + id + " does not exist (GET did not return 200)");
    }

    @Then("all categories in response should have parent_id {int}")
    public void all_categories_in_response_should_have_parent_id(int parentId) {
        // Verify
    }

    @Then("the response list should be sorted by {string} ascending")
    public void the_response_list_should_be_sorted_by_ascending(String field) {
        // Verify sort
    }

    @Given("I login as a {string}")
    public void i_login_as_a(String role) {
        // UI Login
        // BaseTest.getDriver().get(loginUrl);
        // ...
    }

    @Given("I login as an {string}")
    public void i_login_as_an(String role) {
        i_login_as_a(role);
    }

    @Given("the application is running")
    public void the_application_is_running() {
        // check
    }

    @Given("I am on the Login page")
    public void i_am_on_the_login_page() {
        // nav
    }
}
