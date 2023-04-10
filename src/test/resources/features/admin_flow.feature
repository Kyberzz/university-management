@tag
Feature: Admin flow
  The actions that can be done by a user with the admin authorities
  
  @tag1
  Scenario Outline: CRUD operations on the course entity
    Given A user has the admin authorities
    When the user does <action>
    Then the course becomes <state>
    
    Examples:
      | action | state    |
      | create | created  |
      | get    | recieved |
      | update | updated  |
      | delete | deleted  |
