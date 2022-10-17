-- create role university with login password '2345'
-- create database university owner university
--drop schema if exists university cascade;
create schema university;

create table university.teachers (
    id integer primary key,
    first_name varchar,
    last_name varchar
);

create table university.courses (
    id integer primary key,
    teacher_id integer references university.teachers(id) on delete set null,
    name varchar not null,
    description varchar collate pg_catalog."default"
);

create type university.week_day as enum (
    'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'
);

create table university.groups (
    id integer primary key,
    name varchar not null unique  
);

create table university.students (
    id integer primary key,
    group_id integer references university.groups(id) on delete set null,
    first_name varchar not null,
    last_name varchar not null
);

create table university.timetable (
	id integer primary key,
	group_id integer references university.groups(id) on delete set null,
	course_id integer references university.courses(id) on delete set null,
	start_time bigint,
	end_time bigint,
	description varchar,
    week_day university.week_day
);
