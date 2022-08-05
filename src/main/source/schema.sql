drop schema if exists university cascade;
create schema university;

create sequence university.teacher_id_seq as integer;
create table university.teacher (
    id integer default nextval('university.teacher_id_seq'::regclass) primary key,
    first_name varchar collate pg_catalog."default" not null
)
tablespace pg_default;
alter sequence university.teacher_id_seq owned by university.teacher.id;

create sequence university.course_id_seq as integer;
create table university.course (
    id integer default nextval('university.course_id_seq'::regclass) primary key,
    teacher_id integer references university.teacher(id) on delete set null,
    name varchar collate pg_catalog."default" not null unique,
    description varchar collate pg_catalog."default"
)
tablespace pg_default;
alter sequence university.course_id_seq owned by university.course.id;

create type university.week_day as enum (
    'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'
);

create sequence university.group_id_seq as integer;
create table university.group (
    id integer default nextval('university.group_id_seq'::regclass) primary key,
    name varchar collate pg_catalog."default" not null unique  
)
tablespace pg_default;
alter sequence university.group_id_seq owned by university.group.id;

create sequence university.student_id_seq as integer;
create table university.student (
    id integer default nextval('university.student_id_seq'::regclass) primary key,
    group_id integer references university.group(id) on delete set null,
    first_name varchar collate pg_catalog."default" not null,
    last_name varchar collate pg_catalog."default" not null
)
tablespace pg_default;
alter sequence university.student_id_seq owned by university.student.id;