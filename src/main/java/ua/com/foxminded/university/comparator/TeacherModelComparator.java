package ua.com.foxminded.university.comparator;

import java.util.Comparator;

import ua.com.foxminded.university.model.TeacherModel;

public class TeacherModelComparator implements Comparator<TeacherModel>{

    @Override
    public int compare(TeacherModel firstTeacher, TeacherModel secondTeacher) {
        return Integer.compare(firstTeacher.getId(), secondTeacher.getId());
    }
}
