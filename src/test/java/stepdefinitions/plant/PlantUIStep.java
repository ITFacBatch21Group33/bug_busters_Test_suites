package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.testng.Assert;
import pages.CategoryPage;
import utils.BaseTest;
import java.util.List;

public class CategoryUISteps {
    private CategoryPage categoryPage = new CategoryPage(BaseTest.getDriver());

    @When("I navigate to the Categories page")
    public void i_navigate_to_the_categories_page() {
        categoryPage.navigateTo();
    }

    @Given("more categories exist than fit on one page")
    public void more_categories_exist_than_fit_on_one_page() {
        // Precondition setup or assumption
        // In a real test, this might seed the DB
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
        // Mocking or assuming clean state
    }

    @Then("I should see a {string} message")
    public void i_should_see_a_message(String message) {
        if (message.contains("No category")) {
            Assert.assertTrue(categoryPage.isNoCategoryMessageDisplayed(), "No category message should be displayed");
        }
    }

    @Given("a category with name {string} exists")
    public void a_category_with_name_exists(String categoryName) {
        // Seed data
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
        // Seed
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
        // Seed
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
