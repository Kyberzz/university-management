package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.model.StudentModel;

public interface StudentService extends GenericService<StudentModel> {
    
    public void sortByLastName(List<StudentModel> students);
}