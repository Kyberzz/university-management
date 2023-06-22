Feature: Admin flow
  A user should be able to navigate to the admin panel
  A user must be the only one with access to the admin panel
  A user should be able to list all registered users on the admin panel page
  A user should be able to set required role for each registered user
  A user should be able to authorize a user (my addition)
  A user should be able to create/read/update/delete courses
  A user can Create/Read/Update/Delete group information
  A user should be able to list all students in a group (read access)
  A user can assign/reassign Students to Group

  Background: A user authorizes with admin authorities
    Given a user enters admin credentials on the login page
    When the user clicks the SignIn button on the login page
    Then the user goes to the home page

  @adminDeassignsStudent  
  Scenario: The admin role deassign student to a group
    Given a user sees the group list page
    When the user clicks on a present group name link
    Then the user goes to a group page
    * the user clicks deassign button of a student
    * the student is absent in the students list of the group page
  
  @adminAssignsStudents
  Scenario: The admin role assign students to a group
    Given a user sees the group list page
    When the user clicks on a present group name link
    Then the user goes to a group page
    * the user click the add student button
    * the user selects students
    * the user clicks the save selected students button
    * the user sees added students on the group page
  
  Scenario: The admin role sees students in a group
    Given a user sees the group list page
    When the user clicks on a present group name link
    * the user goes to a group page

  Scenario: The admin role navigates to the admin panel
    Given the user sees the admin panel button on the home page
    When the user clicks the admin panel button on the home page
    Then the user sees the admin panel page

  Scenario Outline: The admin role sets required role for each registered user
    Given the user sees the admin panel button on the home page
    When the user clicks the admin panel button on the home page
    Then the user sees the admin panel page
    And the user clicks the Edit button of a user that has an email
    And the user selects <role> authority
    And the user clicks the SaveChanges button
    And the user sees the <role> authority of user in the list

    Examples: 
      | role  |
      | admin |
      | staff |

  Scenario: The admin role authorizes a user
    Given the user sees the admin panel button on the home page
    When the user clicks the admin panel button on the home page
    Then the user sees the admin panel page
    * the user clicks the authorize button of a user with id
    * the user select authority in the authorize menu
    * the user selects active status in the authorize menu
    * the user enters a password and confirm password
    * the user click the athorize button in the authorize menu
    * the user sees updated data of the user on the admin panel

  @adminCreatesCourse
  Scenario: The admin role creates a course
    Given a user sees the courses button on the home page
    When the user clicks the courses button
    Then the user goes to the courses list page
    * the user clicks the create button
    * the user enters a course name on the create course panel
    * the user enters a course desctiption on the create course panel
    * the user clicks the save changes button on the create course panel
    * the created course is present on the courses list page

  Scenario: The admin role receives a course
    Given a user sees the courses list page
    When the user clicks a present course name link
    Then the user goes to a course page

  @adminUpdatesCourse
  Scenario: The admin role updates a course
    Given a user sees the courses list page
    When the user clicks a course name link
    Then the user goes to a course page
    * the user enters a new course name into the input field
    * the user enters new course description into the input field
    * the user clicks the save changes button on the course page
    * the user sees updated couse data on the course page

  @adminDeletesCourse
  Scenario: The admin role deletes a course
    Given a user sees the courses list page
    When the user clicks the delete course button of a course
    Then the course is not present on the courses list page

  @adminCreatesGroup
  Scenario: The admin role creates a group
    Given a user sees the group list page
    When the user clicks the create group button
    Then the user imputs a group name
    * press the save changes button
    * the user sees the created course on the group list page

  Scenario: The admin role retrieves a group information
    Given a user sees the group list page
    When the user clicks on a present group name link
    Then the user goes to a group page

  @adminUpdatesGroup
  Scenario: The admin role updates a group name
    Given a user sees the group list page
    When the user clicks on name text link of a group
    Then the user inputs a group name to the group name field
    Then the user clicks on the update group button
    * the user clicks the confirm button
    * the user sees the updated group name in the group page

  @adminDeletsGroup
  Scenario: The admin deletes a group
    Given a user sees the group list page
    When the user clicks the delete button of a group
    Then the user clicks the confirm button of the group
    * the deleted group is not present
