package pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class CategoryPage {
    private WebDriver driver;

    // Locators
    private By searchBox = By.name("name");
    private By searchButton = By.cssSelector("button[type='submit']");
    private By nextButton = By.xpath("//a[contains(text(),'Next')]");
    private By prevButton = By.xpath("//a[contains(text(),'Previous')]");
    private By categoryRows = By.xpath("//table/tbody/tr");
    // Broader locator for Empty State message using dot for nested text matching
    private By noDataMessage = By
            .xpath("//*[contains(.,'No category') or contains(.,'not found') or contains(.,'No results')]");
    private By parentFilterDropdown = By.name("parentId");
    private By addCategoryBtn = By.linkText("Add A Category");

    // Add Category page locators (strict)
    private By addCategoryNameInput = By.cssSelector("form input[name='name']");
    private By addCategoryParentDropdown = By.cssSelector("form select[name='parentId']");
    private By addCategorySaveButton = By.xpath("//button[normalize-space()='Save']");

    private By categoryNameRequiredError = By.xpath("//*[normalize-space()='Category name is required']");

    public CategoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateTo() {
        String baseUrl = utils.ConfigLoader.getProperty("base.url");
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/ui/categories");
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
        try {
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                    java.time.Duration.ofSeconds(10));
            WebElement element = wait
                    .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(noDataMessage));
            return element.isDisplayed();
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public boolean isAddCategoryButtonVisible() {
        return driver.findElements(addCategoryBtn).size() > 0 && driver.findElement(addCategoryBtn).isDisplayed();
    }

    public void filterByParent(String parentName) {
        Select select = new Select(driver.findElement(parentFilterDropdown));
        select.selectByVisibleText(parentName);
    }

    // Create main category
    public void navigateToAddCategory() {
        String baseUrl = utils.ConfigLoader.getProperty("base.url");
        if (baseUrl.endsWith("/")) baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        driver.get(baseUrl + "/ui/categories/add");
    }

    public void enterCategoryName(String name) {
        org.openqa.selenium.support.ui.WebDriverWait wait =
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(15));
        WebElement input = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(addCategoryNameInput)
        );
        input.clear();
        input.sendKeys(name);
    }

    public void selectParentCategory(String visibleText) {
        org.openqa.selenium.support.ui.WebDriverWait wait =
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(15));
        WebElement dropdown = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(addCategoryParentDropdown)
        );
        new Select(dropdown).selectByVisibleText(visibleText);
    }

    public void clickSaveOnAddCategory() {
        org.openqa.selenium.support.ui.WebDriverWait wait =
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(15));
        WebElement save = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(addCategorySaveButton)
        );
        save.click();
    }

    public void waitForCategoriesListPage() {
        org.openqa.selenium.support.ui.WebDriverWait wait =
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(15));
        wait.until(d -> d.getCurrentUrl().contains("/ui/categories") && !d.getCurrentUrl().contains("/add"));
    }

    // Validation empty category name error
    public void clearCategoryName() {
        org.openqa.selenium.support.ui.WebDriverWait wait =
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        WebElement input = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(addCategoryNameInput)
        );
        input.clear();
    }

    public boolean isOnAddCategoryPage() {
        return driver.getCurrentUrl() != null && driver.getCurrentUrl().contains("/ui/categories/add");
    }

    public void waitForAddCategoryPage() {
        org.openqa.selenium.support.ui.WebDriverWait wait =
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/ui/categories/add"));
    }

    public boolean isValidationMessageDisplayed(String message) {
        try {
            By by = By.xpath("//*[contains(normalize-space(.), " + xpathLiteral(message) + ")]");
            org.openqa.selenium.support.ui.WebDriverWait wait =
                    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));
            return wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(by)).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    private static String xpathLiteral(String text) {
        if (text.contains("'") && text.contains("\"")) {
            String[] parts = text.split("\"", -1);
            StringBuilder sb = new StringBuilder("concat(");
            for (int i = 0; i < parts.length; i++) {
                if (i > 0) sb.append(", '\"', ");
                sb.append("'").append(parts[i]).append("'");
            }
            sb.append(")");
            return sb.toString();
        }
        if (text.contains("\"")) return "'" + text + "'";
        return "\"" + text + "\"";
    }

    // Check cancel add category
    private By addCategoryCancelButton =
        By.xpath("//button[normalize-space()='Cancel'] | //a[normalize-space()='Cancel']");
    
    public void clickCancelOnAddCategory() {
        org.openqa.selenium.support.ui.WebDriverWait wait =
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(15));
        WebElement cancel = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(addCategoryCancelButton)
        );
        cancel.click();
    }

    public boolean isCategoryPresentInResults(String categoryName) {
        String cellXpath = "//table/tbody/tr/td[2][normalize-space()=" + xpathLiteral(categoryName) + "]";
        return driver.findElements(By.xpath(cellXpath)).size() > 0;
    }

    public void waitForResultsOrEmptyState() {
        org.openqa.selenium.support.ui.WebDriverWait wait =
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(15));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.or(
                org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(categoryRows),
                org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(noDataMessage)
        ));
    }
        
}