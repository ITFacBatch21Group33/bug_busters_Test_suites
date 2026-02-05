Feature: Plant API Management
  As a User and Admin
  I want to manage plants via API
  So that I can view and update plant details

  Background:
    Given the Plant API is available

  # User Tests
  Scenario: Fetch all plants successfully (TC-USER-API-PLANT-006)
    Given I have no authentication token for plant API
    When I send a GET request to plant endpoint "/api/plants"
    Then the plant response status code should be 200
    And the plant response should contain a list of plants


  Scenario: Validate response status code for plants (TC-USER-API-PLANT-007)
    Given I have no authentication token for plant API
    When I send a GET request to plant endpoint "/api/plants"
    Then the plant response status code should be 200

  Scenario: Empty plant list handling (TC-USER-API-PLANT-008)
    Given the plant database is empty
    When I send a GET request to plant endpoint "/api/plants"
    Then the plant response list should be empty

  Scenario: Fetch plant by valid ID (TC-USER-API-PLANT-011)
    Given I have a valid "User" token for plant API
    And a plant with ID 1 exists
    When I send a GET request to plant endpoint "/api/plants/1"
    Then the plant response status code should be 200
    And the plant response body should contain the plant details

  Scenario: Non-existing plant ID (TC-USER-API-PLANT-010)
    Given I have no authentication token for plant API
    When I send a GET request to plant endpoint "/api/plants/9999"
    Then the plant response status code should be 404

  # Admin Tests - Update
  Scenario: Update plant successfully (TC-ADMIN-API-PLANT-007)
    Given I have a valid "Admin" token for plant API
    And a plant with ID 1 exists
    When I send a PUT request to plant endpoint "/api/plants/1" with body:
      """
      {
        "name": "Updated Plant",
        "price": 25.0,
        "quantity": 50
      }
      """
    Then the plant response status code should be 200
    And the plant response should contain the updated plant details

  Scenario: Validate price value must be positive (TC-ADMIN-API-PLANT-008)
    Given I have a valid "Admin" token for plant API
    When I send a PUT request to plant endpoint "/api/plants/1" with body:
      """
      {
        "name": "Invalid Price Plant",
        "price": -10.0,
        "quantity": 50
      }
      """
    Then the plant response status code should be 400

  Scenario: Validate quantity value cannot be negative (TC-ADMIN-API-PLANT-009)
    Given I have a valid "Admin" token for plant API
    When I send a PUT request to plant endpoint "/api/plants/1" with body:
      """
      {
        "name": "Invalid Qty Plant",
        "price": 20.0,
        "quantity": -5
      }
      """
    Then the plant response status code should be 400

  Scenario: Duplicate plant name handling (TC-ADMIN-API-PLANT-010)
    Given I have a valid "Admin" token for plant API
    When I send a PUT request to plant endpoint "/api/plants/1" with body:
      """
      {
        "name": "Existing Plant Name",
        "price": 20.0,
        "quantity": 10
      }
      """
    Then the plant response status code should be 409

  Scenario: Invalid plant ID for update (TC-ADMIN-API-PLANT-011)
    Given I have a valid "Admin" token for plant API
    When I send a PUT request to plant endpoint "/api/plants/9999" with body:
      """
      {
        "name": "Non-existent Plant",
        "price": 20.0,
        "quantity": 10
      }
      """
    Then the plant response status code should be 404


  
  @User @API @TC-USER-API-PlANTS-001
  Scenario: Create plant successfully (TC-USER-API-PLANTS-001)
    Given I have a valid "User" token for plant API
    And a valid category with ID 3 exists
    When I send a POST request to plant endpoint "/api/plants/category/3" with body:
      """
      {
        "name": "New Plant",
        "price": 15.5,
        "quantity": 10
      }
      """
    Then the plant response status code should be 201
    And the plant response body should contain the plant details

  @User @API @TC-USER-API-PlANTS-003
  Scenario: Duplicate plant name handling (TC-USER-API-PLANTS-003)
    Given I have a valid "User" token for plant API
    And a plant with name "Existing Plant" exists
    When I send a POST request to plant endpoint "/api/plants/category/3" with body:
      """
      {
        "name": "Existing Plant",
        "price": 15.5,
        "quantity": 10
      }
      """
    Then the plant response status code should be 409
    And the plant response body should contain a conflict error

  @User @API @TC-USER-API-PlANTS-002
  Scenario: Validate required fields (TC-USER-API-PLANTS-002)
    Given I have a valid "User" token for plant API
    And a valid category with ID 3 exists
    When I send a POST request to plant endpoint "/api/plants/category/3" with body:
      """
      {
        "price": 15.5,
        "quantity": 10
      }
      """
    Then the plant response status code should be 400
    And the plant response body should contain a validation error
