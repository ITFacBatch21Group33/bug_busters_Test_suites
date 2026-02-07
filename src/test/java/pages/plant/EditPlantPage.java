package pages.plant;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class EditPlantPage {
    private WebDriver driver;

    // Primary locators using IDs
    // Primary locators - Updated to match working fallbacks
    private By nameInput = By.name("name");
    private By priceInput = By.name("price");
    private By quantityInput = By.name("quantity");
    private By categorySelect = By.name("categoryId");
    
    // Save button - Updated to match working fallback
    private By saveButton = By.xpath("//button[contains(text(), 'Save') or contains(@value, 'Save') or @type='submit']");
    // Cancel might be a link (a tag) or a button
    private By cancelButton = By.xpath("//*[contains(text(), 'Cancel') or @value='Cancel' or @title='Cancel']");
    // Removed unused ID-based locators that were failing
    
    private By nameError = By.id("error-name");
    private By priceError = By.id("error-price");
    private By quantityError = By.id("error-quantity");
    
    // Alternative error locators
    // Alternative error locators - Updated based on actual HTML
    private By nameErrorAlt = By.xpath("//div[contains(@class, 'text-danger') and contains(., 'Plant name is required')]");
    private By nameLengthErrorAlt = By.xpath("//div[contains(@class, 'text-danger') and contains(., 'Plant name must be between')]");
    private By priceErrorAlt = By.xpath("//div[contains(@class, 'text-danger') and contains(., 'Price is required')]");
    // Range/Value validation for price
    private By priceRangeErrorAlt = By.xpath("//div[contains(@class, 'text-danger') and (contains(., 'must be') or contains(., 'greater than'))]");
    private By quantityErrorAlt = By.xpath("//div[contains(@class, 'text-danger') and contains(., 'Quantity is required')]");
    // Range validation typically mentions "must be" or "greater than"
    private By quantityRangeErrorAlt = By.xpath("//div[contains(@class, 'text-danger') and (contains(., 'must be') or contains(., 'greater than'))]");

    public EditPlantPage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Helper method for simple visibility wait
     */
    private WebElement waitForElement(WebDriverWait wait, By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Helper method for clickable elements
     */
    private WebElement waitForClickable(WebDriverWait wait, By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void clearMandatoryFields() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        waitForElement(wait, nameInput).clear();
        waitForElement(wait, priceInput).clear();
        waitForElement(wait, quantityInput).clear();
    }

    public void clickSave() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        waitForClickable(wait, saveButton).click();
        
        // Wait for page to redirect back to plant list
        try {
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(By.tagName("table")));
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("DEBUG: Warning - page did not show table after save. Error: " + e.getMessage());
        }
    }

    public void clickCancel() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        waitForClickable(wait, cancelButton).click();
    }

    public void enterName(String name) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = waitForElement(wait, nameInput);
        element.clear();
        element.sendKeys(name);
    }

    public void enterPrice(String price) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = waitForElement(wait, priceInput);
        element.clear();
        element.sendKeys(price);
    }

    public void enterQuantity(String qty) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = waitForElement(wait, quantityInput);
        element.clear();
        element.sendKeys(qty);
    }

    public void selectCategory(String visibleText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = waitForElement(wait, categorySelect);
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(element);
        try {
            select.selectByVisibleText(visibleText);
        } catch (org.openqa.selenium.NoSuchElementException e) {
             // Fallback: select by index 1
             if (select.getOptions().size() > 1) {
                 select.selectByIndex(1);
             } else {
                 throw e;
             }
        }
    }

    public void selectAnyCategory() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = waitForElement(wait, categorySelect);
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(element);
        if (!select.getOptions().isEmpty()) {
             // Try index 1 first (assuming 0 might be placeholder), otherwise index 0
             if (select.getOptions().size() > 1) {
                 select.selectByIndex(1);
             } else {
                 select.selectByIndex(0);
             }
        }
    }

    public void selectFirstCategory() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = waitForElement(wait, categorySelect);
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(element);
        // Usually index 0 is "Select Category" or similar, so we try index 1 if available, else index 0
        if (select.getOptions().size() > 1) {
            select.selectByIndex(1);
        } else if (!select.getOptions().isEmpty()) {
            select.selectByIndex(0);
        }
    }
    public boolean isNameErrorDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(nameError)).isDisplayed();
            } catch (org.openqa.selenium.TimeoutException e) {
                // Check for either "Required" OR "Length" error
                boolean requiredError = !driver.findElements(nameErrorAlt).isEmpty() && driver.findElement(nameErrorAlt).isDisplayed();
                boolean lengthError = !driver.findElements(nameLengthErrorAlt).isEmpty() && driver.findElement(nameLengthErrorAlt).isDisplayed();
                return requiredError || lengthError;
            }
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public boolean isPriceErrorDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(priceError)).isDisplayed();
            } catch (org.openqa.selenium.TimeoutException e) {
                 boolean requiredError = !driver.findElements(priceErrorAlt).isEmpty() && driver.findElement(priceErrorAlt).isDisplayed();
                 boolean rangeError = !driver.findElements(priceRangeErrorAlt).isEmpty() && driver.findElement(priceRangeErrorAlt).isDisplayed();
                 return requiredError || rangeError;
            }
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public boolean isQuantityErrorDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(quantityError)).isDisplayed();
            } catch (org.openqa.selenium.TimeoutException e) {
                 boolean requiredError = !driver.findElements(quantityErrorAlt).isEmpty() && driver.findElement(quantityErrorAlt).isDisplayed();
                 boolean rangeError = !driver.findElements(quantityRangeErrorAlt).isEmpty() && driver.findElement(quantityRangeErrorAlt).isDisplayed();
                 // Fallback: any text-danger with "Quantity"
                 boolean genericQuantityError = !driver.findElements(By.xpath("//*[contains(@class, 'text-danger') and contains(., 'Quantity')]")).isEmpty() 
                                             && driver.findElement(By.xpath("//*[contains(@class, 'text-danger') and contains(., 'Quantity')]")).isDisplayed();
                 return requiredError || rangeError || genericQuantityError;
            }
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }
}
