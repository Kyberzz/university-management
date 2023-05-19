package ua.com.foxminded.university.entitymother;

import java.time.LocalDate;

import ua.com.foxminded.university.entity.ScheduleEntity;

public class ScheduleEntityMother {
    
    public static final String SCHEDULE_DESCRIPTION = "some timetable description";
    public static final LocalDate DATE = LocalDate.of(2023, 5, 24);
   
    
    public static ScheduleEntity.ScheduleEntityBuilder complete() {
        return ScheduleEntity.builder().datestamp(DATE)
                                       .description(SCHEDULE_DESCRIPTION);
    }
}
