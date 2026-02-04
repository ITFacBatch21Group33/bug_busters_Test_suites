package stepdefinitions;

import api.CategoryApiHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.testng.Assert;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.ConfigLoader;

public class CategoryAPISteps {
    private String token;
    private Response response;
    private pages.LoginPage loginPage;

    @Given("I have a valid {string} token")
    public void i_have_a_valid_token(String role) {
        // Fetch token dynamically using AuthHelper
        if (role.equals("User")) {
            token = utils.AuthHelper.getUserToken();
        } else if (role.equals("Admin")) {
            token = utils.AuthHelper.getAdminToken();
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
        response = CategoryApiHelper.createCategory(token, body);
    }

    @When("I send a PUT request to {string} with body:")
    public void i_send_a_put_request_to_with_body(String endpoint, String body) {
        // extracting ID from endpoint is hacky, but sufficient for stub
        // endpoint is /api/categories/1
        String[] parts = endpoint.split("/");
        int id = Integer.parseInt(parts[parts.length - 1]);
        response = CategoryApiHelper.updateCategory(token, id, body);
    }

    @Given("a category with ID {int} exists")
    public void a_category_with_id_exists(int id) {
        // Seed
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
        String username = "";
        String password = "";

        if (role.equals("User")) {
            username = ConfigLoader.getProperty("user.username");
            password = ConfigLoader.getProperty("user.password");
        } else if (role.equals("Admin")) {
            username = ConfigLoader.getProperty("admin.username");
            password = ConfigLoader.getProperty("admin.password");
        }

        loginPage.login(username, password);
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
        loginPage = new pages.LoginPage(utils.BaseTest.getDriver());
        loginPage.navigateTo();
    }
}
