create schema university;

create sequence university.teachers_id_seq as integer;
create table university.teachers (
    id integer default nextval('university.teachers_id_seq') primary key,
    first_name varchar,
    last_name varchar
);

create sequence university.courses_id_seq as integer;
create table university.courses (
    id integer default nextval('university.courses_id_seq') primary key,
    teacher_id integer references university.teachers(id) on delete set null,
    name varchar not null,
    description varchar
);

create type university.week_day as enum (
    'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'
);

create sequence university.groups_id_seq as integer;
create table university.groups (
    id integer default nextval('university.groups_id_seq') primary key,
    name varchar not null unique  
);

create sequence university.students_id_seq as integer;
create table university.students (
    id integer default nextval('university.students_id_seq') primary key,
    group_id integer references university.groups(id) on delete set null,
    first_name varchar,
    last_name varchar
);

create sequence university.timetable_id_seq as integer;
create table university.timetable (
	id integer default nextval('university.timetable_id_seq') primary key,
	group_id integer references university.groups(id) on delete set null,
	course_id integer references university.courses(id) on delete set null,
	start_time bigint,
	end_time bigint,
	description varchar, 
	week_day university.week_day
);