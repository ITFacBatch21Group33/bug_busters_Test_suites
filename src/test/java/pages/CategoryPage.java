package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryPage {
    private WebDriver driver;

    // Locators
    private By searchBox = By.name("name");
    private By searchButton = By.cssSelector("button[type='submit']");
    private By nextButton = By.xpath("//a[contains(text(),'Next')]");
    private By prevButton = By.xpath("//a[contains(text(),'Previous')]");
    private By categoryRows = By.xpath("//table/tbody/tr");
    // Broader locator for Empty State message
    private By noDataMessage = By.xpath(
            "//*[contains(text(),'No category') or contains(text(),'not found') or contains(text(),'No results')]");
    private By parentFilterDropdown = By.name("parentId");
    private By addCategoryBtn = By.linkText("Add A Category");

    public CategoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateTo() {
        String baseUrl = utils.ConfigLoader.getProperty("base.url");
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/ui/categories");
    }

    public void searchFor(String query) {
        // Increased timeout to 20s to handle potential slow loads
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(20));
        WebElement box = wait
                .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(searchBox));
        box.clear();
        box.sendKeys(query);
        driver.findElement(searchButton).click();
    }

    public boolean isPaginationVisible() {
        try {
            return driver.findElement(nextButton).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public void clickNextPage() {
        WebElement element = driver.findElement(nextButton);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public void clickPrevPage() {
        WebElement element = driver.findElement(prevButton);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public List<String> getCategoryNames() {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(20));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(categoryRows));

        return driver.findElements(By.xpath("//table/tbody/tr/td[2]")).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public boolean isNoCategoryMessageDisplayed() {
        return driver.findElements(noDataMessage).size() > 0 && driver.findElement(noDataMessage).isDisplayed();
    }

    public boolean isAddCategoryButtonVisible() {
        return driver.findElements(addCategoryBtn).size() > 0 && driver.findElement(addCategoryBtn).isDisplayed();
    }

    public void filterByParent(String parentName) {
        Select select = new Select(driver.findElement(parentFilterDropdown));
        select.selectByVisibleText(parentName);
    }
}
