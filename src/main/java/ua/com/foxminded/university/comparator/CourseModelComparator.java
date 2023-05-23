package ua.com.foxminded.university.comparator;

import java.util.Comparator;

import ua.com.foxminded.university.dto.CourseDTO;

public class CourseModelComparator implements Comparator<CourseDTO> {
    
    @Override
    public int compare(CourseDTO firstCourse, CourseDTO secondCourse) {
        return Integer.compare(firstCourse.getId(), secondCourse.getId());
    }
}
