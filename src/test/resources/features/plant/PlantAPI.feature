Feature: Plant API Management
  As a User and Admin
  I want to manage plants via API
  So that I can view and update plant details

  Background:
    Given the Plant API is available

  # User Tests
  @User @API @TC-USER-API-PLANT-007
  Scenario: Fetch all plants successfully (TC-USER-API-PLANT-007)
    Given I have a valid "User" token for plant API
    When I send a GET request to plant endpoint "/api/plants"
    Then the plant response status code should be 200
    And the plant response should contain a list of plants

  @User @API @TC-USER-API-PLANT-008
  Scenario: Validate response status code for plants (TC-USER-API-PLANT-008)
    Given I have a valid "User" token for plant API
    When I send a GET request to plant endpoint "/api/plants"
    Then the plant response status code should be 200

  @User @API @TC-USER-API-PLANT-009
  Scenario: Empty plant list handling (TC-USER-API-PLANT-009)
    Given I have a valid "User" token for plant API
    And the plant database is empty
    When I send a GET request to plant endpoint "/api/plants"
    Then the plant response list should be empty

  @User @API @TC-USER-API-PLANT-010
  Scenario: Validate response status code for plant ID (TC-USER-API-PLANT-010)
    Given I have a valid "User" token for plant API
    And a plant with ID 1 exists
    When I send a GET request to plant endpoint "/api/plants/1"
    Then the plant response status code should be 200

  @User @API @TC-USER-API-PLANT-011
  Scenario: Non-existing plant ID (TC-USER-API-PLANT-011)
    Given I have a valid "User" token for plant API
    When I send a GET request to plant endpoint "/api/plants/9999"
    Then the plant response status code should be 404

  @User @API @TC-USER-API-PLANT-012
  Scenario: Fetch plant by valid ID (TC-USER-API-PLANT-012)
    Given I have a valid "User" token for plant API
    And a plant with ID 1 exists
    When I send a GET request to plant endpoint "/api/plants/1"
    Then the plant response status code should be 200
    And the plant response body should contain the plant details

  # Admin Tests - Update
  @Admin @API @TC-ADMIN-API-PLANT-006
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

  @Admin @API @TC-ADMIN-API-PLANT-007
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

  @Admin @API @TC-ADMIN-API-PLANT-008
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

  @Admin @API @TC-ADMIN-API-PLANT-009
  Scenario: Update plant with valid name (TC-ADMIN-API-PLANT-010)
    Given I have a valid "Admin" token for plant API
    And a plant with ID 1 exists
    When I send a PUT request to plant endpoint "/api/plants/1" with body:
        """
        {
          "name": "Palm",
          "price": 20.0,
          "quantity": 10
        }
        """
    Then the plant response status code should be 200
    And the updated plant should have name "Palm"

  @Admin @API @TC-ADMIN-API-PLANT-010
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
    And a plant with ID 1 exists
    When I send a GET request to plant endpoint "/api/plants/1"
    Then the plant response status code should be 200
    And the plant response body should contain the plant details

  @Admin @API @TC-ADMIN-API-PLANT-001
  Scenario: Create plant successfully (TC-ADMIN-API-PLANT-001)
    Given I have a valid "Admin" token for plant API
    When I send a POST request to plant endpoint "/api/plants/category/2" with body:
      """
      {
        "name": "New Plant 001",
        "price": 50.0,
        "quantity": 100,
        "description": "A new test plant"
      }
      """
    Then the plant response status code should be 201

  @Admin @API @TC-ADMIN-API-PLANT-002
  Scenario: Validate required fields (TC-ADMIN-API-PLANT-002)
    Given I have a valid "Admin" token for plant API
    When I send a POST request to plant endpoint "/api/plants/category/3" with body:
      """
      {
        "price": 50.0,
        "quantity": 100
      }
      """
    Then the plant response status code should be 400

  @Admin @API @TC-ADMIN-API-PLANT-003
  Scenario: Duplicate plant name handling (Create) (TC-ADMIN-API-PLANT-003)
    Given I have a valid "Admin" token for plant API
    When I send a POST request to plant endpoint "/api/plants/category/2" with body:
      """
      {
        "name": "Palm",
        "price": 50.0,
        "quantity": 100
      }
      """
    Then the plant response status code should be 400

  @Admin @API @TC-ADMIN-API-PLANT-004
  Scenario: Validate price value (TC-ADMIN-API-PLANT-004)
    Given I have a valid "Admin" token for plant API
    When I send a POST request to plant endpoint "/api/plants/category/3" with body:
      """
      {
        "name": "Invalid Price",
        "price": -10.0,
        "quantity": 100
      }
      """
    Then the plant response status code should be 400

  @Admin @API @TC-ADMIN-API-PLANT-005
  Scenario: Validate quantity value (TC-ADMIN-API-PLANT-005)
    Given I have a valid "Admin" token for plant API
    When I send a POST request to plant endpoint "/api/plants/category/3" with body:
      """
      {
        "name": "Invalid Qty",
        "price": 50.0,
        "quantity": -5
      }
      """
    Then the plant response status code should be 400

  # User Paged Tests
  @User @API @TC-USER-UI-PLANT-001
  Scenario: Fetch paged plants successfully (TC-USER-UI-PLANT-001)
    Given I have a valid "User" token for plant API
    When I send a GET request to plant endpoint "/api/plants/paged?page=0&size=5"
    Then the plant response status code should be 200
    And the response should contain 5 plants

  @User @API @TC-USER-UI-PLANT-002
  Scenario: Search by plant name (TC-USER-UI-PLANT-002)
    Given I have a valid "User" token for plant API
    When I send a GET request to plant endpoint "/api/plants/paged?name=Anthurium"
    Then the plant response status code should be 200

  @User @API @TC-USER-UI-PLANT-003
  Scenario: Search by categoryId (TC-USER-UI-PLANT-003)
    Given I have a valid "User" token for plant API
    When I send a GET request to plant endpoint "/api/plants/paged?categoryId=1"
    Then the plant response status code should be 200

  @User @API @TC-USER-UI-PLANT-004
  Scenario: Combined search filters (TC-USER-UI-PLANT-004)
    Given I have a valid "User" token for plant API
    When I send a GET request to plant endpoint "/api/plants/paged?name=Rose&categoryId=2"
    Then the plant response status code should be 200

  @User @API @TC-USER-UI-PLANT-005
  Scenario: Sort by field ascending (TC-USER-UI-PLANT-005)
    Given I have a valid "User" token for plant API
    When I send a GET request to plant endpoint "/api/plants/paged?sort=name,asc"
    Then the plant response status code should be 200
    And the plants should be sorted by "name" in "asc" order

  @User @API @TC-USER-UI-PLANT-006
  Scenario: Sort by field descending (TC-USER-UI-PLANT-006)
    Given I have a valid "User" token for plant API
    When I send a GET request to plant endpoint "/api/plants/paged?sort=price,desc"
    Then the plant response status code should be 200
    And the plants should be sorted by "price" in "desc" order
