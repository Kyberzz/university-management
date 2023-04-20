package ua.com.foxminded.university.comparator;

import java.util.Comparator;

import ua.com.foxminded.university.model.CourseModel;

public class CourseModelComparator implements Comparator<CourseModel> {
    
    public int compare(CourseModel courseA, CourseModel courseB) {
        return Integer.compare(courseA.getId(), courseB.getId());
    }
}
