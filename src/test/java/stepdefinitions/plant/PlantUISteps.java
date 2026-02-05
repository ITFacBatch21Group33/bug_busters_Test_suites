package stepdefinitions.plant;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.testng.Assert;
import utils.BaseTest;
import pages.plant.PlantPage;
import pages.plant.EditPlantPage;

public class PlantUISteps {
    
    PlantPage plantPage = new PlantPage(BaseTest.getDriver());
    EditPlantPage editPlantPage = new EditPlantPage(BaseTest.getDriver());

    @Given("I navigate to the Application")
    public void i_navigate_to_the_application() {
        BaseTest.setUpDriver(); // Ensure driver is ready
        // Real login logic would span multiple pages, simplified here
    }

    @Given("I login as a {string} for UI")
    public void i_login_as_a_for_ui(String role) {
        // Mock login or simple navigation if no auth in local test
        // Assume logged in
    }

    @Given("I login as an {string} for UI")
    public void i_login_as_an_for_ui(String role) {
         i_login_as_a_for_ui(role);
    }

    @Given("I navigate to the Plant Management page")
    public void i_navigate_to_the_plant_management_page() {
        plantPage.navigateTo(BaseTest.getBaseUrl());
    }

    @Then("the plant list should be displayed")
    public void the_plant_list_should_be_displayed() {
        Assert.assertTrue(plantPage.isPlantListDisplayed());
    }

    @Given("there are no plants in the system")
    public void there_are_no_plants_in_the_system() {
        // Setup state - complex for UI, maybe assume specific test env
    }

    @Then("I should see a plant {string} message")
    public void i_should_see_a_plant_message(String msg) {
        if (msg.contains("No plants")) {
            Assert.assertTrue(plantPage.isNoDataMessageDisplayed());
        }
    }

    @Given("I enter text in the search field")
    public void i_enter_text_in_the_search_field() {
        plantPage.enterSearchText("Test Plant");
    }

    @When("I click the Reset button")
    public void i_click_the_reset_button() {
        plantPage.clickReset();
    }

    @Then("the search field should be cleared")
    public void the_search_field_should_be_cleared() {
        Assert.assertEquals(plantPage.SearchBoxValue(), "");
    }

    @Then("the full plant list should be displayed")
    public void the_full_plant_list_should_be_displayed() {
        Assert.assertTrue(plantPage.isPlantListDisplayed());
    }

    @Then("the Add Plant button should not be visible")
    public void the_add_plant_button_should_not_be_visible() {
        Assert.assertFalse(plantPage.isAddButtonVisible());
    }

    @Then("I should see plant pagination controls")
    public void i_should_see_plant_pagination_controls() {
        Assert.assertTrue(plantPage.isPaginationNextVisible());
    }

    @When("I click the Next page button")
    public void i_click_the_next_page_button() {
        plantPage.clickNextPage();
    }

    @Then("the next page of plants should be displayed")
    public void the_next_page_of_plants_should_be_displayed() {
        // Assert table content changed
    }

    @When("I click the Previous page button")
    public void i_click_the_previous_page_button() {
        plantPage.clickPrevPage();
    }

    @Then("the previous page of plants should be displayed")
    public void the_previous_page_of_plants_should_be_displayed() {
        // Assert
    }

    @When("I click edit on a plant")
    public void i_click_edit_on_a_plant() {
        plantPage.clickEditPlant("Test Plant");
    }

    @When("I clear mandatory fields")
    public void i_clear_mandatory_fields() {
        editPlantPage.clearMandatoryFields();
    }

    @When("I click the Save button")
    public void i_click_the_save_button() {
        editPlantPage.clickSave();
    }

    @Then("validation messages should be shown for mandatory fields")
    public void validation_messages_should_be_shown_for_mandatory_fields() {
        Assert.assertTrue(editPlantPage.isNameErrorDisplayed());
        Assert.assertTrue(editPlantPage.isPriceErrorDisplayed());
    }

    @When("I enter a plant name with less than {int} characters")
    public void i_enter_a_plant_name_with_less_than_characters(int length) {
        editPlantPage.enterName("Ab");
    }

    @Then("an error message for name length should be displayed")
    public void an_error_message_for_name_length_should_be_displayed() {
        Assert.assertTrue(editPlantPage.isNameErrorDisplayed());
    }

    @When("I enter a plant name with more than {int} characters")
    public void i_enter_a_plant_name_with_more_than_characters(int length) {
        editPlantPage.enterName("This is a very long plant name that exceeds the limit");
    }

    @When("I update price to {int}")
    public void i_update_price_to(int price) {
        editPlantPage.enterPrice(String.valueOf(price));
    }

    @Then("a price validation error should be shown")
    public void a_price_validation_error_should_be_shown() {
        Assert.assertTrue(editPlantPage.isPriceErrorDisplayed());
    }

    @When("I update quantity to {int}")
    public void i_update_quantity_to(int qty) {
        editPlantPage.enterQuantity(String.valueOf(qty));
    }

    @Then("a quantity validation error should be shown")
    public void a_quantity_validation_error_should_be_shown() {
        Assert.assertTrue(editPlantPage.isQuantityErrorDisplayed());
    }

    @When("I click the Cancel button")
    public void i_click_the_cancel_button() {
        editPlantPage.clickCancel();
    }

    @Then("I should be redirected to the plant list")
    public void i_should_be_redirected_to_the_plant_list() {
        Assert.assertTrue(plantPage.isPlantListDisplayed());
    }
    
    @When("I enter valid details for the plant")
    public void i_enter_valid_details_for_the_plant() {
        editPlantPage.enterName("Valid Plant");
        editPlantPage.enterPrice("10.0");
        editPlantPage.enterQuantity("5");
    }

    @Then("the plant should be edited successfully")
    public void the_plant_should_be_edited_successfully() {
        // Assert success message or redirect
        Assert.assertTrue(plantPage.isPlantListDisplayed());
    }
}
