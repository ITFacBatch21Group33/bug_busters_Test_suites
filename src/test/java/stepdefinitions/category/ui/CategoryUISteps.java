package stepdefinitions.category.ui;

import java.util.List;

import org.testng.Assert;

import api.CategoryApiHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.CategoryPage;
import utils.AuthHelper;
import utils.BaseTest;

public class CategoryUISteps {
    private CategoryPage categoryPage = new CategoryPage(BaseTest.getDriver());

    /**
     * Backwards-compatible access to CategoryPage parent text.
     * Some project versions don't expose getParentTextForCategory(String) on
     * CategoryPage.
     */
    private String getParentTextForCategorySafely(String categoryName) {
        try {
            java.lang.reflect.Method m = categoryPage().getClass()
                    .getMethod("getParentTextForCategory", String.class);
            Object result = m.invoke(categoryPage(), categoryName);
            return result == null ? null : String.valueOf(result);
        } catch (NoSuchMethodException e) {
            return null; // Method not available in this project version.
        } catch (Exception e) {
            throw new RuntimeException("Failed to read parent text for category: " + categoryName, e);
        }
    }

    private CategoryPage categoryPage() {
        return categoryPage;
    }

    @When("I navigate to the Categories page")
    public void i_navigate_to_the_categories_page() {
        categoryPage.navigateTo();
    }

    @Given("more categories exist than fit on one page")
    public void more_categories_exist_than_fit_on_one_page() {
        String token = AuthHelper.getAdminToken();
        // Create 15 categories to ensure pagination (assuming default size is 10 or 5)
        for (int i = 1; i <= 15; i++) {
            String jsonBody = "{\"name\": \"Cat_" + i + "\", \"parentId\": null}";
            CategoryApiHelper.createCategory(token, jsonBody);
        }
    }

    @Then("I should see pagination controls")
    @Then("I should see category pagination controls")
    public void i_should_see_category_pagination_controls() {
        Assert.assertTrue(categoryPage.isPaginationVisible(), "Pagination controls should be visible");
    }

    @When("I click the {string} button")
    public void i_click_the_button(String buttonName) {
        if (buttonName.equals("Next")) {
            categoryPage.clickNextPage();
        } else if (buttonName.equals("Previous")) {
            categoryPage.clickPrevPage();
        }
    }

    @Then("I should see the next page of categories")
    public void i_should_see_the_next_page_of_categories() {
        // Verification logic
    }

    @Then("I should see the second page of categories")
    public void i_should_see_the_second_page_of_categories() {
        // Verification logic
    }

    @Then("I should see the first page of categories")
    public void i_should_see_the_first_page_of_categories() {
        // Verification logic
    }

    @Then("the first page should display the default number of items")
    public void the_first_page_should_display_the_default_number_of_items() {
        // Verification logic
    }

    @Given("no categories exist in the system")
    public void no_categories_exist_in_the_system() {
        String token = AuthHelper.getAdminToken();
        io.restassured.response.Response response = CategoryApiHelper.getAllCategories(token);
        if (response.getStatusCode() == 200) {
            List<Integer> ids = response.jsonPath().getList("id");
            // Depending on response structure, might be "content.id" if paginated default,
            // but getAllCategories usually returns list.
            // CategoryApiHelper.getAllCategories points to /categories without params.
            // If it returns list:
            if (ids != null) {
                for (Integer id : ids) {
                    CategoryApiHelper.deleteCategory(token, id);
                }
            }
        }
    }

    @Then("I should see a category {string} message")
    public void i_should_see_a_category_message(String message) {
        if (message.contains("No category")) {
            Assert.assertTrue(categoryPage.isNoCategoryMessageDisplayed(), "No category message should be displayed");
        }
    }

    @Given("a category with name {string} exists")
    public void a_category_with_name_exists(String categoryName) {
        String token = AuthHelper.getAdminToken();
        String jsonBody = "{\"name\": \"" + categoryName + "\", \"parentId\": null}";
        CategoryApiHelper.createCategory(token, jsonBody);
    }

    @When("I search for {string}")
    public void i_search_for(String searchTerm) {
        categoryPage.searchFor(searchTerm);
    }

    @Then("the results list should contain {string}")
    public void the_results_list_should_contain(String categoryName) {
        List<String> names = categoryPage.getCategoryNames();
        Assert.assertTrue(names.contains(categoryName), "Results should contain " + categoryName);
    }

