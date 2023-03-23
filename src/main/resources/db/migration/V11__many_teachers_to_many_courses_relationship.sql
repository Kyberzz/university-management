alter table university.courses drop column teacher_id;

create table university.teacher_course (
    teacher_id integer references university.teachers(id) on delete set null,
    course_id integer references university.courses(id) on delete set null
);