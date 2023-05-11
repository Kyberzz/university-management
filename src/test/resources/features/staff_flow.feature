Feature: The Staff flow
  A user should be able to create/read/update all courses
  A user should be able to assign/deassign teacher to a course
  A user should be able to assign/deassign groups to a course
  A user should be able to Create/Read/Update group information

  Background: A user loged in with staff role
    Given a user enters staff credentials on the login page
    When the user clicks the SignIn button on the login page
    Then the user goes to the home page
  
  Scenario: The staff role creates a group
    Given a user sees the group list page
    When the user clicks the create group button
    Then the user imputs a group name
    * press the save changes button
    * the user sees the created course on the group list page
  
  Scenario: The staff role retrieves a group information
    Given a user sees the group list page
    When the user clicks on the group name link
    Then the user goes to a group page
  
  @staffUpdatesGroup   
  Scenario: The staff role updates a group information
    Given a user sees the group list page
    When the user clicks on text link of the created group
    Then the user inputs a group name to the group name field
    Then the user clicks on the update group button
    * the user clicks the confirm button
    * the user sees the updated group name in the group page
	
  Scenario: The staff role has no access to delete a group
  	Given a user sees the group list page
  	When the user clicks the delete button of a present group
  	Then the user clicks the confirm button of a present group
  	* the user request is denied
  
  Scenario: The staff role creates a timetable for a day without timetable
  	Given a user clicks the timetables button on the home page 
  	When a user goes to a month timetable
  	Then the user clicks the timetable create button of any day
  	* the user selects time
  	* the user selects a break duration time
  	* the user selects a lesson
  	* the user selects a group name
  	* the user clicks the timetable save changes button
  	* the user goes to the created day timetable
 	
 	Scenario: The staff role creates a timetable for a day that already has timetables
 		Given a user clicks the timetables button on the home page 
 		When a user goes to a month timetable
 		Then the user click the expand button of any day
 		* the user goes to a day timetable page
 		* the user click create timetable button on a day timetable page
 		* the user selects time on a day timetable page
 		* the user selects a break duration time on a day timetable page
 		* the user selects the programming lesson on a day timetable page
 		* the user selects a group name on a day timetable page
 		* the user clicks the timetable save changes button on a day timetable page
 		* the created timetable presents on a day timetable page
 	
 	Scenario: The sraff role deletes a timetable from day timetables list
 		Given a user clicks the timetables button on the home page
 		When a user goes to a month timetable
  	Then the user click the expand button of any day
  	* the user goes to a day timetable page
  	* the user clicks the delete button of the created timetable
  	* the user clicks the confirm deleting button
  	* the deleted timetable is not present on a day timetable
  	
  Scenario: The staff role assigns a teacher to a course
    Given a user sees the courses list page
    When the user clicks a present course name link
    Then the user goes to a course page
    * the user clicks the assign teacher button
    * the user select a teacher to a course
    * the user clicks the save changes button
    * the user sees the selected teacher on the course page
  
  Scenario: The staff role deassigns a teacher to a course
    Given a user sees the courses list page
    When the user clicks a present course name link
    Then the user goes to a course page
    * the user clicks the deassign teacher button
    * the user clicks the deassign confirm button
    * the couse page has no the deassigned teacher
	
  Scenario: The staff role creates a course
    Given a user sees the courses list page
    When the user clicks the create button
    Then the user enters a course name on the create course panel
    * the user enters a course desctiption on the create course panel
    * the user clicks the save changes button on the create course panel
    * the created course is present on the courses list page
	
  Scenario: The staff role receives a course
    Given a user sees the courses list page
    When the user clicks a present course name link
    Then the user goes to a course page
	
	@courseUpdatingByStaff
  Scenario: The staff role updates a course
    Given a user sees the courses list page
    When the user clicks the created course name link
    Then the user goes to a course page
    * the user enters a new course name into the input field
    * the user enters new course description into the input field
    * the user clicks the save changes button on the course page
    * the user sees updated couse data on the course page
