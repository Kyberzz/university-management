package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.StudentModel;

public interface StudentService<T> {
    
    public void deleteStudentById(int id) throws ServiceException;
    public void updateStudent(T model) throws ServiceException;
    public List<T> getAllStudentsIncludingEmails() throws ServiceException;
    public StudentModel getStudentById(int id) throws ServiceException;
    public void addStudent(T model) throws ServiceException;
}
