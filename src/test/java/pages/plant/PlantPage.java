package pages.plant;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import java.util.List;
import java.util.stream.Collectors;

public class PlantPage {
    private WebDriver driver;

    // Locators
    private By searchBox = By.id("plant-search");
    private By searchButton = By.id("search-btn");
    private By resetButton = By.id("reset-btn");
    private By addPlantButton = By.id("add-plant-btn");
    private By plantTable = By.id("plant-table");
    private By plantRows = By.xpath("//table[@id='plant-table']/tbody/tr");
    private By noDataMessage = By.id("no-plants-msg");

    private By nextPageBtn = By.id("pagination-next");
    private By prevPageBtn = By.id("pagination-prev");
    private By pageOneBtn = By.cssSelector(".pagination-page[data-page='1']");

    public void clickSearch() {
        driver.findElement(searchButton).click();
    }

    public void searchFor(String query) {
        enterSearchText(query);
        clickSearch();
    }

    public java.util.List<String> getPlantNames() {
        return driver.findElements(plantRows).stream()
                .map(row -> row.findElement(By.xpath("td[2]")).getText())
                .collect(java.util.stream.Collectors.toList());
    }

    public PlantPage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "/plants");
    }

    public void navigateTo() {
        // Convenience overload to use project base URL
        driver.get(utils.BaseTest.getBaseUrl() + "/ui/plants");
    }

    public void enterSearchText(String text) {
        driver.findElement(searchBox).clear();
        driver.findElement(searchBox).sendKeys(text);
    }

    public void clickReset() {
        driver.findElement(resetButton).click();
    }

    public boolean isAddButtonVisible() {
        List<WebElement> buttons = driver.findElements(addPlantButton);
        return !buttons.isEmpty() && buttons.get(0).isDisplayed();
    }

    public boolean isPlantListDisplayed() {
        return driver.findElement(plantTable).isDisplayed();
    }

    public boolean isNoDataMessageDisplayed() {
        List<WebElement> msgs = driver.findElements(noDataMessage);
        return !msgs.isEmpty() && msgs.get(0).isDisplayed();
    }

    public String SearchBoxValue() {
        return driver.findElement(searchBox).getAttribute("value");
    }

    public boolean isPaginationNextVisible() {
        return driver.findElement(nextPageBtn).isDisplayed();
    }

    public void clickNextPage() {
        driver.findElement(nextPageBtn).click();
    }

    public void clickPrevPage() {
        driver.findElement(prevPageBtn).click();
    }

    public void clickPageNumber(int page) {
         // Simplified selector for demo
         driver.findElement(By.xpath("//button[contains(@class,'pagination-page') and text()='" + page + "']")).click();
    }

    public void clickEditPlant(String plantName) {
        // Find row with plant name and click edit
        // Assuming Edit button is in last column
        WebElement editBtn = driver.findElement(By.xpath("//tr[td[contains(text(),'" + plantName + "')]]//button[contains(@class,'edit-btn')]"));
        editBtn.click();
    }
    public void clickAddPlant() {
        driver.findElement(addPlantButton).click();
    }
    public void selectCategoryFilter(String categoryName) {
        Select sel = new Select(driver.findElement(By.id("category-filter")));
        sel.selectByVisibleText(categoryName);
    }

    public String getSelectedCategory() {
        List<WebElement> elems = driver.findElements(By.id("category-filter"));
        if (elems.isEmpty()) return "";
        Select sel = new Select(elems.get(0));
        return sel.getFirstSelectedOption().getText();
    }

    public void clickSortByColumn(String columnName) {
        driver.findElement(By.xpath("//th[normalize-space()='" + columnName + "']")).click();
    }

    public boolean isSortReset() {
        // Assumes headers indicate active sorting via a "sorted" class. If absent, return true.
        return driver.findElements(By.cssSelector("th.sorted")).isEmpty();
    }

    public int getRowCount() {
        return driver.findElements(plantRows).size();
    }

    public java.util.List<String> getPlantCategories() {
        // Assuming category is in the 3rd column (td[3])
        return driver.findElements(plantRows).stream()
                .map(row -> row.findElement(By.xpath("td[3]")).getText().trim())
                .collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<Double> getPlantPrices() {
        // Assuming price is in the 4th column (td[4]) and may include currency symbols
        return driver.findElements(plantRows).stream()
                .map(row -> row.findElement(By.xpath("td[4]")).getText().replaceAll("[^0-9.\\-]", "").trim())
                .map(s -> {
                    try {
                        return Double.parseDouble(s);
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                })
                .collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<Integer> getPlantQuantities() {
        // Assuming quantity is in the 5th column (td[5])
        return driver.findElements(plantRows).stream()
                .map(row -> row.findElement(By.xpath("td[5]")).getText().replaceAll("[^0-9\\-]", "").trim())
                .map(s -> {
                    try {
                        return Integer.parseInt(s);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .collect(java.util.stream.Collectors.toList());
    }

    public boolean isPlantPresent(String name) {
        List<WebElement> matches = driver.findElements(By.xpath("//tr[td[contains(normalize-space(), '" + name + "')]]"));
        return !matches.isEmpty();
    }

    public boolean areActionButtonsVisible() {
        // Look for edit or delete buttons in any row
        java.util.List<WebElement> editBtns = driver.findElements(By.cssSelector("button.edit-btn"));
        java.util.List<WebElement> deleteBtns = driver.findElements(By.cssSelector("button.delete-btn"));
        return ( !editBtns.isEmpty() && editBtns.get(0).isDisplayed() ) || ( !deleteBtns.isEmpty() && deleteBtns.get(0).isDisplayed() );
    }
}

