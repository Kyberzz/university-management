package ua.com.foxminded.university.comparator;

import java.util.Comparator;

import ua.com.foxminded.university.dto.TeacherDTO;

public class TeacherModelComparator implements Comparator<TeacherDTO>{

    @Override
    public int compare(TeacherDTO firstTeacher, TeacherDTO secondTeacher) {
        return Integer.compare(firstTeacher.getId(), secondTeacher.getId());
    }
}
