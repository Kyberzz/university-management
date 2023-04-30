@tag
Feature: Teacher flow
  The actions that can be done by a user with the teacher authorities
  
  @tag1
  Scenario: Operations on a course entity
    Given A user has the <name> authorities
    When The user does <action>
    Then The accesss is <state>
    
    Examples:
    | name    | action   | state   |
    | teacher | create   | denied  |
    | teacher | retrieve | denied  |
    | teacher | update   | denied  |
    | teacher | delete   | denied  |
    | teacher | list     | granted |  