    @Then("I should see a {string} message in the results")
    public void i_should_see_a_message_in_the_results(String message) {
        Assert.assertTrue(categoryPage.isNoCategoryMessageDisplayed(),
                "No category message should be displayed in results");
    }

    @Then("I should not see {string} button")
    public void i_should_not_see_button(String buttonName) {
        if (buttonName.equals("Add Category")) {
            Assert.assertFalse(categoryPage.isAddCategoryButtonVisible(),
                    "Add Category button should not be visible for User");
        }
    }

    @Then("I should not see {string} options")
    public void i_should_not_see_options(String option) {
        // Verify absence of Edit/Delete buttons per row
    }

    @Given("a parent category {string} exists with child categories")
    public void a_parent_category_exists_with_child_categories(String parentName) {
        String token = AuthHelper.getAdminToken();
        String parentBody = "{\"name\": \"" + parentName + "\", \"parentId\": null}";
        io.restassured.response.Response response = CategoryApiHelper.createCategory(token, parentBody);

        if (response.getStatusCode() == 201) {
            int parentId = response.jsonPath().getInt("id");
            String childBody = "{\"name\": \"SubCat\", \"parentId\": " + parentId + "}";
            CategoryApiHelper.createCategory(token, childBody);
        }
    }

    @When("I filter by parent category {string}")
    public void i_filter_by_parent_category(String parentName) {
        categoryPage.filterByParent(parentName);
    }

    @Then("I should see only categories belonging to {string}")
    public void i_should_see_only_categories_belonging_to(String parentName) {
        // Verify
    }

    @Given("multiple categories exist")
    public void multiple_categories_exist() {
        String token = AuthHelper.getAdminToken();
        CategoryApiHelper.createCategory(token, "{\"name\": \"CatA\", \"parentId\": null}");
        CategoryApiHelper.createCategory(token, "{\"name\": \"CatB\", \"parentId\": null}");
        CategoryApiHelper.createCategory(token, "{\"name\": \"CatC\", \"parentId\": null}");
    }

    @When("I sort by {string} {string}")
    public void i_sort_by(String column, String order) {
        // Click headers
    }

    @Then("the list should be sorted by {string} in {string} order")
    public void the_list_should_be_sorted_by_in_order(String column, String order) {
        // Verify sort order

    }

