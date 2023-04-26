package ua.com.foxminded.university.comparator;

import java.util.Comparator;

import ua.com.foxminded.university.model.TimetableModel;

public class TimetableModelComparator implements Comparator<TimetableModel> {

    @Override
    public int compare(TimetableModel one, TimetableModel another) {
        return one.getStartTime().compareTo(another.getStartTime());
    }
}
