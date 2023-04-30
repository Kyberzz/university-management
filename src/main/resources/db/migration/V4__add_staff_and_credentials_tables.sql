create sequence university.staff_seq as integer;
create table university.staff (
	id integer default nextval('university.staff_seq'::regclass),
	first_name varchar collate pg_catalog."default",
	last_name varchar collate pg_catalog."default",
	position varchar collate pg_catalog."default",
	primary key (id)
);
alter sequence university.staff_seq owned by university.staff.id;

create sequence university.credentials_seq as integer;
create table university.credentials (
	id integer default nextval('university.credentials_seq'::regclass) primary key,
	email varchar collate pg_catalog."default" not null unique,
	password varchar collate pg_catalog."default",
	authority varchar collate pg_catalog."default",
	student_id integer references university.students(id) on delete set null,
	teacher_id integer references university.teachers(id) on delete set null,
	staff_id integer,
	foreign key (staff_id) references university.staff(id) on delete set null
);
alter sequence university.credentials_seq owned by university.credentials.id;