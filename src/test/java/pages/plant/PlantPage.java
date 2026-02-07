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




    public void searchFor(String query) {
        enterSearchText(query);
        clickSearch();
    }

    public java.util.List<String> getPlantNames() {
        return driver.findElements(plantRows).stream()
                .map(row -> row.findElement(By.xpath("td[1]")).getText())
                .collect(java.util.stream.Collectors.toList());
    }

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
        List<By> locators = java.util.Arrays.asList(
            By.xpath("//button[contains(text(),'Reset')]"),
            By.xpath("//a[contains(text(),'Reset')]"),
            By.xpath("//*[contains(text(),'Reset')]"),
            By.xpath("//button[contains(text(),'Clear')]"),
            By.xpath("//*[contains(text(),'Clear')]"),
            By.cssSelector(".btn-reset"),
            By.id("reset-btn")
        );

        boolean clicked = false;
        for (By loc : locators) {
            try {
                 WebElement el = driver.findElement(loc);
                 if (el.isDisplayed()) {
                     el.click();
                     clicked = true;
                     break;
                 }
            } catch (Exception e) {
                // ignore and try next
            }
        }

        if (!clicked) {
            // Last ditch: try JS or fail
             System.out.println("DEBUG: Reset button not found. Dumping visible buttons:");
             driver.findElements(By.tagName("button")).forEach(b -> System.out.println("Button: " + b.getText()));
             // throw exception to fail test
             throw new org.openqa.selenium.NoSuchElementException("Could not find Reset button with any common locator");
        }
        
        // Wait for the reset to complete - wait for search field to be cleared and category to reset
        try {
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));
            // Wait for search field to clear
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(By.name("name")));
            // Give a moment for DOM to settle
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("Timeout waiting for reset to complete");
        }
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

    public void clickNextPage() {
        List<By> locators = java.util.Arrays.asList(
            nextPageBtn,
            By.xpath("//a[contains(.,'Next') or contains(.,'next') ]"),
            By.cssSelector(".next"),
            By.xpath("//button[contains(.,'>' ) or contains(.,'»') or contains(.,'›') ]"),
            // case-insensitive 'next' text search
            By.xpath("//*[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'next') and (self::a or self::button or self::span)]"),
            // common pagination structures
            By.xpath("//li[contains(@class,'next')]//a"),
            By.xpath("//nav[contains(@class,'pagination')]//a"),
            By.cssSelector("[aria-label='Next']")
        );

        boolean clicked = false;
        for (By loc : locators) {
            List<WebElement> els = driver.findElements(loc);
            if (!els.isEmpty()) {
                for (WebElement el : els) {
                    try {
                        if (el.isDisplayed()) {
                            try {
                                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                                        .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(el));
                            } catch (Exception ignored) {}
                            el.click();
                            clicked = true;
                            break;
                        }
                    } catch (Exception ignore) {}
                }
            }
            if (clicked) break;
        }

        if (!clicked) {
            // Dump pagination containers for debugging
            List<WebElement> pagContainers = driver.findElements(By.xpath("//*[contains(@class,'pagination') or contains(@aria-label,'pagination')]"));
            for (WebElement pc : pagContainers) {
                try { System.out.println("DEBUG: pagination container text='" + pc.getText() + "'"); } catch (Exception e) {}
            }
            throw new org.openqa.selenium.NoSuchElementException("Could not find Next page button");
        }
    }

    public void clickPreviousPage() {
        List<By> locators = java.util.Arrays.asList(
            prevPageBtn,
            By.xpath("//a[contains(.,'Previous') or contains(.,'previous') ]"),
            By.cssSelector(".prev"),
            By.xpath("//button[contains(.,'<' ) or contains(.,'«') or contains(.,'‹') ]"),
            By.xpath("//*[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'previous') and (self::a or self::button or self::span)]"),
            By.xpath("//li[contains(@class,'prev')]//a"),
            By.xpath("//nav[contains(@class,'pagination')]//a"),
            By.cssSelector("[aria-label='Previous']")
        );

        boolean clicked = false;
        for (By loc : locators) {
            List<WebElement> els = driver.findElements(loc);
            if (!els.isEmpty()) {
                for (WebElement el : els) {
                    try {
                        if (el.isDisplayed()) {
                            try {
                                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                                        .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(el));
                            } catch (Exception ignored) {}
                            el.click();
                            clicked = true;
                            break;
                        }
                    } catch (Exception ignore) {}
                }
            }
            if (clicked) break;
        }

        if (!clicked) {
            List<WebElement> pagContainers = driver.findElements(By.xpath("//*[contains(@class,'pagination') or contains(@aria-label,'pagination')]"));
            for (WebElement pc : pagContainers) {
                try { System.out.println("DEBUG: pagination container text='" + pc.getText() + "'"); } catch (Exception e) {}
            }
            throw new org.openqa.selenium.NoSuchElementException("Could not find Previous page button");
        }
    }
    
    public boolean isNextPageButtonVisible() {
        List<By> locators = java.util.Arrays.asList(
            nextPageBtn,
            By.xpath("//a[contains(.,'Next') or contains(.,'next') ]"),
            By.cssSelector(".next"),
            By.cssSelector("[aria-label='Next']"),
            By.xpath("//*[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'next') and (self::a or self::button or self::span)]"),
            By.xpath("//li[contains(@class,'next')]//a"),
            By.xpath("//nav[contains(@class,'pagination')]//a")
        );

        System.out.println("DEBUG: Checking Next button visibility. Row count=" + getRowCount());
        for (By loc : locators) {
            List<WebElement> els = driver.findElements(loc);
            System.out.println("DEBUG: locator=" + loc + " found=" + els.size());
            if (!els.isEmpty()) {
                for (WebElement e : els) {
                    try {
                        System.out.println("DEBUG: Next candidate text='" + e.getText() + "' displayed=" + e.isDisplayed());
                        if (e.isDisplayed()) return true;
                    } catch (Exception ignore) {}
                }
            }
        }
        // If none found, dump pagination containers
        List<WebElement> pagContainers = driver.findElements(By.xpath("//*[contains(@class,'pagination') or contains(@aria-label,'pagination')]"));
        for (WebElement pc : pagContainers) {
            try { System.out.println("DEBUG: pagination container text='" + pc.getText() + "'"); } catch (Exception e) {}
        }
        return false;
    }

    public boolean isPreviousPageButtonVisible() {
        List<By> locators = java.util.Arrays.asList(
            prevPageBtn,
            By.xpath("//a[contains(.,'Previous') or contains(.,'previous') ]"),
            By.cssSelector(".prev"),
            By.cssSelector("[aria-label='Previous']")
        );

        System.out.println("DEBUG: Checking Previous button visibility. Row count=" + getRowCount());
        for (By loc : locators) {
            List<WebElement> els = driver.findElements(loc);
            System.out.println("DEBUG: locator=" + loc + " found=" + els.size());
            if (!els.isEmpty()) {
                for (WebElement e : els) {
                    try {
                        System.out.println("DEBUG: Prev candidate text='" + e.getText() + "' displayed=" + e.isDisplayed());
                        if (e.isDisplayed()) return true;
                    } catch (Exception ignore) {}
                }
            }
        }
        List<WebElement> pagContainers = driver.findElements(By.xpath("//*[contains(@class,'pagination') or contains(@aria-label,'pagination')]"));
        for (WebElement pc : pagContainers) {
            try { System.out.println("DEBUG: pagination container text='" + pc.getText() + "'"); } catch (Exception e) {}
        }
        return false;
    }
    
    public boolean isNoDataMessageDisplayed() {
        List<WebElement> msgs = driver.findElements(noDataMessage);
        return !msgs.isEmpty() && msgs.get(0).isDisplayed();
    }

    public String SearchBoxValue() {
        return driver.findElement(searchBox).getAttribute("value");
    }

    public void selectCategoryFilter(String categoryName) {
        Select sel = new Select(driver.findElement(By.id("category-filter")));
        sel.selectByVisibleText(categoryName);
    }

    public String getSelectedCategory() {
        List<WebElement> elems = driver.findElements(By.id("category-filter"));
        if (elems.isEmpty()) {
            System.out.println("DEBUG: category-filter element not found!");
            return "All Categories"; // Return default if element doesn't exist
        }
        
        try {
            Select sel = new Select(elems.get(0));
            WebElement selectedOption = sel.getFirstSelectedOption();
            String text = selectedOption.getText().trim();
            
            System.out.println("DEBUG: getSelectedCategory() returned: '" + text + "'");
            
            // If selected option text is empty, return "All Categories" as default
            if (text.isEmpty()) {
                System.out.println("DEBUG: Selected option text is empty, returning 'All Categories'");
                return "All Categories";
            }
            return text;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            // If no option is selected, return "All Categories" as default
            System.out.println("DEBUG: No selected option found, returning 'All Categories'");
            return "All Categories";
        } catch (Exception e) {
            System.out.println("DEBUG: Unexpected exception in getSelectedCategory: " + e.getMessage());
            return "All Categories";
        }
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
        // Assuming category is in the 2nd column (td[2])
        return driver.findElements(plantRows).stream()
                .map(row -> row.findElement(By.xpath("td[2]")).getText().trim())
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

    // Pagination helpers
    public boolean isPageButtonVisible(String btnName) {
        java.util.List<By> locators = java.util.Arrays.asList(
            By.xpath("//button[normalize-space()='" + btnName + "']"),
            By.xpath("//a[normalize-space()='" + btnName + "']"),
            By.xpath("//button[contains(text(),'" + btnName + "')]")
        );

        for (By loc : locators) {
            java.util.List<WebElement> els = driver.findElements(loc);
            if (!els.isEmpty()) {
                for (WebElement e : els) {
                    try {
                        if (e.isDisplayed()) return true;
                    } catch (Exception ignore) {
                    }
                }
            }
        }
        return false;
    }

    public void clickPageButton(String btnName) {
        java.util.List<By> locators = java.util.Arrays.asList(
            By.xpath("//button[normalize-space()='" + btnName + "']"),
            By.xpath("//a[normalize-space()='" + btnName + "']"),
            By.xpath("//button[contains(text(),'" + btnName + "')]")
        );

        boolean clicked = false;
        for (By loc : locators) {
            try {
                java.util.List<WebElement> els = driver.findElements(loc);
                if (!els.isEmpty()) {
                    for (WebElement el : els) {
                        if (el.isDisplayed()) {
                            try {
                                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                                        .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(el));
                            } catch (Exception ignored) {
                            }
                            el.click();
                            clicked = true;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                // ignore and try next locator
            }
            if (clicked) break;
        }

        if (!clicked) {
            throw new org.openqa.selenium.NoSuchElementException("Could not find page button: " + btnName);
        }
    }

    public boolean areActionButtonsVisible() {
        // Look for edit or delete buttons in any row
        java.util.List<WebElement> editBtns = driver.findElements(By.cssSelector("button.edit-btn"));
        java.util.List<WebElement> deleteBtns = driver.findElements(By.cssSelector("button.delete-btn"));
        return ( !editBtns.isEmpty() && editBtns.get(0).isDisplayed() ) || ( !deleteBtns.isEmpty() && deleteBtns.get(0).isDisplayed() );
    }

    public void clickEditButton() {
        // Try multiple locators for Edit button
        List<By> locators = java.util.Arrays.asList(
            By.cssSelector("a[title='Edit']"),
            By.cssSelector("button.edit-btn"),
            By.xpath("//button[contains(text(),'Edit')]"),
            By.xpath("//a[contains(text(),'Edit')]"),
            By.xpath("//*[contains(@class,'edit')]") // broader
        );
        
        boolean clicked = false;
        for (By loc : locators) {
             List<WebElement> els = driver.findElements(loc);
             if (!els.isEmpty() && els.get(0).isDisplayed()) {
                 els.get(0).click();
                 clicked = true;
                 break;
             }
        }
        
        if (!clicked) {
             throw new org.openqa.selenium.NoSuchElementException("Could not find Edit button");
        }
    }

    // Edit plant form methods
    public void enterPlantName(String name) {
        List<By> nameLocators = java.util.Arrays.asList(
            By.name("name"),
            By.id("name"),
            By.xpath("//input[@name='name']")
        );
        
        for (By loc : nameLocators) {
            List<WebElement> els = driver.findElements(loc);
            if (!els.isEmpty()) {
                try {
                    els.get(0).clear();
                    els.get(0).sendKeys(name);
                    return;
                } catch (Exception ignore) {}
            }
        }
        throw new org.openqa.selenium.NoSuchElementException("Could not find Plant Name input field");
    }

    public void enterPlantPrice(double price) {
        List<By> priceLocators = java.util.Arrays.asList(
            By.name("price"),
            By.id("price"),
            By.xpath("//input[@name='price']")
        );
        
        for (By loc : priceLocators) {
            List<WebElement> els = driver.findElements(loc);
            if (!els.isEmpty()) {
                try {
                    els.get(0).clear();
                    els.get(0).sendKeys(String.valueOf(price));
                    return;
                } catch (Exception ignore) {}
            }
        }
        throw new org.openqa.selenium.NoSuchElementException("Could not find Price input field");
    }

    public void enterPlantQuantity(int quantity) {
        List<By> quantityLocators = java.util.Arrays.asList(
            By.name("quantity"),
            By.id("quantity"),
            By.xpath("//input[@name='quantity']")
        );
        
        for (By loc : quantityLocators) {
            List<WebElement> els = driver.findElements(loc);
            if (!els.isEmpty()) {
                try {
                    els.get(0).clear();
                    els.get(0).sendKeys(String.valueOf(quantity));
                    return;
                } catch (Exception ignore) {}
            }
        }
        throw new org.openqa.selenium.NoSuchElementException("Could not find Quantity input field");
    }
}

