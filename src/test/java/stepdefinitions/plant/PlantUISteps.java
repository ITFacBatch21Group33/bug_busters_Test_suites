package stepdefinitions.plant;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.Given;
import org.testng.Assert;
import pages.plant.PlantPage;
import utils.BaseTest;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections; 

public class PlantUISteps {
    private PlantPage plantPage = new PlantPage(BaseTest.getDriver());
    private List<String> previousPageNames;
    private pages.plant.EditPlantPage editPage;
    private pages.plant.AddPlantPage addPage;

    @When("I navigate to the Plant Management page")
    public void i_navigate_to_the_plant_management_page() {
        BaseTest.setUpDriver();
        plantPage.navigateTo();
    }

    @Then("the plant list should be displayed")
    public void the_plant_list_should_be_displayed() {
        Assert.assertTrue(plantPage.isPlantListDisplayed(), "Plant list should be displayed");
    }

    @Given("no plants exist in the system")
    public void no_plants_exist_in_the_system() {
        // Ensure test environment has no plants; assume external fixture or seed cleanup
    }

    @When("I enter {string} in the search field")
    public void i_enter_in_the_search_field(String text) {
        plantPage.enterSearchText(text);
    }

    @When("I click the Search button")
    public void i_click_the_search_button() {
        plantPage.clickSearch();
    }

    @Then("the results list should contain {string}")
    public void the_results_list_should_contain(String name) {
        java.util.List<String> names = plantPage.getPlantNames();
        Assert.assertTrue(names.contains(name), "Results should contain " + name);
    }

    @Given("categories exist")
    public void categories_exist() {
        // Placeholder: ensure categories exist in the system (seed if necessary)
    }

    @When("I select category filter {string}")
    public void i_select_category_filter(String category) {
        plantPage.selectCategoryFilter(category);
    }

    @Then("only plants belonging to {string} are displayed")
    public void only_plants_belonging_to_are_displayed(String category) {
        java.util.List<String> cats = plantPage.getPlantCategories();
        Assert.assertFalse(cats.isEmpty(), "Expected filtered results to show at least one plant");
        for (String c : cats) {
            Assert.assertEquals(c, category, "Expected plant category to match filter");
        }
    }

    @When("I sort by {string}")
    public void i_sort_by(String column) {
        plantPage.clickSortByColumn(column);
    }

    @When("I click the Reset button")
    public void i_click_the_reset_button() {
        plantPage.clickReset();
    }

    @Then("the search field should be cleared")
    public void the_search_field_should_be_cleared() {
        Assert.assertTrue(plantPage.SearchBoxValue() == null || plantPage.SearchBoxValue().isEmpty(), "Search box should be cleared");
    }

    @Then("the category filter should be reset")
    public void the_category_filter_should_be_reset() {
        String sel = plantPage.getSelectedCategory();
        Assert.assertTrue(sel == null || sel.isEmpty() || sel.equalsIgnoreCase("All"), "Category filter should be reset to default");
    }

    @Then("sorting should be reset to default")
    public void sorting_should_be_reset_to_default() {
        Assert.assertTrue(plantPage.isSortReset(), "Sorting should be reset to default (no sorted header)");
    }

    @Then("the plant list should be sorted by \"{string}\" in ascending order")
    public void the_plant_list_should_be_sorted_by_in_ascending_order(String column) {
        if (column.equalsIgnoreCase("Name")) {
            List<String> names = plantPage.getPlantNames();
            ArrayList<String> sorted = new ArrayList<>(names);
            Collections.sort(sorted, String.CASE_INSENSITIVE_ORDER);
            Assert.assertEquals(names, sorted, "Expected plant names to be sorted ascending by " + column);
        } else if (column.equalsIgnoreCase("Price")) {
            List<Double> prices = plantPage.getPlantPrices();
            ArrayList<Double> sorted = new ArrayList<>(prices);
            Collections.sort(sorted);
            Assert.assertEquals(prices, sorted, "Expected plant prices to be sorted ascending by " + column);
        } else if (column.equalsIgnoreCase("Quantity")) {
            List<Integer> qtys = plantPage.getPlantQuantities();
            ArrayList<Integer> sorted = new ArrayList<>(qtys);
            Collections.sort(sorted);
            Assert.assertEquals(qtys, sorted, "Expected plant quantities to be sorted ascending by " + column);
        } else {
            Assert.fail("Sorting check not implemented for column: " + column);
        }
    }

    @Then("the full plant list should be displayed")
    public void the_full_plant_list_should_be_displayed() {
        Assert.assertTrue(plantPage.isPlantListDisplayed(), "Plant list should be displayed");
        Assert.assertTrue(plantPage.getRowCount() > 0, "Expected at least one plant to be visible in full list");
    }

