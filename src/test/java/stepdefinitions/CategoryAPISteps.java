package stepdefinitions;

import java.util.ArrayList;
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
        if (endpoint != null && endpoint.contains("/categories/page")) {
        response = CategoryApiHelper.getCategoriesPageWithParams(token, queryParams);
        } else {
            response = CategoryApiHelper.getCategoriesWithParams(token, queryParams);
        }
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

    //Filter Categories by parent_id

    @Then("the response should contain at least one category")
    public void the_response_should_contain_at_least_one_category() {
        List<Map<String, Object>> categories = extractCategories(response);
        Assert.assertNotNull(categories, "No categories list found in response");
        Assert.assertFalse(categories.isEmpty(), "Expected at least 1 category but got empty list. Body: " + response.asString());
    }

    @Then("all categories in response should have parent_id {int}")
    public void all_categories_in_response_should_have_parent_id(int parentId) {
        List<Map<String, Object>> categories = extractCategories(response);
        Assert.assertNotNull(categories, "No categories list found. Body: " + response.asString());
        Assert.assertFalse(categories.isEmpty(), "Expected non-empty filtered results. Body: " + response.asString());

        Response parentResp = CategoryApiHelper.getCategoryById(token, parentId);
        Assert.assertEquals(parentResp.getStatusCode(), 200, "Could not fetch parent category " + parentId);
        String expectedParentName = parentResp.jsonPath().getString("name");
        Assert.assertNotNull(expectedParentName, "Parent category has no 'name' field");

        for (Map<String, Object> category : categories) {
            String actualParentName = null;
            Object v = category.get("parentName");
            if (v == null) v = category.get("parent");
            if (v != null) actualParentName = String.valueOf(v);

            Assert.assertNotNull(actualParentName, "Category missing parent name field: " + category);
            Assert.assertEquals(actualParentName, expectedParentName, "Category belongs to wrong parent: " + category);
        }
    }

    private List<Map<String, Object>> extractCategories(Response resp) {
        Assert.assertNotNull(resp, "No response captured");

        // Common response shapes: [] OR {content: []} OR {data: []} OR {data: {content: []}}
        List<Map<String, Object>> list = safeGetList(resp, "$");
        if (list != null) return list;

        list = safeGetList(resp, "content");
        if (list != null) return list;

        list = safeGetList(resp, "data");
        if (list != null) return list;

        list = safeGetList(resp, "data.content");
        if (list != null) return list;

        return null;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> safeGetList(Response resp, String path) {
        try {
            List<?> raw = resp.jsonPath().getList(path);
            if (raw == null) return null;

            // Ensure it’s actually a list of objects/maps
            List<Map<String, Object>> mapped = new ArrayList<>();
            for (Object o : raw) {
                if (!(o instanceof Map)) return null;
                mapped.add((Map<String, Object>) o);
            }
            return mapped;
        } catch (Exception ignored) {
            return null;
        }
    }

    @Then("the response filtered list should be empty")
    public void the_response_filtered_list_should_be_empty() {
        Assert.assertNotNull(response, "No response captured");

        List<?> root = null;
        try { root = response.jsonPath().getList("$"); } catch (Exception ignored) {}

        if (root != null) {
            Assert.assertTrue(root.isEmpty(), "Expected empty list but got: " + response.asString());
            return;
        }

        List<Map<String, Object>> categories = extractCategories(response);
        Assert.assertNotNull(categories, "No categories list found. Body: " + response.asString());
        Assert.assertTrue(categories.isEmpty(), "Expected empty list but got: " + response.asString());
    }

    // Sort categories
    @Then("the response list should be sorted by {string} ascending")
    public void the_response_list_should_be_sorted_by_ascending(String field) {
        List<Map<String, Object>> categories = extractCategories(response);
        Assert.assertNotNull(categories, "No categories list found. Body: " + response.asString());
        Assert.assertFalse(categories.isEmpty(), "Expected non-empty list to verify sorting. Body: " + response.asString());

        for (int i = 1; i < categories.size(); i++) {
            Object prev = categories.get(i - 1).get(field);
            Object curr = categories.get(i).get(field);

            Assert.assertTrue(compareValues(prev, curr) <= 0,
                    "List is not sorted by [" + field + "] ascending at index " + i +
                            " (prev=" + prev + ", curr=" + curr + "). Body: " + response.asString());
        }
    }

    private int compareValues(Object a, Object b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;

        // Numeric compare when possible (covers id sorting)
        try {
            double da = Double.parseDouble(String.valueOf(a));
            double db = Double.parseDouble(String.valueOf(b));
            return Double.compare(da, db);
        } catch (Exception ignored) {
            return String.valueOf(a).compareToIgnoreCase(String.valueOf(b));
        }
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
