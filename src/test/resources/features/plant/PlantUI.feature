Feature: Plant UI Management
  As a User and Admin
  I want to interact with the Plant Management UI

  Background:
    Given I have navigated to the Application

  # User UI Tests
  Scenario: View Plant List (TC-USER-UI-PLANT-007)
    Given I login as a "User" for UI
    And I navigate to the Plant Management page
    Then the plant list should be displayed

  Scenario: Empty Plant List (TC-USER-UI-PLANT-008)
    Given I login as a "User" for UI
    And there are no plants in the system
    When I navigate to the Plant Management page
    Then I should see a plant "No plants found" message

  Scenario: Reset Button Functionality (TC-USER-UI-PLANT-009)
    Given I login as a "User" for UI
    And I navigate to the Plant Management page
    And I enter text in the search field
    When I click the Reset button
    Then the search field should be cleared
    And the full plant list should be displayed

  Scenario: Restrict Add Button for User (TC-USER-UI-PLANT-010)
    Given I login as a "User" for UI
    When I navigate to the Plant Management page
    Then the Add Plant button should not be visible

  Scenario: Plant List Pagination (TC-USER-UI-PLANT-011)
    Given I login as a "User" for UI
    And I navigate to the Plant Management page
    Then I should see pagination controls
    When I click the Next page button
    Then the next page of plants should be displayed
    When I click the Previous page button
    Then the previous page of plants should be displayed

  # Admin UI Tests - Edit
  Scenario: Edit Plant - Mandatory Fields (TC-ADMIN-UI-PLANT-009)
    Given I login as an "Admin" for UI
    And I navigate to the Plant Management page
    When I click edit on a plant
    And I clear mandatory fields
    And I click the Save button
    Then validation messages should be shown for mandatory fields

  Scenario: Edit Plant - Name Length (TC-ADMIN-UI-PLANT-010)
    Given I login as an "Admin" for UI
    And I navigate to the Plant Management page
    When I click edit on a plant
    And I enter a plant name with less than 3 characters
    And I click the Save button
    Then an error message for name length should be displayed
    When I enter a plant name with more than 25 characters
    And I click the Save button
    Then an error message for name length should be displayed

  Scenario: Edit Plant - Invalid Price (TC-ADMIN-UI-PLANT-011)
    Given I login as an "Admin" for UI
    And I navigate to the Plant Management page
    When I click edit on a plant
    And I update price to -10
    And I click the Save button
    Then a price validation error should be shown

  Scenario: Edit Plant - Invalid Quantity (TC-ADMIN-UI-PLANT-012)
    Given I login as an "Admin" for UI
    And I navigate to the Plant Management page
    When I click edit on a plant
    And I update quantity to -1
    And I click the Save button
    Then a quantity validation error should be shown

  Scenario: Cancel Edit Action (TC-ADMIN-UI-PLANT-013)
    Given I login as an "Admin" for UI
    And I navigate to the Plant Management page
    When I click edit on a plant
    And I click the Cancel button
    Then I should be redirected to the plant list

  Scenario: Edit Plant - Valid Data (TC-ADMIN-UI-PLANT-006)
    Given I login as an "Admin" for UI
    And I navigate to the Plant Management page
    When I click edit on a plant
    And I enter valid details for the plant
    And I click the Save button
    Then the plant should be edited successfully
