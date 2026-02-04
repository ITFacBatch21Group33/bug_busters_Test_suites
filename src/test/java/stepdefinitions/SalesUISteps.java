package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.testng.Assert;
import pages.SalesPage;
import utils.BaseTest;
import utils.ConfigLoader;

public class SalesUISteps extends BaseTest {
    private SalesPage salesPage;

    @Given("a plant with name {string} exists")
    public void a_plant_with_name_exists(String plantName) {
        // Plant should exist in database for test to work
    }

    @Given("a sale record exists")
    public void a_sale_record_exists() {
        // At least one sale record should exist
    }

    @Given("multiple sales exist")
    public void multiple_sales_exist() {
        // Multiple sale records should exist for sorting test
    }

    @Given("no sales exist in the system")
    public void no_sales_exist_in_the_system() {
        // Database should have no sales for this test
    }

    @When("I navigate to the sales page")
    public void i_navigate_to_the_sales_page() {
        salesPage = new SalesPage(driver);
        salesPage.navigateToSalesPage(ConfigLoader.getProperty("base.url"));
    }

    @When("I navigate to the sell plant page directly")
    public void i_navigate_to_the_sell_plant_page_directly() {
        salesPage = new SalesPage(driver);
        salesPage.navigateToSellPlantPage(ConfigLoader.getProperty("base.url"));
    }

    @When("I click the Sell Plant button")
    public void i_click_the_sell_plant_button() {
        salesPage.clickSellPlantButton();
    }

    @When("I select plant {string}")
    public void i_select_plant(String plantName) {
        salesPage.selectPlant(plantName);
    }

    @When("I enter quantity {string}")
    public void i_enter_quantity(String quantity) {
        salesPage.enterQuantity(quantity);
    }

    @When("I click the Sell button")
    public void i_click_the_sell_button() {
        salesPage.clickSellButton();
    }

    @When("I click the delete button for a sale")
    public void i_click_the_delete_button_for_a_sale() {
        salesPage.clickDeleteButton();
    }

    @When("I confirm the deletion")
    public void i_confirm_the_deletion() {
        salesPage.confirmDelete();
    }

    @When("I click on the Plant column header")
    public void i_click_on_the_plant_column_header() {
        salesPage.clickPlantColumnHeader();
    }

    @Then("I should see the sales list page")
    public void i_should_see_the_sales_list_page() {
        Assert.assertTrue(salesPage.isSalesPageDisplayed(), "Sales page should be displayed");
    }

    @Then("I should see sales records")
    public void i_should_see_sales_records() {
        Assert.assertTrue(salesPage.isSalesTableDisplayed(), "Sales table should be displayed");
        Assert.assertTrue(salesPage.getSalesCount() > 0, "Should have at least one sale record");
    }

    @Then("I should see the Sell Plant button")
    public void i_should_see_the_sell_plant_button() {
        Assert.assertTrue(salesPage.isSellPlantButtonVisible(), "Sell Plant button should be visible");
    }

    @Then("I should not see the Sell Plant button")
    public void i_should_not_see_the_sell_plant_button() {
        Assert.assertFalse(salesPage.isSellPlantButtonVisible(), "Sell Plant button should not be visible for User");
    }

    @Then("I should see the Create New Sell Plant page")
    public void i_should_see_the_create_new_sell_plant_page() {
        Assert.assertTrue(salesPage.isSalesPageDisplayed(), "Create New Sell Plant page should be displayed");
    }

    @Then("the sale should be created successfully")
    public void the_sale_should_be_created_successfully() {
        Assert.assertTrue(salesPage.isSuccessMessageDisplayed(), "Success message should be displayed after creating sale");
    }

    @Then("the sale should be deleted successfully")
    public void the_sale_should_be_deleted_successfully() {
        Assert.assertTrue(salesPage.isSuccessMessageDisplayed(), "Success message should be displayed after deleting sale");
    }

    @Then("I should not see the delete option")
    public void i_should_not_see_the_delete_option() {
        Assert.assertFalse(salesPage.isDeleteButtonVisible(), "Delete button should not be visible for User");
    }

    @Then("I should see a no sales found message")
    public void i_should_see_a_no_sales_found_message() {
        Assert.assertTrue(salesPage.isNoSalesMessageDisplayed(), "No sales found message should be displayed");
    }

    @Then("I should see an access denied page")
    public void i_should_see_an_access_denied_page() {
        Assert.assertTrue(salesPage.isAccessDeniedPageDisplayed(), "Access denied page should be displayed");
    }

    @Then("the sales list should be sorted by plant name")
    public void the_sales_list_should_be_sorted_by_plant_name() {
        Assert.assertTrue(salesPage.isSalesListSorted(), "Sales list should be sorted by plant name");
    }
}
