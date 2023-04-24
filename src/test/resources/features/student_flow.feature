Feature: Student flow
  A user should be able list all courses and have only the read access
   
  Background: A user authorized as student
    Given a user entered student credentials on the login page
    When the user clicks the SignIn button on the login page
    Then the user goes to the home page
  
  Scenario: A user with student authorities has access to the courses list page
    When the user clicks the courses button
    * the user goes to the courses list page
  
  Scenario: A user with student authorities has no access to create a course
    Given a user sees the courses list page
    When the user clicks the create button
    Then the user enters a course name on the create course panel
    * the user enters a course desctiption on the create course panel
    * the user clicks the save changes button on the create course panel
    * the user request is denied
  
  Scenario: A user with student authorities tries to receive a course  
    Given a user sees the courses list page
    When the user clicks an existence course name link
    Then the user request is denied
    
  Scenario: A user with student authorities has no access to update a course
    Given a user sees the courses list page
    When the user clicks an existence course name link   
    Then the user request is denied
    
  Scenario: A user with student authorities has no access to delete a course
    Given a user sees the courses list page
    When the user clicks the delete course button of existence course
    Then the user request is denied