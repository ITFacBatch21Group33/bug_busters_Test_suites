package stepdefinitions.category.ui;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import utils.BaseTest;

public class CommonSteps {

    @Before("@UI")
    public void setupUI() {
        BaseTest.setUpDriver();
    }

    @After("@UI")
    public void tearDownUI() {
        BaseTest.tearDownDriver();
    }
}
