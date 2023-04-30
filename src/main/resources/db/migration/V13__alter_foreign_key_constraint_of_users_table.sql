alter table university.authorities drop constraint authorities_user_id_fkey;
alter table university.authorities add constraint authorities_user_id_fkey 
    foreign key (user_id) references university.users(id) on delete set null;