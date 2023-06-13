package ua.com.foxminded.university.dtomother;

import static ua.com.foxminded.university.entitymother.TimingMother.*;

import ua.com.foxminded.university.dto.TimingDTO;

public class TimingDTOMother {
    
    public static TimingDTO.TimingDTOBuilder complete() {
        return TimingDTO.builder().breakDuration(BREAK_DURATION)
                                  .startTime(START_TIME)
                                  .lessonDuration(LESSON_DURATION);
    }

}
