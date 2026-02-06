Feature: Sales Management UI
  As a User and Admin
  I want to interact with the Sales List page
  So that I can view, search, and manage sales

  Background:
    Given the application is running
    And I am on the Login page

  @Admin @UI @TC-ADMIN-UI-SALES-001
  Scenario: Admin can view sales list
    Given I login as an "Admin"
    When I navigate to the sales page
    Then I should see the sales list page
    And I should see sales records

  @Admin @UI @TC-ADMIN-UI-SALES-002
  Scenario: Admin sees Sell Plant button
    Given I login as an "Admin"
    When I navigate to the sales page
    Then I should see the Sell Plant button

  @Admin @UI @TC-ADMIN-UI-SALES-003
  Scenario: Admin accesses Sell Plant page
    Given I login as an "Admin"
    And a plant with name "A03" exists
    When I navigate to the sales page
    And I click the Sell Plant button
    Then I should see the Create New Sell Plant page

  @Admin @UI @TC-ADMIN-UI-SALES-004
  Scenario: Admin creates sale successfully
    Given I login as an "Admin"
    And a plant with name "A03" exists
    When I navigate to the sales page
    And I click the Sell Plant button
    And I select plant "A03"
    And I enter quantity "5"
    And I click the Sell button
    Then the sale should be created successfully

  @Admin @UI @TC-ADMIN-UI-SALES-005
  Scenario: Admin deletes sale
    Given I login as an "Admin"
    And a sale record exists
    When I navigate to the sales page
    And I click the delete button for a sale
    And I confirm the deletion
    Then the sale should be deleted successfully

  @User @UI @TC-USER-UI-SALES-001
  Scenario: User can sort sales list
    Given I login as a "User"
    And multiple sales exist
    When I navigate to the sales page
    And I click on the Plant column header
    Then the sales list should be sorted by plant name

  @User @UI @TC-USER-UI-SALES-002
  Scenario: User cannot see Sell Plant button
    Given I login as a "User"
    When I navigate to the sales page
    Then I should not see the Sell Plant button

  @User @UI @TC-USER-UI-SALES-003
  Scenario: User cannot access Sell Plant page
    Given I login as a "User"
    When I navigate to the sell plant page directly
    Then I should see an access denied page

  @User @UI @TC-USER-UI-SALES-004
  Scenario: User cannot delete sale
    Given I login as a "User"
    And a sale record exists
    When I navigate to the sales page
    Then I should not see the delete option

  @User @UI @TC-USER-UI-SALES-005
  Scenario: User sees empty sales message
    Given I login as a "User"
    And no sales exist in the system
    When I navigate to the sales page
    Then I should see a no sales found message