insert into university.teachers(id, first_name, last_name) values(1, 'Albert', 'Einstein');  
insert into university.teachers(id, first_name, last_name) values(2, 'Dennis', 'Ritchie');  
insert into university.courses(id, teacher_id, name, description) values(1, 1, 'Physics', 'some description');
insert into university.courses(id, teacher_id, name, description) values(2, 2, 'Programming', 'some description');
insert into university.courses(id, teacher_id, name) values(3, 2, 'Circuitry');
insert into university.groups(id, name) values (1, 'rs-01');
insert into university.groups(id, name) values (2, 'kt-52');
insert into university.students(id, group_id, first_name, last_name) values(1, 1, 'Alex', 'Smith');
insert into university.students(id, group_id, first_name, last_name) values(2, 2, 'Julitta', 'Smith');
insert into university.students(id, group_id, first_name, last_name) values(3, 2, 'Adam', 'Wills');
insert into university.timetable(id, group_id, course_id, start_time, end_time, week_day, description) 
	values(1, 2, 1, 36360000, 39360000, 'MONDAY', 'some description');
insert into university.timetable(id, group_id, course_id, start_time, end_time, week_day) 
	values(2, 1, 2, 36360000, 39360000, 'MONDAY');
insert into university.timetable(id, group_id, course_id, start_time, end_time, week_day, description) 
	values(3, 2, 2, 36000000, 36360000, 'MONDAY', 'some description');
insert into university.timetable(id, group_id, course_id, start_time, end_time, week_day) 
	values(4, 1, 1, 36000000, 36360000, 'MONDAY');
insert into university.timetable(id, group_id, course_id, start_time, end_time, week_day) 
	values(5, 1, 1, 28800000, 32400000, 'MONDAY');
