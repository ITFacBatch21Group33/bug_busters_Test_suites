package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private WebDriver driver;

    // Locators
    private By usernameField = By.name("username");
    private By passwordField = By.name("password");
    private By loginButton = By.cssSelector("button[type='submit']");
    // Fallback or specific ID if known, e.g. By.id("login-submit")

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateTo() {
        // User confirmed URL is http://localhost:8080/ui/login
        String baseUrl = utils.ConfigLoader.getProperty("base.url");
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/ui/login");
    }

    public void login(String username, String password) {
        try {
            // Debug: print where we are
            System.out.println("DEBUG: Attempting login at " + driver.getCurrentUrl());

            // Try explicit wait or lenient find?
            // For debugging, we just try to find.
            // If simple ID fails, we'll error out, but let's print source in catch.
            driver.findElement(usernameField).clear();
            driver.findElement(usernameField).sendKeys(username);
            driver.findElement(passwordField).clear();
            driver.findElement(passwordField).sendKeys(password);
            driver.findElement(loginButton).click();
            
            // Wait for login to complete (URL change or specific element)
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));
            wait.until(d -> !d.getCurrentUrl().contains("/login"));
        } catch (Exception e) {
            System.out.println("DEBUG: Login failed at URL: " + driver.getCurrentUrl());
            System.out.println("DEBUG: Page Title: " + driver.getTitle());
            System.out.println("DEBUG: Page Source Snippet: "
                    + driver.getPageSource().substring(0, Math.min(driver.getPageSource().length(), 500)));
            throw e;
        }
    }
}
