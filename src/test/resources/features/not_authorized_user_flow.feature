Feature: Not authorized user flow
  The actions that can be done by a not authorized user

  Scenario: A user navigates to the Login page
    Given a not authorized user has connection to the home page
    When the user clicks the SignIn button on the home page
    Then the user goes to the login page