package ua.com.foxminded.university.comparator;

import java.util.Comparator;

import ua.com.foxminded.university.model.ScheduleModel;

public class ScheduleModelComparator implements Comparator<ScheduleModel> {

    @Override
    public int compare(ScheduleModel firstSchedule, ScheduleModel secondScheule) {
        if (compareByDate(firstSchedule, secondScheule) == 0 ) {
            return Integer.compare(firstSchedule.getLessonOrder().getRepresentation(), 
                                   secondScheule.getLessonOrder().getRepresentation());
        } else {
            return compareByDate(firstSchedule, secondScheule);
        }
    }
    
    private int compareByDate(ScheduleModel one, ScheduleModel another) {
        return one.getDatestamp().compareTo(another.getDatestamp());
    }
}
