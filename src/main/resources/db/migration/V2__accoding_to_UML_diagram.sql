-- create role university with login password '1234'
-- create database university owner university
drop schema if exists university cascade;
create schema university;

create sequence university.teachers_seq as integer;
create table university.teachers (
    id integer default nextval('university.teachers_seq'::regclass) primary key,
    first_name varchar collate pg_catalog."default" not null,
    last_name varchar collate pg_catalog."default" not null
)
tablespace pg_default;
alter sequence university.teachers_seq owned by university.teachers.id;

create sequence university.courses_seq as integer;
create table university.courses (
    id integer default nextval('university.courses_seq'::regclass) primary key,
    teacher_id integer references university.teachers(id) on delete set null,
    name varchar collate pg_catalog."default" not null,
    description varchar collate pg_catalog."default"
)
tablespace pg_default;
alter sequence university.courses_seq owned by university.courses.id;

create type university.week_day as enum (
    'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'
);

create sequence university.groups_seq as integer;
create table university.groups (
    id integer default nextval('university.groups_seq'::regclass) primary key,
    name varchar collate pg_catalog."default" not null unique  
)
tablespace pg_default;
alter sequence university.groups_seq owned by university.groups.id;

create sequence university.students_seq as integer;
create table university.students (
    id integer default nextval('university.students_seq'::regclass) primary key,
    group_id integer references university.groups(id) on delete set null,
    first_name varchar collate pg_catalog."default" not null,
    last_name varchar collate pg_catalog."default" not null
)
tablespace pg_default;
alter sequence university.students_seq owned by university.students.id;

create sequence university.timetable_seq as integer;
create table university.timetable (
	id integer default nextval('university.timetable_seq'::regclass) primary key,
	group_id integer references university.groups(id) on delete set null,
	course_id integer references university.courses(id) on delete set null,
	start_time bigint,
	end_time bigint,
	description varchar collate pg_catalog."default",
    week_day university.week_day not null
)
tablespace pg_default;
alter sequence university.timetable_seq owned by university.timetable.id;