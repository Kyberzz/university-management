insert into university.users(email, password, is_active) values('admin', 'admin', true);
insert into university.users(email) values('email@com');
insert into university.users(email) values('myemail@com');
update university.students set user_id=2 where students.id=1;
update university.students set user_id=3 where students.id=2;
insert into university.authorities(user_id, authority) values('1', 'ADMINISTRATOR');