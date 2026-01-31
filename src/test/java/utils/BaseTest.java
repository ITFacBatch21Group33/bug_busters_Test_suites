package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

public class BaseTest {
    protected static WebDriver driver;
    private static final String BASE_URL = "http://localhost:8080"; // Placeholder

    public static void setUpDriver() {
        if (driver == null) {
            // WebDriverManager is often used, but assuming Selenium 4.6+ Selenium Manager
            // handles drivers automatically
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            // options.addArguments("--headless"); // Optional: run headless

            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().window().maximize();
        }
    }

    public static void tearDownDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }
}
