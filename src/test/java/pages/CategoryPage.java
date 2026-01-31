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
    private By searchBox = By.id("category-search");
    private By searchButton = By.id("search-btn");
    private By nextButton = By.id("pagination-next");
    private By prevButton = By.id("pagination-prev");
    private By categoryTable = By.id("category-table");
    private By categoryRows = By.xpath("//table[@id='category-table']/tbody/tr");
    private By noDataMessage = By.id("no-data-msg");
    private By parentFilterDropdown = By.id("parent-filter");
    private By addCategoryBtn = By.id("add-category-btn");

    public CategoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateTo() {
        // Assuming the URL, actual implementation depends on routing
        driver.get("http://localhost:8080/ui/categories");
    }

    public void searchFor(String query) {
        driver.findElement(searchBox).clear();
        driver.findElement(searchBox).sendKeys(query);
        driver.findElement(searchButton).click();
    }

    public boolean isPaginationVisible() {
        return driver.findElement(nextButton).isDisplayed();
    }

    public void clickNextPage() {
        driver.findElement(nextButton).click();
    }

    public void clickPrevPage() {
        driver.findElement(prevButton).click();
    }

    public List<String> getCategoryNames() {
        return driver.findElements(categoryRows).stream()
                .map(row -> row.findElement(By.xpath("td[2]")).getText()) // Assuming Name is in 2nd column
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
