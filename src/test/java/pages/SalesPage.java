package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SalesPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By salesPageTitle = By.tagName("h1");
    private By sellPlantButton = By.linkText("Sell Plant");
    private By salesTable = By.tagName("table");
    private By salesTableRows = By.cssSelector("table tbody tr");
    private By deleteButtons = By.cssSelector("button.btn-danger, a.btn-danger");
    private By confirmDeleteButton = By.id("confirmDelete");
    private By noSalesMessage = By.xpath("//*[contains(text(), 'No sales found') or contains(text(), 'no sales')]");
    private By plantColumnHeader = By.xpath("//th[contains(text(), 'Plant')]");
    private By plantSelect = By.id("plantId");
    private By quantityInput = By.id("quantity");
    private By sellButton = By.xpath("//button[contains(text(), 'Sell')]");
    private By successMessage = By.cssSelector(".alert-success");
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
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(salesPageTitle));
            return title.getText().toLowerCase().contains("sales");
        } catch (Exception e) {
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
        wait.until(ExpectedConditions.elementToBeClickable(deleteButtons)).click();
    }

    public void confirmDelete() {
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteButton)).click();
    }

    public void selectPlant(String plantName) {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(plantSelect)));
        select.selectByVisibleText(plantName);
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
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
        } catch (Exception e) {
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
        wait.until(ExpectedConditions.elementToBeClickable(plantColumnHeader)).click();
    }

    public boolean isSalesListSorted() {
        try {
            List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr td:first-child"));
            if (rows.size() < 2) return true;
            
            String firstPlant = rows.get(0).getText();
            String secondPlant = rows.get(1).getText();
            return firstPlant.compareTo(secondPlant) <= 0;
        } catch (Exception e) {
            return false;
        }
    }
}
