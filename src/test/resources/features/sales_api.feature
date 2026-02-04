Feature: Sales Management API

  @Admin @API @TC-ADMIN-API-SALES-001
  Scenario: Create sale with valid data
    Given the API is available
    Given I have a valid "Admin" token for sales API
    When I send a POST request to create a sale for plant 1 with quantity 5
    Then the sales API response status code should be 201
    And the sales API response should contain sale details

  @Admin @API @TC-ADMIN-API-SALES-002
  Scenario: Create sale with invalid quantity
    Given the API is available
    Given I have a valid "Admin" token for sales API
    When I send a POST request to create a sale for plant 1 with invalid quantity 0
    Then the sales API response status code should be 400
    And the sales API response should contain error message

  @Admin @API @TC-ADMIN-API-SALES-003
  Scenario: Create sale with insufficient stock
    Given the API is available
    Given I have a valid "Admin" token for sales API
    When I send a POST request to create a sale for plant 1 with quantity 99999
    Then the sales API response status code should be 400
    And the sales API response should contain error message

  @Admin @API @TC-ADMIN-API-SALES-004
  Scenario: Delete sale successfully
    Given the API is available
    Given I have a valid "Admin" token for sales API
    When I send a DELETE request to delete sale with id 1
    Then the sales API response status code should be 204
    And the sales API response should have no content

  @Admin @API @TC-ADMIN-API-SALES-005
  Scenario: Get all sales list
    Given the API is available
    Given I have a valid "Admin" token for sales API
    When I send a GET request to retrieve all sales
    Then the sales API response status code should be 200
    And the sales API response should contain sales list

  @User @API @TC-USER-API-SALES-001
  Scenario: User sort the sales list
    Given the API is available
    Given I have a valid "User" token for sales API
    When I send a GET request to retrieve sales with page 0 size 10 sorted by "plantName"
    Then the sales API response status code should be 200
    And the sales API response should contain sales list

  @User @API @TC-USER-API-SALES-002
  Scenario: User sale creation blocked
    Given the API is available
    Given I have a valid "User" token for sales API
    When I send a POST request to create a sale for plant 1 with quantity 5
    Then the sales API response status code should be 403

  @User @API @TC-USER-API-SALES-003
  Scenario: User cannot delete sale
    Given the API is available
    Given I have a valid "User" token for sales API
    When I send a DELETE request to delete sale with id 1
    Then the sales API response status code should be 403

  @User @API @TC-USER-API-SALES-004
  Scenario: Unauthorized access to sales API
    Given the API is available
    Given I have no authentication token for sales API
    When I send a GET request to retrieve all sales
    Then the sales API response status code should be 401

  @User @API @TC-USER-API-SALES-005
  Scenario: Get a specific sale details
    Given the API is available
    Given I have a valid "User" token for sales API
    When I send a GET request to retrieve sale with id 1
    Then the sales API response status code should be 200
    And the sales API response should contain sale details