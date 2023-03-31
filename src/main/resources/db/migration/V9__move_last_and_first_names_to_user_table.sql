alter table university.users 
    add column student_id integer references university.students(id);
    
alter table university.users 
    alter column email drop not null,
    alter column password drop not null, 
    alter column enabled drop not null;
insert into university.users(first_name, last_name, student_id) 
    select first_name, last_name, id
    from university.students;
update university.students 
    set user_id = (select id from university.users where users.student_id=students.id);
alter table university.students drop column first_name, drop column last_name;    
alter table university.users drop column student_id;

alter table university.users 
    add column teacher_id integer references university.teachers(id);
    
insert into university.users(first_name, last_name, teacher_id) 
    select first_name, last_name, id from university.teachers;

update university.teachers 
    set user_id = (select id from university.users where teachers.id = users.teacher_id);
alter table university.teachers drop column first_name, drop column last_name;
alter table university.users drop column teacher_id;

alter table university.staffs 
    drop column first_name, 
    drop column last_name;