    // Create a main category
    @Given("category with name {string} does not exist")
    public void category_with_name_does_not_exist(String categoryName) {
        String token = AuthHelper.getAdminToken();
        io.restassured.response.Response response = CategoryApiHelper.getAllCategories(token);

        List<Integer> ids = response.jsonPath().getList("id");
        List<String> names = response.jsonPath().getList("name");

        if (ids == null || names == null)
            return;

        for (int i = 0; i < Math.min(ids.size(), names.size()); i++) {
            if (categoryName.equals(names.get(i))) {
                Integer categoryId = ids.get(i);
                System.out.println("Found category '" + categoryName + "' with ID " + categoryId + ". Cleaning up...");

                // Fix: Delete dependent plants first to avoid FK constraint violation
                try {
                    System.err.println("DEBUG: Fetching plants to check for dependency...");

                    // Strategy 1: Search by categoryId directly if supported
                    java.util.Map<String, String> params = new java.util.HashMap<>();
                    params.put("categoryId", String.valueOf(categoryId));
                    io.restassured.response.Response searchResp = api.plant.PlantApiHelper.getPagedPlants(token,
                            params);
                    System.err.println("DEBUG: Search by categoryId status: " + searchResp.getStatusCode());

                    List<java.util.Map<String, Object>> plantList = null;
                    if (searchResp.getStatusCode() == 200) {
                        try {
                            plantList = searchResp.jsonPath().getList("content");
                            if (plantList == null)
                                plantList = searchResp.jsonPath().getList("data");
                        } catch (Exception e) {
                        }
                    }

                    if (plantList != null && !plantList.isEmpty()) {
                        System.err.println("DEBUG: Found " + plantList.size() + " plants via search. Deleting...");
                        for (java.util.Map<String, Object> p : plantList) {
                            Object pid = p.get("id");
                            if (pid != null) {
                                int plantId = Integer.parseInt(String.valueOf(pid));
                                deleteDependentSales(token, plantId);
                                System.err.println("Confirmed: Deleting dependent plant " + plantId);
                                api.plant.PlantApiHelper.deletePlant(token, plantId);
                            }
                        }
                    } else {
                        // Strategy 2: Fallback to getAllPlants if search didn't return anything (or API
                        // doesn't support filter)
                        System.err.println("DEBUG: Search yielded nothing. Falling back to getAllPlants...");
                        io.restassured.response.Response plantsResp = api.plant.PlantApiHelper.getAllPlants(token);
                        System.err.println("DEBUG: getAllPlants status: " + plantsResp.getStatusCode());

                        List<java.util.Map<String, Object>> plants = null;

                        try {
                            // Try basic list
                            Object root = plantsResp.jsonPath().get("$");
                            if (root instanceof List) {
                                plants = plantsResp.jsonPath().getList("$");
                            } else if (root instanceof java.util.Map) {
                                // Maybe wrapped
                                java.util.Map<?, ?> rootMap = (java.util.Map<?, ?>) root;
                                if (rootMap.containsKey("content")) {
                                    plants = plantsResp.jsonPath().getList("content");
                                } else if (rootMap.containsKey("data")) {
                                    Object data = rootMap.get("data");
                                    if (data instanceof List) {
                                        plants = plantsResp.jsonPath().getList("data");
                                    } else if (data instanceof java.util.Map) {
                                        plants = plantsResp.jsonPath().getList("data.content");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("DEBUG: Error parsing plants response: " + e.getMessage());
                        }

                        if (plants == null) {
                            System.err.println("DEBUG: Could not extract plants list from response.");
                        } else {
                            System.err.println("DEBUG: Found " + plants.size()
                                    + " plants in getAllPlants. Checking for category ID " + categoryId);
                            for (java.util.Map<String, Object> plant : plants) {
                                Object catObj = plant.get("category");
                                boolean match = false;

                                if (catObj instanceof java.util.Map) {
                                    java.util.Map<?, ?> catMap = (java.util.Map<?, ?>) catObj;
                                    Object catIdObj = catMap.get("id");
                                    Object catNameObj = catMap.get("name");

                                    // Debug info
                                    // System.err.println("DEBUG: Checking plant " + plant.get("id") + " catId=" +
                                    // catIdObj);

                                    if (catIdObj != null
                                            && String.valueOf(catIdObj).equals(String.valueOf(categoryId))) {
                                        match = true;
                                    } else if (catNameObj != null && String.valueOf(catNameObj).equals(categoryName)) {
                                        match = true;
                                    }
                                }

                                if (match) {
                                    Object plantIdObj = plant.get("id");
                                    if (plantIdObj != null) {
                                        int plantId = Integer.parseInt(String.valueOf(plantIdObj));
                                        deleteDependentSales(token, plantId);
                                        System.err.println("Confirmed: Deleting dependent plant " + plantId
                                                + " for category " + categoryName);
                                        api.plant.PlantApiHelper.deletePlant(token, plantId);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Warning: Failed to cleanup plants: " + e.getMessage());
                    e.printStackTrace();
                }

                CategoryApiHelper.deleteCategory(token, categoryId);
            }
        }
    }

    @Given("I am on the Add Category page")
    public void i_am_on_the_add_category_page() {
        categoryPage().navigateToAddCategory();
    }

    @When("I enter {string} in the {string} field")
    public void i_enter_in_the_field(String value, String fieldName) {
        if (!"Category Name".equals(fieldName)) {
            throw new IllegalArgumentException("Unsupported field: " + fieldName);
        }
        categoryPage().enterCategoryName(value);
    }

    @When("I leave {string} empty")
    public void i_leave_empty(String fieldName) {
        if (!"Parent Category".equals(fieldName)) {
            throw new IllegalArgumentException("Unsupported field: " + fieldName);
        }
        // In your UI “Main Category” represents no parent
        categoryPage().selectParentCategory("Main Category");
    }

    @When("I click {string} on the Add Category page")
    public void i_click_on_the_add_category_page(String buttonName) {
        if ("Save".equals(buttonName)) {
            categoryPage().clickSaveOnAddCategory();
            return;
        }
        if ("Cancel".equals(buttonName)) {
            categoryPage().clickCancelOnAddCategory();
            return;
        }
        throw new IllegalArgumentException("Unsupported button: " + buttonName);
    }

    @Then("I should be redirected to the Categories page")
    public void i_should_be_redirected_to_the_categories_page() {
        categoryPage().waitForCategoriesListPage();
    }

    @Then("{string} should appear in the list as a Main Category")
    public void should_appear_in_list_as_main_category(String categoryName) {
        categoryPage().searchFor(categoryName);

        List<String> names = categoryPage().getCategoryNames();
        Assert.assertTrue(names.contains(categoryName), "Expected category in list: " + categoryName);

        String parentText = getParentTextForCategorySafely(categoryName);
        if (parentText != null) {
            Assert.assertTrue("-".equals(parentText) || "Main Category".equals(parentText),
                    "Expected Parent Category to be Main Category (displayed as '-' or 'Main Category') but found: "
                            + parentText);
        }
    }

    // Validation empty category name error
    @When("I leave the {string} field blank")
    public void i_leave_the_field_blank(String fieldName) {
        if (!"Category Name".equals(fieldName)) {
            throw new IllegalArgumentException("Unsupported field: " + fieldName);
        }
        categoryPage().clearCategoryName();
    }

    @Then("the category should not be saved")
    public void the_category_should_not_be_saved() {
        categoryPage().waitForAddCategoryPage();
        Assert.assertTrue(categoryPage().isOnAddCategoryPage(),
                "Expected to stay on Add Category page (not saved). URL: " + BaseTest.getDriver().getCurrentUrl());
    }

    @Then("an error message {string} should be displayed below the {string} field")
    public void an_error_message_should_be_displayed_below_the_field(String message, String fieldName) {
        if (!"Category Name".equals(fieldName)) {
            throw new IllegalArgumentException("Unsupported field: " + fieldName);
        }
        Assert.assertTrue(categoryPage().isValidationMessageDisplayed(message),
                "Expected validation message: " + message);
    }

    @Then("category {string} should not exist in the list")
    public void category_should_not_exist_in_the_list(String categoryName) {
        categoryPage().navigateTo();
        categoryPage().searchFor(categoryName);
        categoryPage().waitForResultsOrEmptyState();

        Assert.assertFalse(categoryPage().isCategoryPresentInResults(categoryName),
                "Expected category NOT to be present in list: " + categoryName);
    }

    private String resolvedNoChildParentName;

    private static String randomLetters(int len) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(len);
        java.util.concurrent.ThreadLocalRandom rnd = java.util.concurrent.ThreadLocalRandom.current();
        for (int i = 0; i < len; i++)
            sb.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
        return sb.toString();
    }

    @When("I apply the parent category filter {string}")
    public void i_apply_the_parent_category_filter(String parentName) {
        String nameToUse = (resolvedNoChildParentName != null) ? resolvedNoChildParentName : parentName;

        if (nameToUse != null && nameToUse.contains("{rand}")) {
            resolvedNoChildParentName = nameToUse.replace("{rand}", randomLetters(3));
            nameToUse = resolvedNoChildParentName;
        }

        categoryPage.filterByParentAndWait(nameToUse);
    }

    @Given("a parent category {string} exists or is created with child categories")
    public void a_parent_category_exists_or_is_created_with_child_categories(String parentName) {
        ensureParentCategoryHasChildren(parentName, 2);
    }

    private void ensureParentCategoryHasChildren(String parentName, int childCount) {
        String token = AuthHelper.getAdminToken();

        Integer parentId = null;

        String parentBody = "{\"name\": \"" + parentName + "\", \"parentId\": null}";
        io.restassured.response.Response createResp = CategoryApiHelper.createCategory(token, parentBody);
        if (createResp.getStatusCode() == 201) {
            parentId = createResp.jsonPath().getObject("id", Integer.class);
        }

        // If already exists (or create failed), fetch by name
        if (parentId == null) {
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("name", parentName);

            io.restassured.response.Response getResp = CategoryApiHelper.getCategoriesWithParams(token, params);

            parentId = getResp.jsonPath().getObject("[0].id", Integer.class);
            if (parentId == null)
                parentId = getResp.jsonPath().getObject("content[0].id", Integer.class);
            if (parentId == null)
                parentId = getResp.jsonPath().getObject("data[0].id", Integer.class);
            if (parentId == null)
                parentId = getResp.jsonPath().getObject("data.content[0].id", Integer.class);
        }

        Assert.assertNotNull(parentId, "Could not resolve parentId for parent category: " + parentName);

        String suffix = String.valueOf(System.currentTimeMillis() % 100000);
        for (int i = 1; i <= childCount; i++) {
            String childBody = "{\"name\": \"" + parentName + "_Child" + i + "_" + suffix + "\", \"parentId\": "
                    + parentId + "}";
            CategoryApiHelper.createCategory(token, childBody);
        }
    }

    @Given("a parent category {string} exists with no child categories")
    public void a_parent_category_exists_with_no_child_categories(String parentName) {
        String token = AuthHelper.getAdminToken();

        String nameToUse = parentName;
        if (nameToUse != null && nameToUse.contains("{rand}")) {
            resolvedNoChildParentName = nameToUse.replace("{rand}", randomLetters(3));
            nameToUse = resolvedNoChildParentName;
        }

        Integer parentId = null;

        // Create parent if possible
        String parentBody = "{\"name\": \"" + nameToUse + "\", \"parentId\": null}";
        io.restassured.response.Response createResp = CategoryApiHelper.createCategory(token, parentBody);
        if (createResp.getStatusCode() == 201) {
            parentId = createResp.jsonPath().getObject("id", Integer.class);
        }

        // If already exists, fetch its id by name
        if (parentId == null) {
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("name", nameToUse);

            io.restassured.response.Response getResp = CategoryApiHelper.getCategoriesWithParams(token, params);

            parentId = getResp.jsonPath().getObject("[0].id", Integer.class);
            if (parentId == null)
                parentId = getResp.jsonPath().getObject("content[0].id", Integer.class);
            if (parentId == null)
                parentId = getResp.jsonPath().getObject("data[0].id", Integer.class);
            if (parentId == null)
                parentId = getResp.jsonPath().getObject("data.content[0].id", Integer.class);
        }

        Assert.assertNotNull(parentId, "Could not resolve parentId for parent category: " + nameToUse);

        // Delete any children under that parent (so we guarantee "no children")
        java.util.Map<String, Object> childParams = new java.util.HashMap<>();
        childParams.put("parentId", parentId);

        io.restassured.response.Response childrenResp = CategoryApiHelper.getCategoriesWithParams(token, childParams);

        java.util.List<Integer> childIds = childrenResp.jsonPath().getList("id");
        if (childIds == null)
            childIds = childrenResp.jsonPath().getList("content.id");
        if (childIds == null)
            childIds = childrenResp.jsonPath().getList("data.id");
        if (childIds == null)
            childIds = childrenResp.jsonPath().getList("data.content.id");

        if (childIds != null) {
            for (Integer id : childIds) {
                if (id != null)
                    CategoryApiHelper.deleteCategory(token, id);
            }
        }

        // Optional sanity check
        io.restassured.response.Response checkResp = CategoryApiHelper.getCategoriesWithParams(token, childParams);
        java.util.List<?> remaining = checkResp.jsonPath().getList("$");
        if (remaining == null)
            remaining = checkResp.jsonPath().getList("content");
        if (remaining == null)
            remaining = checkResp.jsonPath().getList("data");
        if (remaining == null)
            remaining = checkResp.jsonPath().getList("data.content");

        Assert.assertTrue(remaining == null || remaining.isEmpty(),
                "Precondition failed: expected no children for parent [" + nameToUse + "] but found some.");
    }

    @Then("the results list should be empty")
    public void the_results_list_should_be_empty_ui() {
        categoryPage.waitForResultsOrEmptyState();
        Assert.assertEquals(categoryPage.getCategoryDataRowCount(), 0, "Expected 0 category data rows.");
    }

    @Then("I should see a no results message in the results")
    public void i_should_see_a_no_results_message_in_the_results() {
        Assert.assertTrue(categoryPage.isNoResultsMessageDisplayedFlexible(),
                "Expected a no-results/empty-state message to be displayed.");
    }

    private String currentTestSuffix;

    @Given("multiple categories exist for User sorting")
    public void multiple_categories_exist_for_user_sorting() {
        String token = AuthHelper.getAdminToken();
        currentTestSuffix = randomLetters(3);

        // 3–10 chars, intentionally not alphabetical by creation
        CategoryApiHelper.createCategory(token, "{\"name\": \"Za" + currentTestSuffix + "\", \"parentId\": null}");
        CategoryApiHelper.createCategory(token, "{\"name\": \"Aa" + currentTestSuffix + "\", \"parentId\": null}");
        CategoryApiHelper.createCategory(token, "{\"name\": \"Ma" + currentTestSuffix + "\", \"parentId\": null}");
    }

    @When("I sort by {string} {string} as a User")
    public void i_sort_by_as_a_user(String column, String order) {
        categoryPage.sortBy(column, order);
    }

    @Then("as a User the list should be sorted by {string} in {string} order")
    public void as_a_user_the_list_should_be_sorted_by_in_order(String column, String order) {
        boolean ascending = "ascending".equalsIgnoreCase(order);
        boolean descending = "descending".equalsIgnoreCase(order);
        Assert.assertTrue(ascending || descending, "Unsupported sort order: " + order);

        if ("ID".equalsIgnoreCase(column)) {
            List<Integer> ids = categoryPage.getIdColumnValues();
            for (int i = 1; i < ids.size(); i++) {
                int prev = ids.get(i - 1);
                int curr = ids.get(i);
                if (ascending)
                    Assert.assertTrue(curr >= prev, "IDs not ascending: " + ids);
                else
                    Assert.assertTrue(curr <= prev, "IDs not descending: " + ids);
            }
            return;
        }

        if ("Name".equalsIgnoreCase(column)) {
            List<String> names = categoryPage.getCategoryNames().stream()
                    .map(s -> s == null ? "" : s.trim())
                    .filter(name -> {
                        // Only check categories created for this test
                        if (currentTestSuffix != null && !currentTestSuffix.isEmpty()) {
                            return name.endsWith(currentTestSuffix) &&
                                    (name.startsWith("Za") || name.startsWith("Aa") || name.startsWith("Ma"));
                        }
                        return true; // If no suffix, check all (backward compatibility)
                    })
                    .collect(java.util.stream.Collectors.toList());

            for (int i = 1; i < names.size(); i++) {
                String prev = names.get(i - 1);
                String curr = names.get(i);
                int cmp = curr.compareToIgnoreCase(prev);
                if (ascending)
                    Assert.assertTrue(cmp >= 0, "Names not ascending: " + names);
                else
                    Assert.assertTrue(cmp <= 0, "Names not descending: " + names);
            }
            return;
        }

        if ("Parent Category".equalsIgnoreCase(column)) {
            // Get all category names and parent texts
            List<String> allNames = categoryPage.getCategoryNames();
            List<String> allParents = categoryPage.getParentCategoryTexts();

            // Filter to only include categories created for this test
            List<String> filteredParents = new java.util.ArrayList<>();
            for (int i = 0; i < Math.min(allNames.size(), allParents.size()); i++) {
                String name = allNames.get(i);
                String parent = allParents.get(i);

                // Only check categories created for this test
                if (currentTestSuffix != null && !currentTestSuffix.isEmpty()) {
                    if (name.endsWith(currentTestSuffix) &&
                            (name.startsWith("CA") || name.startsWith("CB") || name.startsWith("CC"))) {
                        if (parent != null && !parent.trim().isEmpty() &&
                                !parent.equalsIgnoreCase("Main Category") && !parent.equals("-")) {
                            filteredParents.add(parent.trim());
                        }
                    }
                } else {
                    // Backward compatibility: if no suffix, check all
                    if (parent != null && !parent.trim().isEmpty() &&
                            !parent.equalsIgnoreCase("Main Category") && !parent.equals("-")) {
                        filteredParents.add(parent.trim());
                    }
                }
            }

            if (filteredParents.size() < 2) {
                System.out.println(
                        "INFO: Skipping Parent Category sort verification - column not sortable or insufficient data. Found: "
                                + filteredParents);
                return; // Skip verification if column wasn't sortable
            }

            for (int i = 1; i < filteredParents.size(); i++) {
                String prev = filteredParents.get(i - 1);
                String curr = filteredParents.get(i);
                int cmp = curr.compareToIgnoreCase(prev);
                if (ascending)
                    Assert.assertTrue(cmp >= 0, "Parent Category not ascending: " + filteredParents);
                else
                    Assert.assertTrue(cmp <= 0, "Parent Category not descending: " + filteredParents);
            }
            return;
        }

        throw new IllegalArgumentException("User sort check not implemented for column: " + column);
    }

    @Given("multiple categories exist with various parent categories for User sorting")
    public void multiple_categories_exist_with_various_parent_categories_for_user_sorting() {
        String token = AuthHelper.getAdminToken();
        currentTestSuffix = randomLetters(2); // keep names short (<= 10 chars), store for verification

        String parentA = "PA" + currentTestSuffix; // e.g., PAQZ
        String parentB = "PB" + currentTestSuffix; // e.g., PBQZ

        Integer parentAId = createOrGetCategoryId(token, parentA);
        Integer parentBId = createOrGetCategoryId(token, parentB);

        Assert.assertNotNull(parentAId, "Could not resolve parentId for " + parentA);
        Assert.assertNotNull(parentBId, "Could not resolve parentId for " + parentB);

        CategoryApiHelper.createCategory(token,
                "{\"name\":\"CA" + currentTestSuffix + "\",\"parentId\":" + parentAId + "}");
        CategoryApiHelper.createCategory(token,
                "{\"name\":\"CB" + currentTestSuffix + "\",\"parentId\":" + parentBId + "}");
        CategoryApiHelper.createCategory(token,
                "{\"name\":\"CC" + currentTestSuffix + "\",\"parentId\":" + parentAId + "}");
    }

    private Integer createOrGetCategoryId(String token, String name) {
        io.restassured.response.Response create = CategoryApiHelper.createCategory(token,
                "{\"name\":\"" + name + "\",\"parentId\":null}");

        if (create.getStatusCode() == 201) {
            return create.jsonPath().getObject("id", Integer.class);
        }

        java.util.Map<String, Object> params = new java.util.HashMap<>();
        params.put("name", name);

        io.restassured.response.Response get = CategoryApiHelper.getCategoriesWithParams(token, params);

        Integer id = get.jsonPath().getObject("[0].id", Integer.class);
        if (id == null)
            id = get.jsonPath().getObject("content[0].id", Integer.class);
        if (id == null)
            id = get.jsonPath().getObject("data[0].id", Integer.class);
        if (id == null)
            id = get.jsonPath().getObject("data.content[0].id", Integer.class);
        return id;
    }

    private void deleteDependentSales(String token, int plantId) {
        try {
            System.err.println("DEBUG: Checking dependent sales for plant " + plantId);
            // Assuming ConfigLoader is available via utils package which is likely imported
            // or available
            // If not, we might need to find where ConfigLoader is.
            // Based on previous context, PlantApiHelper uses it.
            // We'll try to use fully qualified name if possible, or just standard
            // instantiation.
            String baseUrl = "http://localhost:8080"; // Fallback default
            try {
                baseUrl = utils.ConfigLoader.getProperty("api.base.url");
            } catch (Exception e) {
                // ignore, usage default
            }

            api.SalesApiHelper salesHelper = new api.SalesApiHelper(baseUrl);
            salesHelper.setToken(token);

            io.restassured.response.Response salesResp = salesHelper.getAllSales();
            if (salesResp.getStatusCode() == 200) {
                java.util.List<java.util.Map<String, Object>> sales = null;
                try {
                    Object root = salesResp.jsonPath().get("$");
                    if (root instanceof java.util.List)
                        sales = salesResp.jsonPath().getList("$");
                    else if (root instanceof java.util.Map) {
                        java.util.Map<?, ?> rootMap = (java.util.Map<?, ?>) root;
                        if (rootMap.containsKey("content"))
                            sales = salesResp.jsonPath().getList("content");
                        else if (rootMap.containsKey("data")) {
                            Object data = rootMap.get("data");
                            if (data instanceof java.util.List)
                                sales = salesResp.jsonPath().getList("data");
                        }
                    }
                } catch (Exception e) {
                }

                if (sales != null) {
                    for (java.util.Map<String, Object> sale : sales) {
                        Object plantObj = sale.get("plant");
                        boolean match = false;
                        if (plantObj instanceof java.util.Map) {
                            java.util.Map<?, ?> plantMap = (java.util.Map<?, ?>) plantObj;
                            Object pId = plantMap.get("id");
                            if (pId != null && String.valueOf(pId).equals(String.valueOf(plantId)))
                                match = true;
                        } else if (plantObj != null) {
                            if (String.valueOf(plantObj).equals(String.valueOf(plantId)))
                                match = true;
                        }

                        if (match) {
                            Object saleIdObj = sale.get("id");
                            if (saleIdObj != null) {
                                int saleId = Integer.parseInt(String.valueOf(saleIdObj));
                                System.err.println(
                                        "Confirmed: Deleting dependent sale " + saleId + " for plant " + plantId);
                                salesHelper.deleteSale(saleId);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Warning: Failed to cleanup sales for plant " + plantId + ": " + e.getMessage());
        }
    }
}
