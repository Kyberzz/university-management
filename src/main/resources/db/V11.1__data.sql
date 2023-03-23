insert into university.users(email, password, enabled, first_name, last_name) values ('a', '{noop}a', true, 'Jhon', 'Parker');
insert into university.users(email, first_name, last_name) values('some@com', 'Elin', 'Traise');
insert into university.authorities(authority, user_id) values('ROLE_ADMIN', 1);