package ua.com.foxminded.university.entitymother;

import ua.com.foxminded.university.entity.TimetableEntity;

public class TimetableEntityMother {
    
    public static final String TIMETABLE_NAME = "general";
    
    public static TimetableEntity.TimetableEntityBuilder complete() {
        return TimetableEntity.builder().name(TIMETABLE_NAME);
    }
}
