insert into university.teachers(first_name, last_name) values('Linus', 'Torvalds');
insert into university.teachers(first_name, last_name) values('Dennis', 'Ritchie');
insert into university.courses(name, teacher_id) values('Programming', 1);
insert into university.courses(name, teacher_id, description) values('Electrodynamics', 2, 
    'It is a branch of theoretical physics');
insert into university.courses(name, teacher_id) values('Mathematics', 1);
insert into university.groups(name) values('lt-58');
insert into university.groups(name) values('ua-77');
insert into university.timetable(start_time, end_time, week_day, group_id, course_id)
	values('8:00', '9:30', 'MONDAY', 1, 1);
insert into university.timetable(start_time, end_time, week_day, group_id, course_id)
	values('10:00', '11:30', 'TUESDAY', 1, 2);
insert into university.timetable(start_time, end_time, week_day, group_id, course_id)
	values('8:00', '9:30', 'FRIDAY', 1, 3);
insert into university.timetable(start_time, end_time, week_day, group_id, course_id)
	values('11:45', '13:15', 'TUESDAY', 2, 2);	
insert into university.timetable(start_time, end_time, week_day, group_id, course_id)
	values('9:45', '11:15', 'WEDNESDAY', 2, 3);	
insert into university.students(first_name, last_name, group_id) values('Adam', 'Fox', 1);
insert into university.students(first_name, last_name, group_id) values('Stiven', 'Parker', 1);
insert into university.students(first_name, last_name, group_id) values('Oliver', 'Stoun', 1);
insert into university.students(first_name, last_name, group_id) values('Luise', 'Key', 1);
insert into university.students(first_name, last_name, group_id) values('Jon', 'Snow', 2);
insert into university.students(first_name, last_name, group_id) values('Margaret', 'Thatcher', 2);
insert into university.students(first_name, last_name, group_id) values('Sansa', 'Stark ', 2);
insert into university.students(first_name, last_name, group_id) values('Rhaenyra ', 'Targaryen ', 2);