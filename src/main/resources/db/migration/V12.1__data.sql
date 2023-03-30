insert into university.users(email, password, enabled) values('a', '{noop}a', 'true');
insert into university.authorities(authority, user_id) values('ROLE_ADMIN', 1);

insert into university.users(first_name, last_name) values('Linus', 'Torvalds');
insert into university.users(first_name, last_name) values('Dennis', 'Ritchie');
insert into university.users(first_name, last_name) values('Isaac', 'Barrow');

insert into university.teachers(user_id) values(1);
insert into university.teachers(user_id) values(2);
insert into university.teachers(user_id) values(3);

insert into university.courses(name) values('Programming');
insert into university.courses(name) values('Mathematics');
insert into university.courses(name, description) values('Electrodynamics', 
                                                         'It is a branch of theoretical physics');

insert into university.teacher_course(teacher_id, course_id) values(1, 1);
insert into university.teacher_course(teacher_id, course_id) values(2, 1);
insert into university.teacher_course(teacher_id, course_id) values(3, 2);
insert into university.teacher_course(teacher_id, course_id) values(1, 3);

insert into university.groups(name) values('lt-58');
insert into university.groups(name) values('ua-77');

insert into university.users(first_name, last_name) values('Adam', 'Fox');
insert into university.users(first_name, last_name) values('Stiven', 'Parker');
insert into university.users(first_name, last_name) values('Oliver', 'Stoun');
insert into university.users(first_name, last_name) values('Luise', 'Key');
insert into university.users(first_name, last_name) values('Jon', 'Snow');
insert into university.users(first_name, last_name) values('Margaret', 'Thatcher');
insert into university.users(first_name, last_name) values('Sansa', 'Stark');
insert into university.users(first_name, last_name) values('Rhaenyra', 'Targaryen');

insert into university.students(user_id, group_id) values(4, 1);
insert into university.students(user_id, group_id) values(5, 1);
insert into university.students(user_id, group_id) values(6, 1);
insert into university.students(user_id, group_id) values(7, 1);
insert into university.students(user_id, group_id) values(8, 2);
insert into university.students(user_id, group_id) values(9, 2);
insert into university.students(user_id, group_id) values(10, 2);
insert into university.students(user_id, group_id) values(11, 2);

insert into university.timetables(lesson_order, week_day, group_id, course_id)
    values('FIRST_LESSON', 'MONDAY', 1, 1);
insert into university.timetables(lesson_order, week_day, group_id, course_id)
    values('SECOND_LESSON', 'TUESDAY', 1, 2);
insert into university.timetables(lesson_order, week_day, group_id, course_id)
    values('THIRD_LESSON', 'FRIDAY', 1, 3);
insert into university.timetables(lesson_order, week_day, group_id, course_id)
    values('FIFTH_LESSON', 'TUESDAY', 2, 2);  
insert into university.timetables(lesson_order, week_day, group_id, course_id)
    values('FOURTH_LESSON', 'WEDNESDAY', 2, 3); 