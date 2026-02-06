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
      | name | flower |
    Then the response status code should be 200
    And the response list should contain categories matching "flower"

  @User @API @TC-USER-API-CAT-004
  Scenario: Search categories by name via API (no results)
    Given I have a valid "User" token
    When I send a GET request to "/api/categories" with params:
      | name | nonexistent |
    Then the response status code should be 200
    And the response list should be empty


  @User @API @TC-USER-API-CAT-005
  Scenario: Attempt to create a category as User (unauthorized)
    Given I have a valid "User" token
    When I send a POST request to "/api/categories" with body:
      """
      {
        "name": "NewCat",
        "parentId": null
      }
      """
    Then the response status code should be 403

  @User @API @TC-ADMIN-API-CAT-006
  Scenario: Attempt to update a category as User (unauthorized)
    Given I have a valid "User" token
    And a category with ID 1 exists
    When I send a PUT request to "/api/categories/1" with body:
         """
      {
        "name": "UpdCat"
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
      | name | CategoryName |
    Then the response status code should be 200
    And the response list should contain categories matching "CategoryName"

  @Admin @API @TC-ADMIN-API-CAT-003
  Scenario: Filter categories by parent via API
    Given I have a valid "Admin" token
    When I send a GET request to "/api/categories" with params:
      | parentId | 10 |
    Then the response status code should be 200
    And all categories in response should have parent_id 10

  @Admin @API @TC-ADMIN-API-CAT-004
  Scenario: Sort categories via API
    Given I have a valid "Admin" token
    When I send a GET request to "/api/categories/page" with params:
      | sortField | id   |
      | sortDir   | desc |
    Then the response status code should be 200
    And the response list should be sorted by "id" ascending
    When I send a GET request to "/api/categories/page" with params:
      | sortField | name  |
      | sortDir   | desc  |
    Then the response list should be sorted by "name" ascending

  @Admin @API @TC-ADMIN-API-CAT-005
  Scenario: Unauthorized access without token
    Given I do not have an authentication token
    When I send a GET request to "/api/categories"
    Then the response status code should be 401

  @Admin @API @TC-ADMIN-API-CAT-007
  Scenario: Verify API Create Category - 201 and correct name
    Given I have a valid "Admin" token
    When I send a POST request to "/api/categories" with body:
      """
      {"name":"Fruits${suffix}","parent":null}
      """
    Then the response status code should be 201
    And the response should contain created category name same as request

  @Admin @API @TC-ADMIN-API-CAT-008
  Scenario: Verify API Edit Category
    Given I have a valid "Admin" token
    And a category with ID 3 exists
    When I send a PUT request to "/api/categories/3" with body:
      """
      {
        "name": "Veggies"
      }
      """
    Then the response status code should be 200
    And category with ID 3 should have name "Veggies"

  @Admin @API @TC-ADMIN-API-CAT-009
  Scenario: Verify API Validation - Name Missing
    Given I have a valid "Admin" token
    When I send a POST request to "/api/categories" with body:
      """
      {"parent":null}
      """
    Then the response status code should be 400
    And the response should contain validation error for missing name

  @Admin @API @TC-ADMIN-API-CAT-010
  Scenario: Verify API Validation - Name Too Short
    Given I have a valid "Admin" token
    When I send a POST request to "/api/categories" with body:
      """
      {"name":"AB"}
      """
    Then the response status code should be 400
    And the response should contain validation error for name too short

  @Admin @API @TC-ADMIN-API-CAT-011
  Scenario: Verify API Validation - Name Too Long
    Given I have a valid "Admin" token
    When I send a POST request to "/api/categories" with body:
      """
      {"name":"Vegetables1"}
      """
    Then the response status code should be 400
    And the response should contain validation error for name too long

  @User @API @TC-USER-API-CAT-006
  Scenario: Filter categories by parent category via API (positive)
    Given I have a valid "User" token
    And a category with parentId 5 exists or I create it with name "Flowers"
    When I send a GET request to "/api/categories" with params:
      | parentId | 5 |
    Then the response status code should be 200
    And the response should contain at least one category
    And all categories in response should have parent_id 5
  @User @API @TC-USER-API-CAT-007
  Scenario: Filter categories by parent category via API (no results)
    Given I have a valid "User" token
    When I send a GET request to "/api/categories" with params:
      | parentId | 12 |
    Then the response status code should be 200
    And the response filtered list should be empty

  @User @API @TC-USER-API-CAT-008
  Scenario: Sort categories by ID via API
    Given I have a valid "User" token
    When I send a GET request to "/api/categories/page" with params:
      | sortField | id  |
      | sortDir   | desc |
    Then the response status code should be 200
    And the response list should be sorted by "id" ascending

  @User @API @TC-USER-API-CAT-009
  Scenario: Sort categories by name via API (page endpoint)
    Given I have a valid "User" token
    When I send a GET request to "/api/categories/page" with params:
      | sortField | name |
      | sortDir   | desc  |
    Then the response status code should be 200
    And the response list should be sorted by "name" ascending

  @User @API @TC-USER-API-CAT-010
  Scenario: Sort categories by parentId via API (page endpoint)
    Given I have a valid "User" token
    When I send a GET request to "/api/categories/page" with params:
      | sortField | parentId |
      | sortDir   | asc  |
    Then the response status code should be 200
    And the response list should be sorted by "parentId" ascending
