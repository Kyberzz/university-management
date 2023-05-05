package ua.com.foxminded.university.comparator;

import java.util.Comparator;

import ua.com.foxminded.university.model.TimetableModel;

public class TimetableModelComparator implements Comparator<TimetableModel> {

    @Override
    public int compare(TimetableModel one, TimetableModel another) {
        if (compareByDate(one, another) == 0 ) {
            return one.getStartTime().compareTo(another.getStartTime());
        } else {
            return compareByDate(one, another);
        }
    }
    
    private int compareByDate(TimetableModel one, TimetableModel another) {
        return one.getDatestamp().compareTo(another.getDatestamp());
    }
}
