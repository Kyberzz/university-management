alter table university.students drop constraint students_user_id_fkey;
alter table university.students 
add constraint students_user_id_fkey foreign key(user_id) 
references university.users(id) on delete set null;

alter table university.teachers drop constraint teachers_user_id_fkey;
alter table university.teachers
add constraint teachers_user_id_fkey foreign key(user_id)
references university.users(id) on delete set null;

alter table university.staffs drop constraint staffs_user_id_fkey;
alter table university.staffs
add constraint staffs_user_id_fkey foreign key(user_id)
references university.users(id) on delete set null;

