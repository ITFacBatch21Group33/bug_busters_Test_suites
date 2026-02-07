Feature: Plant Management UI
  As a User
  I want to view the plant list
  So that I can browse plants in the system

  Background:
    Given the application is running
    And I am on the Login page

  # User UI Tests - Filter Plants
  @User @UI @TC-USER-UI-PLANT-001
  Scenario: Search Plant (TC-USER-UI-PLANT-001)
    Given I login as a "User" for UI
    And plants exist in the system
    When I enter plant name "Daisy" in the search field
    And I click Search
    Then the plant "Daisy" should be displayed in the list

  @User @UI @TC-USER-UI-PLANT-002
  Scenario: Search Invalid Plant (TC-USER-UI-PLANT-002)
    Given I login as a "User" for UI
    And plants exist in the system
    When I enter plant name "InvalidPlant123" in the search field
    And I click Search
    Then I should see a plant "No plants found" message

  @User @UI @TC-USER-UI-PLANT-003
  Scenario: Filter by Category (TC-USER-UI-PLANT-003)
    Given I login as a "User" for UI
    And categories exist in the system
    When I select category "Indoor" from the dropdown
    And I click Apply Filter
    Then only plants in category "Indoor" should be displayed

  @User @UI @TC-USER-UI-PLANT-004
  Scenario: Sort by Name (TC-USER-UI-PLANT-004)
    Given I login as a "User" for UI
    And plants exist in the system
    When I click "Sort by Name"
    Then the plants in the UI should be sorted by "name" in "ascending" order

  @User @UI @TC-USER-UI-PLANT-005
  Scenario: Sort by Price (TC-USER-UI-PLANT-005)
    Given I login as a "User" for UI
    And plants exist in the system
    When I click "Sort by Price"
    Then the plants in the UI should be sorted by "price" in "ascending" order

  @User @UI @TC-USER-UI-PLANT-006
  Scenario: Sort by Quantity (TC-USER-UI-PLANT-006)
    Given I login as a "User" for UI
    And plants exist in the system
    When I click "Sort by Quantity"
    Then the plants in the UI should be sorted by "quantity" in "ascending" order

  # ------------------

  @User @UI @TC-USER-UI-PLANT-007
  Scenario: View Plant List (TC-USER-UI-PLANT-007)
    Given I login as a "User" for UI
    And plants exist in the system
    When I navigate to the Plant Management page
    Then the plant list is displayed

  @User @UI @TC-USER-UI-PLANT-008
  Scenario: Empty Plant List (TC-USER-UI-PLANT-008)
    Given I login as a "User" for UI
    And no plants exist in the system
    When I navigate to the Plant Management page
    Then I should see a plant "No plants found" message


  @User @UI @TC-USER-UI-PLANT-009
  Scenario: Reset Button (TC-USER-UI-PLANT-009)
    Given I login as a "User" for UI
    And plants exist in the system
    When I navigate to the Plant Management page
    And I enter plant name "Rose" in the search field
    And I select category "Indoor" from the dropdown
    And I click "Sort by Price"
    And I click the Reset button
    Then the search field should be cleared
    And all filters should be removed
    And sorting should be reset to default
    And the plant list is displayed

  @User @UI @TC-USER-UI-PLANT-010
  Scenario: Restrict Add Button (TC-USER-UI-PLANT-010)
    Given I login as a "User" for UI
    When I navigate to the Plant Management page
    Then the "Add Plant" button should not be visible

  @User @UI @TC-USER-UI-PLANT-011
  Scenario: Plant List Pagination (TC-USER-UI-PLANT-011)
    Given I login as a "User" for UI
    And enough plants exist for pagination
    When I navigate to the Plant Management page
    Then the "Next" page button should be visible
    When I click the "Next" page button
    Then I should see different plants
    And the "Previous" page button should be visible
    When I click the "Previous" page button
    Then I should see the initial plants

  # Admin UI Tests - Add Plants
  @Admin @UI @TC-ADMIN-UI-PLANT-001
  Scenario: Add Plant Button (TC-ADMIN-UI-PLANT-001)
    Given I login as an "Admin" for UI
    And plants exist in the system
    When I navigate to the Plant Management page
    Then the "Add Plant" button should be visible
    And the "Edit" button should be visible for plants
    And the "Delete" button should be visible for plants

  @Admin @UI @TC-ADMIN-UI-PLANT-002
  Scenario: Open Add Plant Page (TC-ADMIN-UI-PLANT-002)
    Given I login as an "Admin" for UI
    When I click the Add Plant button on the plant page
    Then the Add Plant page should be open

  @Admin @UI @TC-ADMIN-UI-PLANT-003
  Scenario: Mandatory Validation (TC-ADMIN-UI-PLANT-003)
    Given I login as an "Admin" for UI
    And I am on the Add Plant page
    When I leave mandatory fields empty
    And I click the Save button
    Then validation messages should be shown for mandatory fields

  @Admin @UI @TC-ADMIN-UI-PLANT-004
  Scenario: Plant Name Length Validation (TC-ADMIN-UI-PLANT-004)
    Given I login as an "Admin" for UI
    And I am on the Add Plant page
    When I enter a plant name with less than 3 characters
    And I click the Save button
    Then an error message for name length should be displayed
    When I enter a plant name with more than 25 characters
    And I click the Save button
    Then an error message for name length should be displayed
    When I enter a plant name with 10 characters
    And I click the Save button
    Then the plant should be saved successfully

  @Admin @UI @TC-ADMIN-UI-PLANT-005
  Scenario: Add Valid Plant (TC-ADMIN-UI-PLANT-005)
    Given I login as an "Admin" for UI
    And I am on the Add Plant page
    When I enter valid details for the plant
    And I click the Save button
    Then the plant should be added successfully

  @Admin @UI @TC-ADMIN-UI-PLANT-006
  Scenario: Invalid Price (TC-ADMIN-UI-PLANT-006)
    Given I login as an "Admin" for UI
    And I am on the Add Plant page
    When I enter price as 0
    And I click the Save button
    Then a price validation error should be shown

  @Admin @UI @TC-ADMIN-UI-PLANT-007
  Scenario: Invalid Quantity (TC-ADMIN-UI-PLANT-007)
    Given I login as an "Admin" for UI
    And I am on the Add Plant page
    When I enter quantity -1
    And I click the Save button
    Then a quantity validation error should be shown

  @Admin @UI @TC-ADMIN-UI-PLANT-008
  Scenario: Cancel Action (TC-ADMIN-UI-PLANT-008)
    Given I login as an "Admin" for UI
    And I am on the Add Plant page
    When I click the Cancel button
    Then I should be redirected to the plant list

  # -----

  
  @Admin @UI @TC-ADMIN-UI-PLANT-009
  Scenario: Edit Plant – Mandatory Fields (TC-ADMIN-UI-PLANT-009)
    Given I login as an "Admin" for UI
    And plants exist in the system
    When I navigate to the Plant Management page
    And I click the Edit button for a plant
    And I leave mandatory fields empty
    And I click the Save button
    Then validation messages should be shown for mandatory fields

  @Admin @UI @TC-ADMIN-UI-PLANT-010
  Scenario: Edit Plant – Name Length (TC-ADMIN-UI-PLANT-010)
    Given I login as an "Admin" for UI
    And plants exist in the system
    When I navigate to the Plant Management page
    And I click the Edit button for a plant
    And I enter a plant name with less than 3 characters
    And I click the Save button
    Then an error message for name length should be displayed
    When I enter a plant name with more than 25 characters
    And I click the Save button
    Then an error message for name length should be displayed
    When I enter a plant name with 10 characters
    And I click the Save button
    Then the plant should be saved successfully

  @Admin @UI @TC-ADMIN-UI-PLANT-011
  Scenario: Edit Plant – Invalid Price (TC-ADMIN-UI-PLANT-011)
    Given I login as an "Admin" for UI
    And plants exist in the system
    When I navigate to the Plant Management page
    And I click the Edit button for a plant
    And I enter price as 0
    And I click the Save button
    Then a price validation error should be shown

  @Admin @UI @TC-ADMIN-UI-PLANT-012
  Scenario: Edit Plant – Invalid Quantity (TC-ADMIN-UI-PLANT-012)
    Given I login as an "Admin" for UI
    And plants exist in the system
    When I navigate to the Plant Management page
    And I click the Edit button for a plant
    And I enter quantity -1
    And I click the Save button
    Then a quantity validation error should be shown
    When I enter price as -10
    And I click the Save button
    Then a price validation error should be shown

  @Admin @UI @TC-ADMIN-UI-PLANT-013
  Scenario: Cancel Action (TC-ADMIN-UI-PLANT-013)
    Given I login as an "Admin" for UI
    And plants exist in the system
    When I navigate to the Plant Management page
    And I click the Edit button for a plant
    And I click the Cancel button
    Then I should be redirected to the plant list

  @Admin @UI @TC-ADMIN-UI-PLANT-014
  Scenario: Edit Plant - Valid Data (TC-ADMIN-UI-PLANT-014)
    Given I login as an "Admin" for UI
    And plants exist in the system
    When I navigate to the Plant Management page
    And I click the Edit button for a plant
    And I enter plant name "Updated Plant"
    And I enter price as 25.99
    And I enter quantity as 50
    And I click the Save button
    And I navigate to the Plant Management page
    Then the plant list is displayed