    @Then("I should see pagination controls")
    public void i_should_see_pagination_controls() {
        Assert.assertTrue(plantPage.isPaginationNextVisible(), "Pagination controls should be visible");
    }

    @When("I click the Next page button")
    public void i_click_the_next_page_button() {
        previousPageNames = plantPage.getPlantNames();
        plantPage.clickNextPage();
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        List<String> current = plantPage.getPlantNames();
        Assert.assertNotEquals(previousPageNames, current, "Expected different plants on next page");
    }

    @When("I click the Previous page button")
    public void i_click_the_previous_page_button() {
        List<String> before = plantPage.getPlantNames();
        plantPage.clickPrevPage();
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        List<String> after = plantPage.getPlantNames();
        Assert.assertNotEquals(before, after, "Expected different plants after clicking previous page");
    }

    @When("I click page number {int}")
    public void i_click_page_number(int page) {
        previousPageNames = plantPage.getPlantNames();
        plantPage.clickPageNumber(page);
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        List<String> curr = plantPage.getPlantNames();
        Assert.assertNotEquals(previousPageNames, curr, "Expected page change when clicking specific page number");
    }

    @Then("correct plants displayed for selected page")
    public void correct_plants_displayed_for_selected_page() {
        Assert.assertTrue(plantPage.getRowCount() > 0, "Expected plants to be shown for selected page");
    }

    @Then("I should not see {string} button")
    public void i_should_not_see_button(String buttonName) {
        if (buttonName.equalsIgnoreCase("Add Plant")) {
            Assert.assertFalse(plantPage.isAddButtonVisible(), "Add Plant button should not be visible for User");
        } else {
            Assert.fail("Unexpected button assertion: " + buttonName);
        }
    }

    @When("I click Edit for plant {string}")
    public void i_click_edit_for_plant(String plantName) {
        plantPage.clickEditPlant(plantName);
        editPage = new pages.plant.EditPlantPage(BaseTest.getDriver());
    }

    @When("I click Add Plant")
    public void i_click_add_plant() {
        plantPage.clickAddPlant();
        addPage = new pages.plant.AddPlantPage(BaseTest.getDriver());
    }

    @Then("the Add Plant page should be displayed")
    public void the_add_plant_page_should_be_displayed() {
        Assert.assertTrue(addPage.isAddFormDisplayed(), "Expected Add Plant form to be displayed");
    }

    @When("I clear mandatory fields on the Add Plant page")
    public void i_clear_mandatory_fields_on_the_add_plant_page() {
        addPage.clearMandatoryFields();
    }

    @When("I click Save on the Add Plant page")
    public void i_click_save_on_the_add_plant_page() {
        addPage.clickSave();
    }

    @When("I click Cancel on the Add Plant page")
    public void i_click_cancel_on_the_add_plant_page() {
        addPage.clickCancel();
    }

    @Then("I should see validation messages for mandatory fields on the Add Plant page")
    public void i_should_see_validation_messages_for_mandatory_fields_on_the_add_plant_page() {
        Assert.assertTrue(addPage.isNameErrorDisplayed() || addPage.isPriceErrorDisplayed() || addPage.isQuantityErrorDisplayed(),
                "Expected at least one validation message for mandatory fields on Add Plant page");
    }

    @When("I enter plant name {string} on the Add Plant page")
    public void i_enter_plant_name_on_the_add_plant_page(String name) {
        addPage.enterName(name);
    }

    @When("I enter plant price {string} on the Add Plant page")
    public void i_enter_plant_price_on_the_add_plant_page(String price) {
        addPage.enterPrice(price);
    }

    @When("I enter plant quantity {string} on the Add Plant page")
    public void i_enter_plant_quantity_on_the_add_plant_page(String qty) {
        addPage.enterQuantity(qty);
    }

    @Then("I should see name validation message on the Add Plant page")
    public void i_should_see_name_validation_message_on_the_add_plant_page() {
        Assert.assertTrue(addPage.isNameErrorDisplayed(), "Expected name validation message on Add Plant page");
    }

    @Then("I should not see name validation message on the Add Plant page")
    public void i_should_not_see_name_validation_message_on_the_add_plant_page() {
        Assert.assertFalse(addPage.isNameErrorDisplayed(), "Expected no name validation message on Add Plant page");
    }

    @Then("I should see price validation message on the Add Plant page")
    public void i_should_see_price_validation_message_on_the_add_plant_page() {
        Assert.assertTrue(addPage.isPriceErrorDisplayed(), "Expected price validation message on Add Plant page");
    }

