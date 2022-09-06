insert into university.teachers(first_name, last_name) values('Albert', 'Einstein');  
insert into university.teachers(first_name, last_name) values('Dennis', 'Ritchie');  
insert into university.courses(teacher_id, name, description) values(1, 'Physics', 'some description');
insert into university.courses(teacher_id, name, description) values(2, 'Programming', 'some description');
insert into university.courses(teacher_id, name) values(2, 'Circuitry');
insert into university.groups(name) values ('rs-01');
insert into university.groups(name) values ('kt-52');
insert into university.students(group_id, first_name, last_name) values(1, 'Alex', 'Smith');
insert into university.students(group_id, first_name, last_name) values(2, 'Julitta', 'Smith');
insert into university.timetable(group_id, course_id, start_time, end_time, week_day, description) 
	values(2, 1, 36360000, 39360000, 'MONDAY', 'some description');
insert into university.timetable(group_id, course_id, start_time, end_time, week_day) 
	values(1, 2, 36360000, 39360000, 'MONDAY');
insert into university.timetable(group_id, course_id, start_time, end_time, week_day) 
	values(1, 1, 36000000, 36360000, 'MONDAY');
insert into university.timetable(group_id, course_id, start_time, end_time, week_day) 
	values(1, 1, 28800000, 32400000, 'MONDAY');
