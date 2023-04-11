package ua.com.foxminded.university.model;

import java.time.LocalTime;

import lombok.Data;

@Data
public class LocalTimePeriod {
    
    private LocalTime start;
    private LocalTime end;
    
    public static LocalTimePeriod between(LocalTime start, LocalTime end) {
        LocalTimePeriod localTimePeriod = new LocalTimePeriod();
        localTimePeriod.setStart(start);
        localTimePeriod.setEnd(end);
        return localTimePeriod;
    }
    
    public boolean include(LocalTime localTime) {
        return localTime.isAfter(start) && localTime.isBefore(end);
    }
}
