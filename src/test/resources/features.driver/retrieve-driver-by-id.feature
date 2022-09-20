Feature: Retrieve driver by id

  Scenario: Retrieve driver by valid id
    Given db contains an driver with valid id
    And set bearer token
    When client makes a call to GET 'api/drivers' with id
    Then client receives status code 200
    And client id is valid

  Scenario: Retrieve driver by invalid id
    Given set bearer token
    When client makes a call to GET 'api/drivers' with invalid id
    Then client receives status code 404