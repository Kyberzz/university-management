alter table university.timetable drop start_time;
alter table university.timetable add start_time time;
alter table university.timetable drop end_time;
alter table university.timetable add end_time time;