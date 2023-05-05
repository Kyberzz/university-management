package ua.com.foxminded.university.comparator;

import java.util.Comparator;

import ua.com.foxminded.university.model.StudentModel;

public class StudentModelComparator implements Comparator<StudentModel> {

    @Override
    public int compare(StudentModel studentA, StudentModel studentB) {
        return Integer.compare(studentA.getId(), studentB.getId());
    }
}
