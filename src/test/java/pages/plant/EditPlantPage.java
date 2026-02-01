package pages.plant;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EditPlantPage {
    private WebDriver driver;

    private By nameInput = By.id("plant-name");
    private By priceInput = By.id("plant-price");
    private By quantityInput = By.id("plant-quantity");
    private By saveButton = By.id("save-btn");
    private By cancelButton = By.id("cancel-btn");
    
    private By nameError = By.id("error-name");
    private By priceError = By.id("error-price");
    private By quantityError = By.id("error-quantity");

    public EditPlantPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clearMandatoryFields() {
        driver.findElement(nameInput).clear();
        driver.findElement(priceInput).clear();
        driver.findElement(quantityInput).clear();
    }

    public void clickSave() {
        driver.findElement(saveButton).click();
    }

    public void clickCancel() {
        driver.findElement(cancelButton).click();
    }

    public void enterName(String name) {
        driver.findElement(nameInput).clear();
        driver.findElement(nameInput).sendKeys(name);
    }

    public void enterPrice(String price) {
        driver.findElement(priceInput).clear(); // inputs often text
        driver.findElement(priceInput).sendKeys(price);
    }

    public void enterQuantity(String qty) {
        driver.findElement(quantityInput).clear();
        driver.findElement(quantityInput).sendKeys(qty);
    }

    public boolean isNameErrorDisplayed() {
        return driver.findElement(nameError).isDisplayed();
    }

    public boolean isPriceErrorDisplayed() {
        return driver.findElement(priceError).isDisplayed();
    }

    public boolean isQuantityErrorDisplayed() {
        return driver.findElement(quantityError).isDisplayed();
    }
}
