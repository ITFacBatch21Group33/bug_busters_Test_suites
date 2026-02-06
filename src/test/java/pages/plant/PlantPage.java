package pages.plant;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.stream.Collectors;

public class PlantPage {
    private WebDriver driver;

    // Locators
    private By searchBox = By.name("name"); // Updated to match CategoryPage pattern
    private By resetButton = By.xpath("//button[contains(text(),'Reset')]");
    private By addPlantButton = By.xpath("//*[contains(text(),'Add a Plant')]");
    private By plantTable = By.tagName("table");
    private By plantRows = By.xpath("//table/tbody/tr");
    private By noDataMessage = By.xpath("//*[contains(text(),'No plants') or contains(text(),'not found')]");

    private By nextPageBtn = By.xpath("//button[contains(text(),'Next') or contains(@class,'next')]");
    private By prevPageBtn = By.xpath("//button[contains(text(),'Previous') or contains(@class,'prev')]");

    // Sorting & Filtering
    private By sortNameBtn = By.xpath("//*[contains(text(),'Name')]"); // Likely a header
    private By sortPriceBtn = By.xpath("//*[contains(text(),'Price')]");
    private By sortQuantityBtn = By.xpath("//*[contains(text(),'Stock')]");
    private By categoryDropdown = By.tagName("select"); // Assumption
    private By applyFilterBtn = By.xpath("//button[contains(text(),'Search')]"); // Uses Search button for filter
    private By searchBtn = By.xpath("//button[contains(text(),'Search')]");


    public PlantPage(WebDriver driver) {
        this.driver = driver;
    }
// ... (omitting constructor)
    public void navigateTo() {
        String baseUrl = utils.ConfigLoader.getProperty("base.url");
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/ui/plants");
    }

    public void clickSearch() {
        driver.findElement(searchBtn).click();
    }

    public void selectCategory(String categoryName) {
        driver.findElement(categoryDropdown).sendKeys(categoryName);
    }

    public void clickApplyFilter() {
        driver.findElement(applyFilterBtn).click();
    }

    public void clickSortBy(String field) {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(10));
        
        if (field.equalsIgnoreCase("Name")) {
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(sortNameBtn)).click();
        } else if (field.equalsIgnoreCase("Price")) {
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(sortPriceBtn)).click();
        } else if (field.equalsIgnoreCase("Quantity")) {
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(sortQuantityBtn)).click();
        }
    }

    public List<String> getPlantNames() {
         org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(20));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(plantRows));
        return driver.findElements(By.xpath("//table/tbody/tr/td[1]"))
                .stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public List<Double> getPlantPrices() {
        return driver.findElements(By.xpath("//table/tbody/tr/td[3]"))
                .stream().map(e -> Double.parseDouble(e.getText().replace("$", ""))).collect(Collectors.toList());
    }

    public List<Integer> getPlantQuantities() {
        return driver.findElements(By.xpath("//table/tbody/tr/td[4]"))
                .stream().map(e -> Integer.parseInt(e.getText())).collect(Collectors.toList());
    }
    
    public boolean isEditButtonVisible() {
         return !driver.findElements(By.cssSelector("a[title='Edit']")).isEmpty();
    }

    public boolean isDeleteButtonVisible() {
         return !driver.findElements(By.cssSelector("button[title='Delete']")).isEmpty();
    }

    public void clickAddPlant() {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(10));
        
        // Wait until at least one is present
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(addPlantButton));
        
        List<WebElement> buttons = driver.findElements(addPlantButton);
        boolean clicked = false;
        for (WebElement btn : buttons) {
            if (btn.isDisplayed() && btn.isEnabled()) {
                btn.click();
                clicked = true;
                break;
            }
        }
        
        if (!clicked) {
             // Fallback to standard wait if loop didn't find one (or let it timeout)
             wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(addPlantButton)).click();
        }
    }
    
    public boolean isPaginationVisible() {
        try {
            return driver.findElement(nextPageBtn).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }
    
    public void enterSearchText(String text) {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(20));
        WebElement box = wait
                .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(searchBox));
        box.clear();
        box.sendKeys(text);
    }
    public void clickReset() {
        driver.findElement(resetButton).click();
    }

    public boolean isAddButtonVisible() {
        // Match CategoryPage pattern: check > 0 and isDisplayed
        List<WebElement> buttons = driver.findElements(addPlantButton);
        return buttons.size() > 0 && buttons.get(0).isDisplayed();
    }

    public boolean isPlantListDisplayed() {
        try {
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(10));
            return wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(plantTable)).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException | org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isNoDataMessageDisplayed() {
        List<WebElement> msgs = driver.findElements(noDataMessage);
        return !msgs.isEmpty() && msgs.get(0).isDisplayed();
    }

    public String SearchBoxValue() {
        return driver.findElement(searchBox).getAttribute("value");
    }
}
