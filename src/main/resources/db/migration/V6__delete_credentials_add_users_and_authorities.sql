drop table university.credentials;

create sequence university.users_seq as integer;
create table university.users(
	id integer default nextval('university.users_seq'::regclass) primary key,
	email varchar not null,
	is_actirve boolean not null
);
alter sequence university.users_seq owned by university.users.id;

create sequence university.authorities_seq as integer;
create table university.authorities (
	id integer default nextval('university.authorities_seq'::regclass) primary key,
	authority varchar not null,
	user_id integer references university.users(id)
);
alter sequence university.authorities_seq owned by university.authorities.id;

alter table university.staff rename to staffs;
alter table university.staffs add user_id integer references university.users(id);

alter table university.students add user_id integer references university.users(id);
alter table university.teachers add user_id integer references university.users(id);
alter table university.timetable rename to timetables;