alter table university.timetables add constraint timetables_name_key unique (name);

create table university.lesson_group (
    lesson_id integer references university.lessons(id) on delete set null,
    group_id integer references university.groups(id) on delete set null
);

alter table university.lessons drop column group_id;