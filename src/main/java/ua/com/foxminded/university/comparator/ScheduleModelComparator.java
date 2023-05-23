package ua.com.foxminded.university.comparator;

import java.util.Comparator;

import ua.com.foxminded.university.dto.LessonDTO;

public class ScheduleModelComparator implements Comparator<LessonDTO> {

    @Override
    public int compare(LessonDTO firstSchedule, LessonDTO secondScheule) {
        if (compareByDate(firstSchedule, secondScheule) == 0 ) {
            return Integer.compare(firstSchedule.getLessonOrder(), 
                                   secondScheule.getLessonOrder());
        } else {
            return compareByDate(firstSchedule, secondScheule);
        }
    }
    
    private int compareByDate(LessonDTO one, LessonDTO another) {
        return one.getDatestamp().compareTo(another.getDatestamp());
    }
}
