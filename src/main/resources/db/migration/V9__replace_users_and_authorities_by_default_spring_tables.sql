drop table university.authorities cascade;
drop table university.users cascade;

create sequence users_seq as integer;
create table users(
    username varchar(50) not null primary key,
    password varchar(500) not null,
    enabled boolean not null
);

create table authorities (
    username varchar(50) not null,
    authority varchar(50) not null,
    constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);

alter table university.students drop column user_id;
alter table university.students add username varchar references users(username);

alter table university.teachers drop column user_id;
alter table university.teachers add username varchar references users(username);

alter table university.staffs drop column user_id;
alter table university.staffs add username varchar references users(username);
