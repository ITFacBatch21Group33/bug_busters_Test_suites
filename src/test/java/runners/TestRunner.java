package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(features = "src/test/resources/features", glue = { "stepdefinitions", "hellocucumber" }, // Include
                                                                                                          // hellocucumber
                                                                                                          // for
                                                                                                          // tutorial
                                                                                                          // backward
                                                                                                          // compatibility
                                                                                                          // if needed,
                                                                                                          // but mainly
                                                                                                          // stepdefinitions
        plugin = { "pretty", "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json" })
public class TestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
