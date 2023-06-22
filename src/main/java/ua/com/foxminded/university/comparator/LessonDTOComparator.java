package ua.com.foxminded.university.comparator;

import java.util.Comparator;

import ua.com.foxminded.university.dto.LessonDTO;

public class LessonDTOComparator implements Comparator<LessonDTO> {

    @Override
    public int compare(LessonDTO firstLesson, LessonDTO secondLesson) {
        if (compareByDate(firstLesson, secondLesson) == 0 ) {
            return Integer.compare(firstLesson.getLessonOrder(), 
                                   secondLesson.getLessonOrder());
        } else {
            return compareByDate(firstLesson, secondLesson);
        }
    }
    
    private int compareByDate(LessonDTO one, LessonDTO another) {
        return one.getDatestamp().compareTo(another.getDatestamp());
    }
}
