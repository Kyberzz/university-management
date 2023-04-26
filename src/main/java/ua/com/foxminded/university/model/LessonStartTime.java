package ua.com.foxminded.university.model;

import java.time.LocalTime;

import lombok.Getter;

@Getter
public enum LessonStartTime {
    TIME_8_00("08:00"), TIME_9_45("09:45"), TIME_12_00("12:00"), 
    TIME_13_45("13:45"), TIME_15_30("15:30");
    
    private LocalTime representation;
    
    private LessonStartTime(String time) {
        representation = LocalTime.parse(time);
    }
}
