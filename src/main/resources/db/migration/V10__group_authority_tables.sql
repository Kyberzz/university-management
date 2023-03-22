create sequence university.user_groups_seq as integer;
create table university.user_groups (
    id integer default nextval('university.user_groups_seq'::regclass) primary key,
    name varchar not null
);
alter sequence university.user_groups_seq owned by university.user_groups.id;

create sequence university.group_authorities_seq as integer;
create table university.group_authorities (
    id integer default nextval('university.group_authorities_seq'::regclass) primary key,
    user_group_id integer references university.user_groups(id) not null,
    authority_id integer references university.user_groups(id) not null
);
alter sequence university.group_authorities_seq owned by university.group_authorities.id;

create sequence university.group_members_seq as integer;
create table university.group_members (
    id integer default nextval('university.group_members_seq'::regclass) primary key,
    user_id integer references university.users(id) not null,
    user_group_id integer not null references university.user_groups(id) not null
);
alter sequence university.group_members_seq owned by university.group_members.id;