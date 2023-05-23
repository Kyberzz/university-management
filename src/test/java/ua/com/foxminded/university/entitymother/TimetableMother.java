package ua.com.foxminded.university.entitymother;

import ua.com.foxminded.university.entity.Timetable;

public class TimetableMother {
    
    public static final String TIMETABLE_NAME = "general";
    
    public static Timetable.TimetableBuilder complete() {
        return Timetable.builder().name(TIMETABLE_NAME);
    }
}
