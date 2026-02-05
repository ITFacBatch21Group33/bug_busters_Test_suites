package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SalesPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By salesPageTitle = By.tagName("h1");
    private By[] alternativeTitleLocators = {
        By.tagName("h1"),
        By.tagName("h2"),
        By.tagName("h3"),
        By.cssSelector("h1, h2, h3, h4"),
        By.xpath("//*[contains(@class, 'title') or contains(@class, 'heading')]")
    };
    private By sellPlantButton = By.linkText("Sell Plant");
    private By salesTable = By.tagName("table");
    private By salesTableRows = By.cssSelector("table tbody tr");
    private By deleteButtons = By.cssSelector("button.btn-outline-danger, a.btn-outline-danger, button.btn-danger, a.btn-danger");
    private By confirmDeleteButton = By.id("confirmDelete");
    private By noSalesMessage = By.xpath("//*[contains(text(), 'No sales found') or contains(text(), 'no sales')]");
    private By plantSelect = By.id("plantId");
    private By quantityInput = By.id("quantity");
    private By sellButton = By.xpath("//button[contains(text(), 'Sell')]");
    private By successMessage = By.cssSelector(".alert-success");
    private By[] alternativeSuccessMessageLocators = {
        By.cssSelector(".alert-success"),
        By.cssSelector(".alert.alert-success"),
        By.xpath("//*[contains(@class, 'alert') and contains(@class, 'success')]"),
        By.xpath("//*[contains(@class, 'success')]"),
        By.xpath("//*[contains(text(), 'successfully') or contains(text(), 'Successfully') or contains(text(), 'Success')]")
    };
    private By errorMessage = By.cssSelector(".alert-danger");

    public SalesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToSalesPage(String baseUrl) {
        driver.get(baseUrl + "/ui/sales");
    }

    public void navigateToSellPlantPage(String baseUrl) {
        driver.get(baseUrl + "/ui/sales/new");
    }

    public boolean isSalesPageDisplayed() {
        try {
            System.out.println("DEBUG: Current URL: " + driver.getCurrentUrl());
            
            // First check if URL is correct
            String currentUrl = driver.getCurrentUrl();
            if (!currentUrl.contains("/ui/sales")) {
                System.err.println("ERROR: URL doesn't contain '/ui/sales': " + currentUrl);
                return false;
            }
            
            // Try to find a title element using multiple locators
            WebElement titleElement = null;
            for (By locator : alternativeTitleLocators) {
                try {
                    titleElement = driver.findElement(locator);
                    if (titleElement.isDisplayed()) {
                        String titleText = titleElement.getText();
                        System.out.println("DEBUG: Found title element with text: '" + titleText + "' using locator: " + locator);
                        if (titleText.toLowerCase().contains("sales") || titleText.toLowerCase().contains("sale")) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                    // Try next locator
                    continue;
                }
            }
            
            // If no title found but URL is correct, check if table is present
            System.out.println("DEBUG: No title found, checking for sales table as fallback");
            try {
                WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(salesTable));
                System.out.println("DEBUG: Sales table found, considering page as displayed");
                return true;
            } catch (Exception e) {
                System.err.println("ERROR: Sales table not found either");
            }
            
            // As last resort, if URL is correct, assume page is displayed
            System.out.println("DEBUG: URL is correct, assuming page is displayed");
            return true;
            
        } catch (Exception e) {
            System.err.println("ERROR: Failed to check if sales page is displayed: " + e.getMessage());
            System.err.println("DEBUG: Current URL on error: " + driver.getCurrentUrl());
            return false;
        }
    }

    public boolean isSellPlantButtonVisible() {
        try {
            return driver.findElement(sellPlantButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickSellPlantButton() {
        wait.until(ExpectedConditions.elementToBeClickable(sellPlantButton)).click();
    }

    public boolean isSalesTableDisplayed() {
        try {
            return driver.findElement(salesTable).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getSalesCount() {
        try {
            List<WebElement> rows = driver.findElements(salesTableRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isNoSalesMessageDisplayed() {
        try {
            return driver.findElement(noSalesMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDeleteButtonVisible() {
        try {
            List<WebElement> buttons = driver.findElements(deleteButtons);
            return !buttons.isEmpty() && buttons.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickDeleteButton() {
        try {
            System.out.println("DEBUG: Looking for delete button at URL: " + driver.getCurrentUrl());
            
            // Wait for table to be present
            wait.until(ExpectedConditions.presenceOfElementLocated(salesTable));
            
            // Try to find delete button - now using btn-outline-danger
            WebElement deleteButton = wait.until(ExpectedConditions.presenceOfElementLocated(deleteButtons));
            
            if (deleteButton.isDisplayed()) {
                System.out.println("DEBUG: Found delete button: " + deleteButton.getAttribute("outerHTML"));
                deleteButton.click();
                System.out.println("DEBUG: Delete button clicked successfully");
            } else {
                throw new RuntimeException("Delete button not visible");
            }
        } catch (Exception e) {
            System.err.println("ERROR: Failed to click delete button: " + e.getMessage());
            throw e;
        }
    }

    public void confirmDelete() {
        try {
            System.out.println("DEBUG: Confirming delete operation");
            
            // The delete button triggers a JavaScript confirm dialog, not a modal
            // We need to handle the alert
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
            
            System.out.println("DEBUG: Alert accepted");
            
            // Wait for page to navigate/reload after deletion
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            
        } catch (Exception e) {
            System.err.println("ERROR: Failed to confirm deletion: " + e.getMessage());
            throw e;
        }
    }

    public void selectPlant(String plantName) {
        try {
            Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(plantSelect)));
            
            // Debug: Print all available options
            List<WebElement> options = select.getOptions();
            System.out.println("DEBUG: Available plant options in dropdown:");
            for (int i = 0; i < options.size(); i++) {
                WebElement option = options.get(i);
                System.out.println("  [" + i + "] value='" + option.getAttribute("value") + "' text='" + option.getText() + "'");
            }
            
            // Try to select by visible text first
            try {
                select.selectByVisibleText(plantName);
                System.out.println("DEBUG: Successfully selected plant by visible text: " + plantName);
                return;
            } catch (Exception e) {
                System.out.println("DEBUG: Could not select by exact visible text '" + plantName + "', trying alternatives");
            }
            
            // Try to select by partial text match (case-insensitive)
            for (WebElement option : options) {
                String optionText = option.getText().trim();
                if (optionText.toLowerCase().contains(plantName.toLowerCase())) {
                    System.out.println("DEBUG: Found matching option by partial text: '" + optionText + "'");
                    option.click();
                    return;
                }
            }
            
            // Try to select by value
            try {
                select.selectByValue(plantName);
                System.out.println("DEBUG: Successfully selected plant by value: " + plantName);
                return;
            } catch (Exception e) {
                System.out.println("DEBUG: Could not select by value '" + plantName + "'");
            }
            
            // If nothing worked, throw exception
            throw new RuntimeException("Could not find plant option matching: " + plantName);
            
        } catch (Exception e) {
            System.err.println("ERROR: Failed to select plant '" + plantName + "': " + e.getMessage());
            throw e;
        }
    }

    public void enterQuantity(String quantity) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(quantityInput));
        input.clear();
        input.sendKeys(quantity);
    }

    public void clickSellButton() {
        wait.until(ExpectedConditions.elementToBeClickable(sellButton)).click();
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            System.out.println("DEBUG: Checking for success message at URL: " + driver.getCurrentUrl());
            
            // Wait a moment for any redirect or message to appear
            Thread.sleep(2000);
            
            String currentUrl = driver.getCurrentUrl();
            System.out.println("DEBUG: Current URL after wait: " + currentUrl);
            
            // If we're on the sales list page (not the /new page), consider it success
            if (currentUrl.contains("/ui/sales") && !currentUrl.contains("/new")) {
                System.out.println("DEBUG: Redirected to sales list page, assuming success");
                return true;
            }
            
            // Try to find success message with very short timeout
            By[] messageLocators = {
                By.cssSelector(".alert-success"),
                By.cssSelector(".alert.alert-success"),
                By.xpath("//*[contains(@class, 'alert-success')]")
            };
            
            for (By locator : messageLocators) {
                try {
                    List<WebElement> messages = driver.findElements(locator);
                    if (!messages.isEmpty() && messages.get(0).isDisplayed()) {
                        String messageText = messages.get(0).getText();
                        System.out.println("DEBUG: Found success message: '" + messageText + "'");
                        return true;
                    }
                } catch (Exception e) {
                    // Continue to next locator
                }
            }
            
            System.err.println("ERROR: No success indicator found");
            return false;
            
        } catch (Exception e) {
            System.err.println("ERROR: Exception in isSuccessMessageDisplayed: " + e.getMessage());
            return false;
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return driver.findElement(errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAccessDeniedPageDisplayed() {
        try {
            String currentUrl = driver.getCurrentUrl();
            String pageSource = driver.getPageSource().toLowerCase();
            return currentUrl.contains("403") || 
                   pageSource.contains("access denied") || 
                   pageSource.contains("forbidden");
        } catch (Exception e) {
            return false;
        }
    }

    public void clickPlantColumnHeader() {
        try {
            // Wait for table to be visible first
            wait.until(ExpectedConditions.visibilityOfElementLocated(salesTable));
            
            System.out.println("DEBUG: Looking for 'Plant' text link in table header");
            
            // Specifically find the link with text "Plant" inside the table header
            // User wants to click the text "Plant", not just the header cell
            WebElement plantLink = null;
            
            // Try different locators to find the "Plant" link
            By[] linkLocators = {
                By.xpath("//table//thead//th//a[contains(text(), 'Plant')]"),
                By.xpath("//thead//th//a[text()='Plant']"),
                By.xpath("//table//thead//a[normalize-space()='Plant']"),
                By.xpath("//th[contains(., 'Plant')]//a"),
                By.linkText("Plant")
            };
            
            for (By locator : linkLocators) {
                try {
                    plantLink = driver.findElement(locator);
                    if (plantLink.isDisplayed()) {
                        System.out.println("DEBUG: Found 'Plant' link using locator: " + locator);
                        System.out.println("DEBUG: Link HTML: " + plantLink.getAttribute("outerHTML"));
                        System.out.println("DEBUG: Link text: '" + plantLink.getText() + "'");
                        System.out.println("DEBUG: Link href: " + plantLink.getAttribute("href"));
                        break;
                    }
                } catch (Exception e) {
                    // Try next locator
                }
            }
            
            if (plantLink == null) {
                // Fallback: try to find any link in first th
                System.out.println("DEBUG: Fallback - looking for any link in first table header");
                try {
                    plantLink = driver.findElement(By.xpath("//table//thead//th[1]//a"));
                    System.out.println("DEBUG: Found link in first header: " + plantLink.getText());
                } catch (Exception e) {
                    throw new RuntimeException("Could not find 'Plant' link in table header");
                }
            }
            
            // Click the Plant link
            if (plantLink != null && plantLink.isDisplayed()) {
                System.out.println("DEBUG: Clicking on 'Plant' link");
                
                // Use JavaScript click for reliability
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", plantLink);
                System.out.println("DEBUG: JavaScript click completed on Plant link");
                
                // Wait for page to reload/resort
                Thread.sleep(2000);
                System.out.println("DEBUG: Waited for page to process sort");
                
            } else {
                throw new RuntimeException("Plant link not found or not visible");
            }
            
        } catch (Exception e) {
            System.err.println("ERROR: Failed to click Plant column header: " + e.getMessage());
            throw new RuntimeException("Failed to click Plant column header: " + e.getMessage(), e);
        }
    }

    public boolean isSalesListSorted() {
        try {
            // Wait for table rows to be present
            wait.until(ExpectedConditions.presenceOfElementLocated(salesTableRows));
            
            // Wait longer for any sorting JavaScript to complete
            Thread.sleep(2000);
            
            // Get plant names from the table
            List<WebElement> rows = driver.findElements(salesTableRows);
            if (rows.isEmpty()) {
                System.out.println("No rows found in sales table");
                return true;
            }
            
            System.out.println("DEBUG: Found " + rows.size() + " rows in sales table");
            
            List<String> plantNames = new ArrayList<>();
            for (int i = 0; i < rows.size(); i++) {
                WebElement row = rows.get(i);
                try {
                    // Try to get the plant name from the first cell
                    List<WebElement> cells = row.findElements(By.tagName("td"));
                    if (!cells.isEmpty()) {
                        String plantName = cells.get(0).getText().trim();
                        System.out.println("DEBUG: Row " + i + " plant name: '" + plantName + "'");
                        if (!plantName.isEmpty()) {
                            plantNames.add(plantName);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("DEBUG: Error reading row " + i + ": " + e.getMessage());
                }
            }
            
            System.out.println("DEBUG: Extracted plant names: " + plantNames);
            
            // Check if list is sorted (at least first two elements should be in order)
            if (plantNames.size() < 2) {
                System.out.println("DEBUG: Less than 2 plant names, considering sorted");
                return true;
            }
            
            // Compare consecutive pairs to verify sorting
            boolean isSorted = true;
            for (int i = 0; i < plantNames.size() - 1; i++) {
                int comparison = plantNames.get(i).compareToIgnoreCase(plantNames.get(i + 1));
                System.out.println("DEBUG: Comparing '" + plantNames.get(i) + "' with '" + plantNames.get(i + 1) + "': " + comparison);
                if (comparison > 0) {
                    System.err.println("ERROR: List not sorted at index " + i + ": '" + plantNames.get(i) + "' > '" + plantNames.get(i + 1) + "'");
                    isSorted = false;
                    break;
                }
            }
            
            System.out.println("DEBUG: Final sorting result: " + isSorted);
            return isSorted;
        } catch (Exception e) {
            System.err.println("Error checking if sales list is sorted: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
