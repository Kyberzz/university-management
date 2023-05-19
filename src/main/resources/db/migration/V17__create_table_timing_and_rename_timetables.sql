alter table university.timetables rename to schedules;
alter table university.schedules
    drop column start_time, 
    drop column lesson_duration,
    drop column break_duration;

create sequence university.timetables_seq as integer;
create table university.timetables (
    id bigint default nextval('university.timetables_seq'::regclass) primary key,
    name varchar
);
alter sequence university.timetables_seq owned by university.timetables.id;
    
create sequence university.lessons_timing_seq as integer;
create table university.lessons_timing (
    id bigint default nextval('university.lessons_timing_seq'::regclass) primary key,
    start_time time,
    lesson_duration integer,
    break_duration integer,
    timetable_id bigint references university.timetables(id) on delete set null
);
alter sequence university.lessons_timing_seq owned by university.lessons_timing.id;

alter table university.schedules 
    add column lesson_timing_id bigint references university.lessons_timing(id) 
    on delete set null;
