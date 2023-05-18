alter table university.timetables rename to schedules;
alter table university.schedules
    add column lesson_order varchar,
    drop column start_time, 
    drop column lesson_duration,
    drop column break_duration;

create sequence university.timings_seq as integer;
create table university.timings (
    id bigint default nextval('university.timings_seq'::regclass) primary key,
    start_time time,
    first_lesson_duration integer,
    first_break_duration integer,
    second_lesson_duration integer,
    second_break_duration integer,
    third_lesson_duration integer,
    third_break_duration integer,
    fourth_lesson_duration integer,
    fourth_break_duration integer,
    fifth_lesson_duration integer
);
alter sequence university.timings_seq owned by university.timings.id;

alter table university.schedules 
    add column timing_id bigint references university.timings(id) on delete set null;
