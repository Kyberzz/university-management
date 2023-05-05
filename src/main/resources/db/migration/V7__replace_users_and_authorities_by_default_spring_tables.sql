drop table university.authorities cascade;
drop table university.users cascade;

create table public.users(
    username varchar(50) not null primary key,
    password varchar(500) not null,
    enabled boolean not null
);

create sequence public.authorities_seq as integer;
create table public.authorities (
    id integer default nextval('authorities_seq'::regclass) unique not null, 
    username varchar(50) not null,
    authority varchar(50) not null,
    constraint fk_authorities_users foreign key(username) references public.users(username)
);
alter sequence public.authorities_seq owned by public.authorities.id;
create unique index ix_auth_username on public.authorities (username,authority);

alter table university.students drop column user_id;
alter table university.students add username varchar references public.users(username);

alter table university.teachers drop column user_id;
alter table university.teachers add username varchar references public.users(username);

alter table university.staffs drop column user_id;
alter table university.staffs add username varchar references public.users(username);

create sequence public.groups_seq as integer;
create table public.groups (
    id integer default nextval('public.groups_seq'::regclass) primary key,
    group_name varchar (50) not null
);
alter sequence public.groups_seq owned by public.groups.id;

create sequence public.group_authorities_seq as integer;
create table public.group_authorities (
    group_id integer not null,
    authority varchar(50) not null,
    constraint fk_group_authorities_group foreign key(group_id) references public.groups(id)
);
alter sequence public.group_authorities_seq owned by public.group_authorities.group_id;

create sequence public.group_members_seq as integer;
create table public.group_members (
    id integer default nextval('public.group_members_seq'::regclass) primary key,
    username varchar(50) not null,
    group_id integer not null,
    constraint fk_group_members_group foreign key(group_id) references public.groups(id)
);
alter sequence public.group_members_seq owned by public.group_members.id;
