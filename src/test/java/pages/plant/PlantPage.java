package pages.plant;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.stream.Collectors;

public class PlantPage {
    private WebDriver driver;

    // Locators
    private By searchBox = By.id("plant-search");
    private By resetButton = By.id("reset-btn");
    private By addPlantButton = By.id("add-plant-btn");
    private By plantTable = By.id("plant-table");
    private By plantRows = By.xpath("//table[@id='plant-table']/tbody/tr");
    private By noDataMessage = By.id("no-plants-msg");

    private By nextPageBtn = By.id("pagination-next");
    private By prevPageBtn = By.id("pagination-prev");
    private By pageOneBtn = By.cssSelector(".pagination-page[data-page='1']");

    public PlantPage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "/plants");
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
}
