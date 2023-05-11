Feature: Student flow
  A user should be able list all courses and have only the read access
  A user should be able to list all groups information (read access)
   
  Background: A user authorized as student
    Given a user entered student credentials on the login page
    When the user clicks the SignIn button on the login page
    Then the user goes to the home page
  
  Scenario: The student role has no access to delete a group information
  	Given a user sees the group list page
  	When the user clicks delete button of a present group
  	Then the user clicks confirm button of the present group
  	* the user request is denied
  
  Scenario: The student role has no access to retrieve and update a group information
  	Given a user sees the group list page
    When the user clicks on the group name link 
    Then the user request is denied
  
  Scenario: The user role has no acces to a create group
  	Given a user sees the group list page
  	When the user clicks the create group button
  	Then the user imputs a group name
  	* press the save changes button
  	* the user request is denied
    
  Scenario: The user role lists all groups
  	Given a user on the home page
  	When the user clicks on the groups button
  	Then the user goes to groups list page
  
  Scenario: The student role has access to the courses list page
    When the user clicks the courses button
    * the user goes to the courses list page
  
  Scenario: The user role has no access to create a course
    Given a user sees the courses list page
    When the user clicks the create button
    Then the user enters a course name on the create course panel
    * the user enters a course desctiption on the create course panel
    * the user clicks the save changes button on the create course panel
    * the user request is denied
  
  Scenario: The user role has no acccess to receive a course  
    Given a user sees the courses list page
    When the user clicks a present course name link
    Then the user request is denied
    
  Scenario: The student role has no access to update a course
    Given a user sees the courses list page
    When the user clicks a present course name link   
    Then the user request is denied
    
  Scenario: The student role has no access to delete a course
    Given a user sees the courses list page
    When the user clicks the delete course button of existence course
    Then the user request is denied
