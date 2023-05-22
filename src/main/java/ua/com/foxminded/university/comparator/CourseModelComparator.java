package ua.com.foxminded.university.comparator;

import java.util.Comparator;

import ua.com.foxminded.university.model.CourseModel;

public class CourseModelComparator implements Comparator<CourseModel> {
    
    @Override
    public int compare(CourseModel firstCourse, CourseModel secondCourse) {
        return Integer.compare(firstCourse.getId(), secondCourse.getId());
    }
}
