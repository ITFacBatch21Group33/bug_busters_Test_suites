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

  @Admin @UI @TC-ADMIN-UI-CAT-005
  Scenario: Search yields no results for Admin
    Given I login as an "Admin"
    And a category with name "Dummy" exists
    When I navigate to the Categories page
    And I search for "XYZRandomString"
    Then I should see a "No categories found" message in the results