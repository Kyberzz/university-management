package ua.com.foxminded.university.comparator;

import java.util.Comparator;

import ua.com.foxminded.university.model.LessonModel;

public class ScheduleModelComparator implements Comparator<LessonModel> {

    @Override
    public int compare(LessonModel firstSchedule, LessonModel secondScheule) {
        if (compareByDate(firstSchedule, secondScheule) == 0 ) {
            return Integer.compare(firstSchedule.getLessonOrder(), 
                                   secondScheule.getLessonOrder());
        } else {
            return compareByDate(firstSchedule, secondScheule);
        }
    }
    
    private int compareByDate(LessonModel one, LessonModel another) {
        return one.getDatestamp().compareTo(another.getDatestamp());
    }
}
