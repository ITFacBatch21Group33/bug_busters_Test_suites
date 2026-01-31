Feature: Category Management API
  As a User and Admin
  I want to interact with the Category API
  So that I can retrieve and manage category data programmatically

  Background:
    Given the API is available

  @User @API @TC-USER-API-CAT-001
  Scenario: Retrieve all categories list
    Given I have a valid "User" token
    When I send a GET request to "/api/categories"
    Then the response status code should be 200
    And the response should contain a list of categories

  @User @API @TC-USER-API-CAT-002
  Scenario: Retrieve categories with pagination parameters
    Given I have a valid "User" token
    When I send a GET request to "/api/categories" with params:
      | page | 2 |
      | size | 5 |
    Then the response status code should be 200
    And the response should contain a paginated list of categories

  @User @API @TC-USER-API-CAT-003
  Scenario: Search categories by name via API
    Given I have a valid "User" token
    When I send a GET request to "/api/categories" with params:
      | search | flower |
    Then the response status code should be 200
    And the response list should contain categories matching "flower"

  @User @API @TC-USER-API-CAT-004
  Scenario: Search categories by name via API (no results)
    Given I have a valid "User" token
    When I send a GET request to "/api/categories" with params:
      | search | nonexistent |
    Then the response status code should be 200
    And the response list should be empty

  @User @API @TC-USER-API-CAT-005
  Scenario: Attempt to create a category as User (unauthorized)
    Given I have a valid "User" token
    When I send a POST request to "/api/categories" with body:
      """
      {
        "name": "New Category",
        "parent_id": null
      }
      """
    Then the response status code should be 403

  @User @API @TC-USER-API-CAT-006
  Scenario: Attempt to update a category as User (unauthorized)
    Given I have a valid "User" token
    And a category with ID 1 exists
    When I send a PUT request to "/api/categories/1" with body:
         """
      {
        "name": "Updated Category"
      }
      """
    Then the response status code should be 403

  @Admin @API @TC-ADMIN-API-CAT-001
  Scenario: List categories with pagination for Admin
    Given I have a valid "Admin" token
    When I send a GET request to "/api/categories" with params:
      | page | 2 |
    Then the response status code should be 200
    And the response should contain a paginated list of categories

  @Admin @API @TC-ADMIN-API-CAT-002
  Scenario: Same as User search but for Admin
    Given I have a valid "Admin" token
    When I send a GET request to "/api/categories" with params:
      | search | CategoryName |
    Then the response status code should be 200
    And the response list should contain categories matching "CategoryName"

  @Admin @API @TC-ADMIN-API-CAT-003
  Scenario: Filter categories by parent via API
    Given I have a valid "Admin" token
    When I send a GET request to "/api/categories" with params:
      | parent_id | 10 |
    Then the response status code should be 200
    And all categories in response should have parent_id 10

  @Admin @API @TC-ADMIN-API-CAT-004
  Scenario: Sort categories via API
    Given I have a valid "Admin" token
    When I send a GET request to "/api/categories" with params:
      | sort | id |
    Then the response status code should be 200
    And the response list should be sorted by "id" ascending
    When I send a GET request to "/api/categories" with params:
      | sort | name |
    Then the response list should be sorted by "name" ascending

  @Admin @API @TC-ADMIN-API-CAT-005
  Scenario: Unauthorized access without token
    Given I do not have an authentication token
    When I send a GET request to "/api/categories"
    Then the response status code should be 401
