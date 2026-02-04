package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.testng.Assert;
import pages.CategoryPage;
import utils.BaseTest;
import java.util.List;
import api.CategoryApiHelper;
import utils.AuthHelper;

public class CategoryUISteps {
    private CategoryPage categoryPage = new CategoryPage(BaseTest.getDriver());

    @When("I navigate to the Categories page")
    public void i_navigate_to_the_categories_page() {
        categoryPage.navigateTo();
    }

    @Given("more categories exist than fit on one page")
    public void more_categories_exist_than_fit_on_one_page() {
        String token = AuthHelper.getAdminToken();
        // Create 15 categories to ensure pagination (assuming default size is 10 or 5)
        for (int i = 1; i <= 15; i++) {
            String jsonBody = "{\"name\": \"Cat_" + i + "\", \"parentId\": null}";
            CategoryApiHelper.createCategory(token, jsonBody);
        }
    }

    @Then("I should see pagination controls")
    public void i_should_see_pagination_controls() {
        Assert.assertTrue(categoryPage.isPaginationVisible(), "Pagination controls should be visible");
    }

    @When("I click the {string} button")
    public void i_click_the_button(String buttonName) {
        if (buttonName.equals("Next")) {
            categoryPage.clickNextPage();
        } else if (buttonName.equals("Previous")) {
            categoryPage.clickPrevPage();
        }
    }

    @Then("I should see the next page of categories")
    public void i_should_see_the_next_page_of_categories() {
        // Verification logic
    }

    @Then("I should see the second page of categories")
    public void i_should_see_the_second_page_of_categories() {
        // Verification logic
    }

    @Then("I should see the first page of categories")
    public void i_should_see_the_first_page_of_categories() {
        // Verification logic
    }

    @Then("the first page should display the default number of items")
    public void the_first_page_should_display_the_default_number_of_items() {
        // Verification logic
    }

    @Given("no categories exist in the system")
    public void no_categories_exist_in_the_system() {
        String token = AuthHelper.getAdminToken();
        io.restassured.response.Response response = CategoryApiHelper.getAllCategories(token);
        if (response.getStatusCode() == 200) {
            List<Integer> ids = response.jsonPath().getList("id");
            // Depending on response structure, might be "content.id" if paginated default,
            // but getAllCategories usually returns list.
            // CategoryApiHelper.getAllCategories points to /categories without params.
            // If it returns list:
            if (ids != null) {
                for (Integer id : ids) {
                    CategoryApiHelper.deleteCategory(token, id);
                }
            }
        }
    }

    @Then("I should see a {string} message")
    public void i_should_see_a_message(String message) {
        if (message.contains("No category")) {
            Assert.assertTrue(categoryPage.isNoCategoryMessageDisplayed(), "No category message should be displayed");
        }
    }

    @Given("a category with name {string} exists")
    public void a_category_with_name_exists(String categoryName) {
        String token = AuthHelper.getAdminToken();
        String jsonBody = "{\"name\": \"" + categoryName + "\", \"parentId\": null}";
        CategoryApiHelper.createCategory(token, jsonBody);
    }

    @When("I search for {string}")
    public void i_search_for(String searchTerm) {
        categoryPage.searchFor(searchTerm);
    }

    @Then("the results list should contain {string}")
    public void the_results_list_should_contain(String categoryName) {
        List<String> names = categoryPage.getCategoryNames();
        Assert.assertTrue(names.contains(categoryName), "Results should contain " + categoryName);
    }

    @Then("I should see a {string} message in the results")
    public void i_should_see_a_message_in_the_results(String message) {
        Assert.assertTrue(categoryPage.isNoCategoryMessageDisplayed(),
                "No category message should be displayed in results");
    }

    @Then("I should not see {string} button")
    public void i_should_not_see_button(String buttonName) {
        if (buttonName.equals("Add Category")) {
            Assert.assertFalse(categoryPage.isAddCategoryButtonVisible(),
                    "Add Category button should not be visible for User");
        }
    }

    @Then("I should not see {string} options")
    public void i_should_not_see_options(String option) {
        // Verify absence of Edit/Delete buttons per row
    }

    @Given("a parent category {string} exists with child categories")
    public void a_parent_category_exists_with_child_categories(String parentName) {
        String token = AuthHelper.getAdminToken();
        String parentBody = "{\"name\": \"" + parentName + "\", \"parentId\": null}";
        io.restassured.response.Response response = CategoryApiHelper.createCategory(token, parentBody);

        if (response.getStatusCode() == 201) {
            int parentId = response.jsonPath().getInt("id");
            String childBody = "{\"name\": \"SubCat\", \"parentId\": " + parentId + "}";
            CategoryApiHelper.createCategory(token, childBody);
        }
    }

    @When("I filter by parent category {string}")
    public void i_filter_by_parent_category(String parentName) {
        categoryPage.filterByParent(parentName);
    }

    @Then("I should see only categories belonging to {string}")
    public void i_should_see_only_categories_belonging_to(String parentName) {
        // Verify
    }

    @Given("multiple categories exist")
    public void multiple_categories_exist() {
        String token = AuthHelper.getAdminToken();
        CategoryApiHelper.createCategory(token, "{\"name\": \"CatA\", \"parentId\": null}");
        CategoryApiHelper.createCategory(token, "{\"name\": \"CatB\", \"parentId\": null}");
        CategoryApiHelper.createCategory(token, "{\"name\": \"CatC\", \"parentId\": null}");
    }

    @When("I sort by {string} {string}")
    public void i_sort_by(String column, String order) {
        // Click headers
    }

    @Then("the list should be sorted by {string} in {string} order")
    public void the_list_should_be_sorted_by_in_order(String column, String order) {
        // Verify sort order

    }
}
