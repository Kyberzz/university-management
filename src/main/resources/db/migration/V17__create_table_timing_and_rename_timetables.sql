alter table university.timetables rename to schedules;
alter table university.schedules
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
    timetable_id bigint not null references university.timetables(id) on delete cascade
);
alter sequence university.timings_seq owned by university.timings.id;

alter table university.schedules 
    add column timing_id bigint references university.timings(id) 
    on delete set null;
