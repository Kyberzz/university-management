package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.model.StudentModel;

public interface StudentService<T> {
    
    public void updateStudent(T model) throws ServiceException;
    public List<T> getAllStudents() throws ServiceException;
    public StudentModel getStudentById(int id) throws ServiceException;
}
