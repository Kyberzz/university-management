alter table university.timetable 
alter column week_day type varchar, 
alter column week_day drop not null; 
drop type if exists university.week_day;