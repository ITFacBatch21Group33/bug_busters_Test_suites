Feature: Plant Management UI
  As a User
  I want to view the plant list
  So that I can browse plants in the system

  Background:
    Given the application is running
    And I am on the Login page

  @User @UI @TC-USER-UI-PLANT-007
  Scenario: View Plant List (TC-USER-UI-PLANT-007)
    Given I login as a "User"
    When I navigate to the Plant Management page
    Then the plant list should be displayed

  @User @UI @TC-USER-UI-PLANT-009
  Scenario: Reset Button (TC-USER-UI-PLANT-009)
    Given plants exist
    When I navigate to the Plant Management page
    And I enter "Aloe" in the search field
    And I select category filter "Garden"
    And I sort by "Name"
    And I click the Reset button
    Then the search field should be cleared
    And the category filter should be reset
    And sorting should be reset to default
    And the full plant list should be displayed

  @User @UI @TC-USER-UI-PlANTS-001
  Scenario: Search Plant (TC-USER-UI-PlANTS-001)
    Given plants exist
    When I navigate to the Plant Management page
    And I enter "Aloe" in the search field
    And I click the Search button
    Then the results list should contain "Aloe"

  @User @UI @TC-USER-UI-PlANTS-002
  Scenario: Search Invalid Plant (TC-USER-UI-PlANTS-002)
    Given plants exist
    When I navigate to the Plant Management page
    And I enter "NonExistentPlantXYZ" in the search field
    And I click the Search button
    Then I should see a "No plants found" message in the results

  @User @UI @TC-USER-UI-PlANTS-003
  Scenario: Filter by Category (TC-USER-UI-PlANTS-003)
    Given categories exist
    And plants exist
    When I navigate to the Plant Management page
    And I select category filter "Garden"
    Then only plants belonging to "Garden" are displayed

  @User @UI @TC-USER-UI-PLANT-010
  Scenario: Restrict Add Button (TC-USER-UI-PLANT-010)
    Given I login as a "User"
    When I navigate to the Plant Management page
    Then I should not see "Add Plant" button

  @User @UI @TC-USER-UI-PLANT-008
  Scenario: Empty Plant List (TC-USER-UI-PLANT-008)
    Given I login as a "User"
    And no plants exist in the system
    When I navigate to the Plant Management page
    Then I should see a "No plants found" message in the results

  @User @UI @TC-USER-UI-PLANT-011
  Scenario: Plant List Pagination (TC-USER-UI-PLANT-011)
    Given I login as a "User"
    And more plants exist than fit on one page
    When I navigate to the Plant Management page
    Then I should see pagination controls
    When I click the Next page button
    Then correct plants displayed for selected page
    When I click the Previous page button
    Then correct plants displayed for selected page
    When I click page number 2
    Then correct plants displayed for selected page

  @User @UI @TC-USER-UI-PLANT-012
  Scenario: Sort Plants by Name (TC-USER-UI-PLANT-012)
    Given plants exist
    When I navigate to the Plant Management page
    And I sort by "Name"
    Then the plant list should be sorted by "Name" in ascending order

  @User @UI @TC-USER-UI-PlANTS-004
  Scenario: Sort Plants by Name (TC-USER-UI-PlANTS-004)
    Given plants exist
    When I navigate to the Plant Management page
    And I sort by "Name"
    Then the plant list should be sorted by "Name" in ascending order

  @User @UI @TC-USER-UI-PlANTS-005
  Scenario: Sort Plants by Price (TC-USER-UI-PlANTS-005)
    Given plants exist
    When I navigate to the Plant Management page
    And I sort by "Price"
    Then the plant list should be sorted by "Price" in ascending order

  @User @UI @TC-USER-UI-PlANTS-006
  Scenario: Sort Plants by Quantity (TC-USER-UI-PlANTS-006)
    Given plants exist
    When I navigate to the Plant Management page
    And I sort by "Quantity"
    Then the plant list should be sorted by "Quantity" in ascending order

  @Admin @UI @TC-ADMIN-UI-PLANT-009
  Scenario: Edit Plant - Mandatory Fields (TC-ADMIN-UI-PLANT-009)
    Given I login as an "Admin"
    And a plant with name "Test Plant" exists
    When I navigate to the Plant Management page
    And I click Edit for plant "Test Plant"
    And I clear mandatory fields on the Edit Plant page
    And I click Save on the Edit Plant page
    Then I should see validation messages for mandatory fields

  @Admin @UI @TC-ADMIN-UI-PLANT-010
  Scenario: Edit Plant - Name Length (TC-ADMIN-UI-PLANT-010)
    Given I login as an "Admin"
    And a plant with name "Test Plant" exists
    When I navigate to the Plant Management page
    And I click Edit for plant "Test Plant"
    # Name too short (< 3 chars)
    When I enter plant name "AB" on the Edit Plant page
    And I click Save on the Edit Plant page
    Then I should see name validation message
    # Name too long (> 25 chars)
    When I enter plant name "ThisIsAVeryLongPlantNameExceedingLimit" on the Edit Plant page
    And I click Save on the Edit Plant page
    Then I should see name validation message
    # Valid length (3-25 chars)
    When I enter plant name "Valid Plant" on the Edit Plant page
    And I click Save on the Edit Plant page
    Then I should not see name validation message

  @Admin @UI @TC-ADMIN-UI-PLANT-011
  Scenario: Edit Plant - Invalid Price (TC-ADMIN-UI-PLANT-011)
    Given I login as an "Admin"
    And a plant with name "Test Plant" exists
    When I navigate to the Plant Management page
    And I click Edit for plant "Test Plant"
    # Price zero (invalid)
    When I enter plant price "0" on the Edit Plant page
    And I click Save on the Edit Plant page
    Then I should see price validation message
    # Price negative (invalid)
    When I enter plant price "-5" on the Edit Plant page
    And I click Save on the Edit Plant page
    Then I should see price validation message
    # Valid positive price
    When I enter plant price "10.50" on the Edit Plant page
    And I click Save on the Edit Plant page
    Then I should not see price validation message

  @Admin @UI @TC-ADMIN-UI-PLANT-012
  Scenario: Edit Plant - Invalid Quantity (TC-ADMIN-UI-PLANT-012)
    Given I login as an "Admin"
    And a plant with name "Test Plant" exists
    When I navigate to the Plant Management page
    And I click Edit for plant "Test Plant"
    # Quantity negative (invalid)
    When I enter plant quantity "-1" on the Edit Plant page
    And I click Save on the Edit Plant page
    Then I should see quantity validation message
    # Valid quantity (non-negative)
    When I enter plant quantity "5" on the Edit Plant page
    And I click Save on the Edit Plant page
    Then I should not see quantity validation message

  @Admin @UI @TC-ADMIN-UI-PLANT-013
  Scenario: Edit Plant - Cancel Action (TC-ADMIN-UI-PLANT-013)
    Given I login as an "Admin"
    And a plant with name "Test Plant" exists
    When I navigate to the Plant Management page
    And I click Edit for plant "Test Plant"
    When I click Cancel on the Edit Plant page
    Then I should be redirected to the Plant Management page

  @Admin @UI @TC-ADMIN-UI-PLANT-014
  Scenario: Edit Plant - Valid data (TC-ADMIN-UI-PLANT-014)
    Given I login as an "Admin"
    And a plant with name "Test Plant" exists
    When I navigate to the Plant Management page
    And I click Edit for plant "Test Plant"
    When I enter plant name "Edited Plant" on the Edit Plant page
    And I enter plant price "12.00" on the Edit Plant page
    And I enter plant quantity "10" on the Edit Plant page
    And I click Save on the Edit Plant page
    Then I should see plant named "Edited Plant" in the list

  @Admin @UI @TC-ADMIN-UI-PLANTS-001
  Scenario: Add/Edit/Delete Buttons Visible (TC-ADMIN-UI-PLANTS-001)
    Given I login as an "Admin"
    When I navigate to the Plant Management page
    Then the Add Plant button should be visible
    And edit and delete actions should be visible for plants

  @Admin @UI @TC-ADMIN-UI-PLANTS-002
  Scenario: Open Add Plant Page (TC-ADMIN-UI-PLANTS-002)
    Given I login as an "Admin"
    When I navigate to the Plant Management page
    And I click Add Plant
    Then the Add Plant page should be displayed

  @Admin @UI @TC-ADMIN-UI-PLANTS-003
  Scenario: Add Plant - Mandatory Validation (TC-ADMIN-UI-PLANTS-003)
    Given I login as an "Admin"
    When I navigate to the Plant Management page
    And I click Add Plant
    And I clear mandatory fields on the Add Plant page
    And I click Save on the Add Plant page
    Then I should see validation messages for mandatory fields on the Add Plant page

  @Admin @UI @TC-ADMIN-UI-PLANTS-004
  Scenario: Add Plant - Name Length Validation (TC-ADMIN-UI-PLANTS-004)
    Given I login as an "Admin"
    When I navigate to the Plant Management page
    And I click Add Plant
    # Name too short (< 3 chars)
    When I enter plant name "AB" on the Add Plant page
    And I click Save on the Add Plant page
    Then I should see name validation message on the Add Plant page
    # Name too long (> 25 chars)
    When I enter plant name "ThisIsAVeryLongPlantNameExceedingLimit" on the Add Plant page
    And I click Save on the Add Plant page
    Then I should see name validation message on the Add Plant page
    # Valid name (3-25 chars)
    When I enter plant name "Valid Plant" on the Add Plant page
    And I click Save on the Add Plant page
    Then I should not see name validation message on the Add Plant page
    And I should see plant named "Valid Plant" in the list

  @Admin @UI @TC-ADMIN-UI-PLANTS-005
  Scenario: Add Plant - Valid data (TC-ADMIN-UI-PLANTS-005)
    Given I login as an "Admin"
    When I navigate to the Plant Management page
    And I click Add Plant
    And I enter plant name "New Plant" on the Add Plant page
    And I enter plant price "9.99" on the Add Plant page
    And I enter plant quantity "5" on the Add Plant page
    And I click Save on the Add Plant page
    Then I should see plant named "New Plant" in the list

  @Admin @UI @TC-ADMIN-UI-PLANTS-006
  Scenario: Add Plant - Invalid Price (TC-ADMIN-UI-PLANTS-006)
    Given I login as an "Admin"
    When I navigate to the Plant Management page
    And I click Add Plant
    When I enter plant price "0" on the Add Plant page
    And I click Save on the Add Plant page
    Then I should see price validation message on the Add Plant page

  @Admin @UI @TC-ADMIN-UI-PLANTS-007
  Scenario: Add Plant - Invalid Quantity (TC-ADMIN-UI-PLANTS-007)
    Given I login as an "Admin"
    When I navigate to the Plant Management page
    And I click Add Plant
    When I enter plant quantity "-1" on the Add Plant page
    And I click Save on the Add Plant page
    Then I should see quantity validation message on the Add Plant page

  @Admin @UI @TC-ADMIN-UI-PLANTS-008
  Scenario: Add Plant - Cancel Action (TC-ADMIN-UI-PLANTS-008)
    Given I login as an "Admin"
    When I navigate to the Plant Management page
    And I click Add Plant
    When I click Cancel on the Add Plant page
    Then I should be redirected to the Plant Management page


