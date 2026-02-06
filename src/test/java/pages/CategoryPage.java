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

    private By noDataMessageFlexible = By.xpath(
            "//*[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'no category')"
                    +
                    " or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'no categories')"
                    +
                    " or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'no results')"
                    +
                    " or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'not found')]");

    private By categoryDataRows = By.xpath("//table/tbody/tr[td and not(td[@colspan])]");

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
        if (baseUrl.endsWith("/"))
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        driver.get(baseUrl + "/ui/categories/add");
    }

    public void enterCategoryName(String name) {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(15));
        WebElement input = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(addCategoryNameInput));
        input.clear();
        input.sendKeys(name);
    }

    public void selectParentCategory(String visibleText) {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(15));
        WebElement dropdown = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions
                        .visibilityOfElementLocated(addCategoryParentDropdown));
        new Select(dropdown).selectByVisibleText(visibleText);
    }

    public void clickSaveOnAddCategory() {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(15));
        WebElement save = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(addCategorySaveButton));
        save.click();
    }

    public void waitForCategoriesListPage() {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(15));
        wait.until(d -> d.getCurrentUrl().contains("/ui/categories") && !d.getCurrentUrl().contains("/add"));
    }

    // Validation empty category name error
    public void clearCategoryName() {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(10));
        WebElement input = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(addCategoryNameInput));
        input.clear();
    }

    public boolean isOnAddCategoryPage() {
        return driver.getCurrentUrl() != null && driver.getCurrentUrl().contains("/ui/categories/add");
    }

    public void waitForAddCategoryPage() {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(10));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/ui/categories/add"));
    }

    public boolean isValidationMessageDisplayed(String message) {
        try {
            By by = By.xpath("//*[contains(normalize-space(.), " + xpathLiteral(message) + ")]");
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                    java.time.Duration.ofSeconds(10));
            return wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(by))
                    .isDisplayed();
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    private static String xpathLiteral(String text) {
        if (text.contains("'") && text.contains("\"")) {
            String[] parts = text.split("\"", -1);
            StringBuilder sb = new StringBuilder("concat(");
            for (int i = 0; i < parts.length; i++) {
                if (i > 0)
                    sb.append(", '\"', ");
                sb.append("'").append(parts[i]).append("'");
            }
            sb.append(")");
            return sb.toString();
        }
        if (text.contains("\""))
            return "'" + text + "'";
        return "\"" + text + "\"";
    }

    // Check cancel add category
    private By addCategoryCancelButton = By
            .xpath("//button[normalize-space()='Cancel'] | //a[normalize-space()='Cancel']");

    public void clickCancelOnAddCategory() {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(15));
        WebElement cancel = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(addCategoryCancelButton));
        cancel.click();
    }

    public boolean isCategoryPresentInResults(String categoryName) {
        String cellXpath = "//table/tbody/tr/td[2][normalize-space()=" + xpathLiteral(categoryName) + "]";
        return driver.findElements(By.xpath(cellXpath)).size() > 0;
    }

    public void waitForResultsOrEmptyState() {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(15));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.or(
                org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(categoryRows),
                org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(noDataMessageFlexible),
                org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(noDataMessage)));
    }

    // Filter categories with parent
    public void filterByParentAndWait(String parentName) {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(15));

        WebElement firstRow = null;
        List<WebElement> rows = driver.findElements(categoryRows);
        if (!rows.isEmpty())
            firstRow = rows.get(0);

        WebElement dropdown = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(parentFilterDropdown));
        new Select(dropdown).selectByVisibleText(parentName);

        // IMPORTANT: trigger filtering by clicking Search/Submit
        WebElement submit = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(searchButton));
        try {
            submit.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);
        }

        // Wait for refresh: either rows change OR empty-state appears
        if (firstRow != null) {
            try {
                wait.until(org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf(firstRow));
            } catch (org.openqa.selenium.TimeoutException ignored) {
            }
        }

        waitForResultsOrEmptyState();
    }

    public List<String> getParentCategoryTexts() {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(15));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(categoryRows));

        return driver.findElements(By.xpath("//table/tbody/tr/td[3]")).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public String getParentTextForCategory(String categoryName) {
        String xpath = "//table/tbody/tr[td[2][normalize-space()=" + xpathLiteral(categoryName) + "]]/td[3]";
        List<WebElement> els = driver.findElements(By.xpath(xpath));
        return els.isEmpty() ? null : els.get(0).getText();
    }

    public int getCategoryDataRowCount() {
        return driver.findElements(categoryDataRows).size();
    }

    public boolean isNoResultsMessageDisplayedFlexible() {
        try {
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                    java.time.Duration.ofSeconds(10));
            WebElement el = wait.until(
                    org.openqa.selenium.support.ui.ExpectedConditions
                            .visibilityOfElementLocated(noDataMessageFlexible));
            return el.isDisplayed();
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    // Sort by ascending order
    public void sortBy(String column, String order) {
        String col = (column == null) ? "" : column.trim();
        String ord = (order == null) ? "" : order.trim();

        if ("ID".equalsIgnoreCase(col)) {
            sortByIdLinkAndWait(ord);
            return;
        }

        if ("Name".equalsIgnoreCase(col)) {
            sortByNameLinkAndWait(ord);
            return;
        }

        int clicks = "descending".equalsIgnoreCase(ord) ? 2 : 1;
        for (int i = 0; i < clicks; i++)
            clickColumnHeaderAndWait(column);
    }

    private void sortByIdLinkAndWait(String order) {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(15));

        String desiredDir = "descending".equalsIgnoreCase(order) ? "desc" : "asc";

        // If we’re already on the desired sort, do nothing
        String url = driver.getCurrentUrl();
        if (url != null && url.contains("sortField=id") && url.contains("sortDir=" + desiredDir))
            return;

        // Click the *link* that will produce the desired direction
        By desiredLink = By.cssSelector("table a[href*='sortField=id'][href*='sortDir=" + desiredDir + "']");

        // Fallback: click any ID sort link (if UI only renders one state at a time)
        if (driver.findElements(desiredLink).isEmpty()) {
            clickColumnHeaderAndWait("ID");
            return;
        }

        String beforeUrl = driver.getCurrentUrl();
        WebElement link = wait
                .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(desiredLink));

        try {
            link.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        }

        // Wait for navigation (sorting is server-side via query params)
        wait.until(d -> {
            String after = d.getCurrentUrl();
            return after != null && after.contains("sortField=id") && after.contains("sortDir=" + desiredDir);
        });

        waitForResultsOrEmptyState();
    }

    private void sortByNameLinkAndWait(String order) {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(15));

        String desiredDir = "descending".equalsIgnoreCase(order) ? "desc" : "asc";

        String url = driver.getCurrentUrl();
        if (url != null && url.contains("sortField=name") && url.contains("sortDir=" + desiredDir))
            return;

        WebElement firstRow = null;
        List<WebElement> rows = driver.findElements(categoryRows);
        if (!rows.isEmpty())
            firstRow = rows.get(0);

        // Click the *exact* Name sort link for the desired direction
        By linkBy = By.xpath(
                "//table//th//a[normalize-space()='Name' and contains(@href,'sortField=name') and contains(@href,'sortDir="
                        + desiredDir + "')]");
        if (driver.findElements(linkBy).isEmpty()) {
            // Fallback if header text differs but href is correct
            linkBy = By.xpath(
                    "//table//a[contains(@href,'sortField=name') and contains(@href,'sortDir=" + desiredDir + "')]");
        }

        WebElement link = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(linkBy));

        try {
            link.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        }

        // Wait until the URL really reflects the requested sort
        wait.until(d -> {
            String after = d.getCurrentUrl();
            return after != null && after.contains("sortField=name") && after.contains("sortDir=" + desiredDir);
        });

        if (firstRow != null) {
            try {
                wait.until(org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf(firstRow));
            } catch (org.openqa.selenium.TimeoutException ignored) {
            }
        }

        waitForResultsOrEmptyState();
    }

    public List<Integer> getIdColumnValues() {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(15));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.or(
                org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(categoryDataRows),
                org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(noDataMessageFlexible),
                org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(noDataMessage)));

        return driver.findElements(By.xpath("//table/tbody/tr[td and not(td[@colspan])]/td[1]"))
                .stream()
                .map(WebElement::getText)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(java.util.stream.Collectors.toList());
    }

    private void clickColumnHeaderAndWait(String column) {
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver,
                java.time.Duration.ofSeconds(15));

        // capture current state so we can wait for refresh/navigation
        String beforeUrl = driver.getCurrentUrl();

        WebElement firstRow = null;
        List<WebElement> rows = driver.findElements(categoryRows);
        if (!rows.isEmpty())
            firstRow = rows.get(0);

        String col = (column == null) ? "" : column.trim();
        String lower = col.toLowerCase();
        String wordNeedle = " " + lower + " ";

        String ciWordMatch = "contains(concat(' ', translate(normalize-space(.), " +
                "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), ' '), " +
                xpathLiteral(wordNeedle) + ")";

        // Prefer clicking the <a> inside <th> (that's what actually sorts in your UI)
        By linkBy = By.xpath("//table//th//a[" + ciWordMatch + "]");
        By thBy = By.xpath("//table//th[" + ciWordMatch + "]");

        // Check if the column exists before trying to click
        if (driver.findElements(linkBy).isEmpty() && driver.findElements(thBy).isEmpty()) {
            System.out.println("WARNING: Column '" + column + "' not found or not sortable in the UI. Skipping sort.");
            return; // Column doesn't exist, skip sorting
        }

        By target = driver.findElements(linkBy).isEmpty() ? thBy : linkBy;

        WebElement header = wait.until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(target));

        try {
            header.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", header);
        }

        // Sorting is server-side (href), so URL often changes; otherwise table rows
        // refresh
        try {
            wait.until(d -> {
                String afterUrl = d.getCurrentUrl();
                return afterUrl != null && !afterUrl.equals(beforeUrl);
            });
        } catch (org.openqa.selenium.TimeoutException ignored) {
        }

        if (firstRow != null) {
            try {
                wait.until(org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf(firstRow));
            } catch (org.openqa.selenium.TimeoutException ignored) {
            }
        }

        waitForResultsOrEmptyState();
    }

}