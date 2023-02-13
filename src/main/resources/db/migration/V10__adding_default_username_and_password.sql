insert into public.users(username, password, enabled) values('admin', 
'{bcrypt}$2a$10$4w9yFzI6KlhVyKhdBJBBGeQjE6fwy8sk0FuUvM3ZuJqXTznHwVZdO', true);
insert into public.authorities(username, authority) values('admin', 'ADMIN');