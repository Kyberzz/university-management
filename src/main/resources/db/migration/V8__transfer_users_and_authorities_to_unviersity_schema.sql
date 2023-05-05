alter table public.users set schema university;
alter table public.authorities set schema university;

create sequence university.users_seq as integer;
alter table university.users rename column username to email;
alter table university.users 
    add column first_name varchar,
    add column last_name varchar,
    add column id integer default nextval('university.users_seq'::regclass) not null unique,
    add unique(email);
alter sequence university.users_seq owned by university.users.id;

alter table university.students 
    add column user_id integer,
    add foreign key(user_id) references university.users(id) on delete set null;
update university.students 
    set user_id = (select id from university.users where students.username = users.email);
alter table university.students drop column username;
    
alter table university.teachers 
    add column user_id integer, 
    add foreign key (user_id) references university.users(id) on delete set null;
update university.teachers 
    set user_id = (select id from university.users where teachers.username = users.email);
alter table university.teachers drop column username;
    
alter table university.staffs 
    add column user_id integer,
    add foreign key(user_id) references university.users(id) on delete set null;
update university.staffs 
    set user_id = (select id from university.users where staffs.username = users.email);
alter table university.staffs drop column username;

alter table university.authorities 
    add column user_id integer references university.users(id);
update university.authorities 
    set user_id = (select id from university.users where authorities.username = users.email);
alter table university.authorities drop constraint fk_authorities_users;
alter table university.authorities drop column username;
alter table university.authorities add primary key (id);

alter table university.users drop constraint users_pkey;
alter table university.users add primary key (id);

drop table public.group_authorities, public.group_members, public.groups;