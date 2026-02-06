Feature: Category Management UI
  As a User and Admin
  I want to interact with the Category List page
  So that I can view, search, and manage categories

  Background:
    Given the application is running
    And I am on the Login page

  @User @UI @TC-USER-UI-CAT-001
  Scenario: Verify pagination on Category List page
    Given I login as a "User"
    And more categories exist than fit on one page
    When I navigate to the Categories page
    Then I should see pagination controls
    When I click the "Next" button
    Then I should see the next page of categories

  @User @UI @TC-USER-UI-CAT-002
  Scenario: Verify "No category found" message
    Given I login as a "User"
    And no categories exist in the system
    When I navigate to the Categories page
    Then I should see a "No category found" message

  @User @UI @TC-USER-UI-CAT-003
  Scenario Outline: Search for an existing category by name
    Given I login as a "User"
    And a category with name "<categoryName>" exists
    When I navigate to the Categories page
    And I search for "<searchTerm>"
    Then the results list should contain "<categoryName>"

    Examples:
      | categoryName | searchTerm |
      | Web          | Web        |
      | Garden       | Gard       |

  @User @UI @TC-USER-UI-CAT-004
  Scenario: Search for a non-existing category name
    Given I login as a "User"
    And a category with name "Dummy" exists
    When I navigate to the Categories page
    And I search for "NonExistentCategoryXYZ"
    Then I should see a "No categories found" message in the results

  @User @UI @TC-USER-UI-CAT-005
  Scenario: Verify access control for User role
    Given I login as a "User"
    When I navigate to the Categories page
    Then I should not see "Add Category" button
    And I should not see "Edit" options
    And I should not see "Delete" options

  @Admin @UI @TC-ADMIN-UI-CAT-001
  Scenario: Verify default category list pagination for Admin
    Given I login as an "Admin"
    And more categories exist than fit on one page
    When I navigate to the Categories page
    Then the first page should display the default number of items
    When I click the "Next" button
    Then I should see the second page of categories
    When I click the "Previous" button
    Then I should see the first page of categories

  @Admin @UI @TC-ADMIN-UI-CAT-002
  Scenario: Search categories by name as Admin
    Given I login as an "Admin"
    And a category with name "Indoor" exists
    When I navigate to the Categories page
    And I search for "Indoor"
    Then the results list should contain "Indoor"

  @Admin @UI @TC-ADMIN-UI-CAT-003
  Scenario: Filter categories by parent
    Given I login as an "Admin"
    And a parent category "Web" exists with child categories
    When I navigate to the Categories page
    And I filter by parent category "Web"
    Then I should see only categories belonging to "Web"

  @Admin @UI @TC-ADMIN-UI-CAT-004
  Scenario: Sort categories by columns
    Given I login as an "Admin"
    And multiple categories exist
    When I navigate to the Categories page
    And I sort by "ID" "ascending"
    Then the list should be sorted by "ID" in "ascending" order
    When I sort by "Name" "descending"
    Then the list should be sorted by "Name" in "descending" order
    When I sort by "Parent Category" "ascending"
    Then the list should be sorted by "Parent Category" in "ascending" order
  
  @Admin @UI @TC-ADMIN-UI-CAT-006
  Scenario: Verify successful creation of a Main Category
    Given I login as an "Admin"
    And category with name "Herbs" does not exist
    And I am on the Add Category page
    When I enter "Herbs" in the "Category Name" field
    And I leave "Parent Category" empty
    And I click "Save" on the Add Category page
    Then I should be redirected to the Categories page
    And "Herbs" should appear in the list as a Main Category
  
  @Admin @UI @TC-ADMIN-UI-CAT-007
  Scenario: Verify validation for empty Category Name
    Given I login as an "Admin"
    And I am on the Add Category page
    When I leave the "Category Name" field blank
    And I click "Save" on the Add Category page
    Then the category should not be saved
    And an error message "Category name is required" should be displayed below the "Category Name" field
    And an error message "Category name must be between 3 and 10 characters" should be displayed below the "Category Name" field

  @Admin @UI @TC-ADMIN-UI-CAT-008
  Scenario: Verify validation for Category Name length (Minimum Boundary)
    Given I login as an "Admin"
    And I am on the Add Category page
    When I enter "AB" in the "Category Name" field
    And I click "Save" on the Add Category page
    Then the category should not be saved
    And an error message "Category name must be between 3 and 10 characters" should be displayed below the "Category Name" field

  @Admin @UI @TC-ADMIN-UI-CAT-009
  Scenario: Verify validation for Category Name length (Maximum Boundary)
    Given I login as an "Admin"
    And I am on the Add Category page
    When I enter "Sunflowers1" in the "Category Name" field
    And I click "Save" on the Add Category page
    Then the category should not be saved
    And an error message "Category name must be between 3 and 10 characters" should be displayed below the "Category Name" field

  @Admin @UI @TC-ADMIN-UI-CAT-010
  Scenario: Verify Cancel button functionality on Add Category page
    Given I login as an "Admin"
    And category with name "CancelCat01" does not exist
    And I am on the Add Category page
    When I enter "CancelCat01" in the "Category Name" field
    And I leave "Parent Category" empty
    And I click "Cancel" on the Add Category page
    Then I should be redirected to the Categories page
    And category "CancelCat01" should not exist in the list

  @User @UI @TC-USER-UI-CAT-006
  Scenario: Filter categories by an existing parent category as User
    Given I login as a "User"
    And a parent category "Web" exists or is created with child categories
    When I navigate to the Categories page
    And I filter by parent category "Web"
    Then I should see only categories belonging to "Web"

  @User @UI @TC-USER-UI-CAT-007
  Scenario: Filter categories by a parent category with no children as User
    Given I login as a "User"
    And a parent category "CatNoCh{rand}" exists with no child categories
    When I navigate to the Categories page
    And I apply the parent category filter "CatNoCh{rand}"
    Then I should see a no results message in the results
    And the results list should be empty

  @User @UI @TC-USER-UI-CAT-008
  Scenario: Sort category list by ID as User
    Given I login as a "User"
    And multiple categories exist for User sorting
    When I navigate to the Categories page
    And I sort by "ID" "ascending" as a User
    Then as a User the list should be sorted by "ID" in "ascending" order

  @User @UI @TC-USER-UI-CAT-009
  Scenario: Sort category list by Name as User
    Given I login as a "User"
    And multiple categories exist for User sorting
    When I navigate to the Categories page
    And I sort by "Name" "ascending" as a User
    Then as a User the list should be sorted by "Name" in "ascending" order

  @User @UI @TC-USER-UI-CAT-010
  Scenario: Sort category list by Parent Category as User
    Given I login as a "User"
    And multiple categories exist with various parent categories for User sorting
    When I navigate to the Categories page
    And I sort by "Parent Category" "ascending" as a User
    Then as a User the list should be sorted by "Parent Category" in "ascending" order
