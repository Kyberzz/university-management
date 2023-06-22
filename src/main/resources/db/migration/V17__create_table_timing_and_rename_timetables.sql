alter table university.timetables rename to lessons;
alter table university.lessons
    add column lesson_order integer,
    drop column start_time, 
    drop column lesson_duration,
    drop column break_duration;

create sequence university.timetables_seq as integer;
create table university.timetables (
    id integer default nextval('university.timetables_seq'::regclass) primary key,
    name varchar
);
alter sequence university.timetables_seq owned by university.timetables.id;
    
create sequence university.timings_seq as integer;
create table university.timings (
    id integer default nextval('university.timings_seq'::regclass) primary key,
    start_time time,
    lesson_duration integer,
    break_duration integer,
    timetable_id integer not null references university.timetables(id) on delete cascade
);
alter sequence university.timings_seq owned by university.timings.id;

alter table university.lessons 
    add column timetable_id integer references university.timetables(id) 
    on delete set null;