    @Then("I should not see price validation message on the Add Plant page")
    public void i_should_not_see_price_validation_message_on_the_add_plant_page() {
        Assert.assertFalse(addPage.isPriceErrorDisplayed(), "Expected no price validation message on Add Plant page");
    }

    @Then("I should see quantity validation message on the Add Plant page")
    public void i_should_see_quantity_validation_message_on_the_add_plant_page() {
        Assert.assertTrue(addPage.isQuantityErrorDisplayed(), "Expected quantity validation message on Add Plant page");
    }

    @Then("I should not see quantity validation message on the Add Plant page")
    public void i_should_not_see_quantity_validation_message_on_the_add_plant_page() {
        Assert.assertFalse(addPage.isQuantityErrorDisplayed(), "Expected no quantity validation message on Add Plant page");
    }

    @When("I clear mandatory fields on the Edit Plant page")
    public void i_clear_mandatory_fields_on_the_edit_plant_page() {
        editPage.clearMandatoryFields();
    }

    @When("I click Save on the Edit Plant page")
    public void i_click_save_on_the_edit_plant_page() {
        editPage.clickSave();
    }

    @When("I enter plant name {string} on the Edit Plant page")
    public void i_enter_plant_name_on_the_edit_plant_page(String name) {
        editPage.enterName(name);
    }

    @Then("I should see name validation message")
    public void i_should_see_name_validation_message() {
        Assert.assertTrue(editPage.isNameErrorDisplayed(), "Expected name validation message to be displayed");
    }

    @Then("I should not see name validation message")
    public void i_should_not_see_name_validation_message() {
        Assert.assertFalse(editPage.isNameErrorDisplayed(), "Expected no name validation message for valid name");
    }

    @When("I enter plant price {string} on the Edit Plant page")
    public void i_enter_plant_price_on_the_edit_plant_page(String price) {
        editPage.enterPrice(price);
    }

    @Then("I should see price validation message")
    public void i_should_see_price_validation_message() {
        Assert.assertTrue(editPage.isPriceErrorDisplayed(), "Expected price validation message to be displayed");
    }

    @Then("I should not see price validation message")
    public void i_should_not_see_price_validation_message() {
        Assert.assertFalse(editPage.isPriceErrorDisplayed(), "Expected no price validation message for valid price");
    }

    @When("I enter plant quantity {string} on the Edit Plant page")
    public void i_enter_plant_quantity_on_the_edit_plant_page(String qty) {
        editPage.enterQuantity(qty);
    }

    @Then("I should see quantity validation message")
    public void i_should_see_quantity_validation_message() {
        Assert.assertTrue(editPage.isQuantityErrorDisplayed(), "Expected quantity validation message to be displayed");
    }

    @Then("I should not see quantity validation message")
    public void i_should_not_see_quantity_validation_message() {
        Assert.assertFalse(editPage.isQuantityErrorDisplayed(), "Expected no quantity validation message for valid quantity");
    }

    @When("I click Cancel on the Edit Plant page")
    public void i_click_cancel_on_the_edit_plant_page() {
        editPage.clickCancel();
    }

    @Then("I should be redirected to the Plant Management page")
    public void i_should_be_redirected_to_the_plant_management_page() {
        // Verify plant list is displayed indicating we are back on the Plant Management page
        Assert.assertTrue(plantPage.isPlantListDisplayed(), "Expected to be redirected to Plant Management page with list displayed");
    }

    @Then("I should see plant named {string} in the list")
    public void i_should_see_plant_named_in_the_list(String name) {
        Assert.assertTrue(plantPage.isPlantPresent(name), "Expected to find plant named " + name + " in the list");
    }

    @Then("the Add Plant button should be visible")
    public void the_add_plant_button_should_be_visible() {
        Assert.assertTrue(plantPage.isAddButtonVisible(), "Add Plant button should be visible for Admin");
    }

    @Then("edit and delete actions should be visible for plants")
    public void edit_and_delete_actions_should_be_visible_for_plants() {
        Assert.assertTrue(plantPage.areActionButtonsVisible(), "Expected edit or delete actions to be visible for at least one plant row");
    }

    @Then("I should see validation messages for mandatory fields")
    public void i_should_see_validation_messages_for_mandatory_fields() {
        Assert.assertTrue(editPage.isNameErrorDisplayed() || editPage.isPriceErrorDisplayed() || editPage.isQuantityErrorDisplayed(),
                "Expected at least one validation message for mandatory fields");
    }

    @Then("I should see a {string} message in the results")
    public void i_should_see_a_message_in_the_results(String message) {
        if (message.contains("No plants")) {
            Assert.assertTrue(plantPage.isNoDataMessageDisplayed(), "No plants message should be displayed");
        } else {
            Assert.fail("Unexpected message assertion: " + message);
        }
    }
}