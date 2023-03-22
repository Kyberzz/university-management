package ua.com.foxminded.university.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.StudentModel;

public interface StudentService<T> {
    
    public static final String HAS_ROLE_STAFF_OR_ADMIN = "hasAnyRole('STAFF','ADMIN')";
    
    @PreAuthorize(HAS_ROLE_STAFF_OR_ADMIN)
    public void deleteStudentById(int id) throws ServiceException;
    
    @PreAuthorize(HAS_ROLE_STAFF_OR_ADMIN)
    public void updateStudent(T model) throws ServiceException;

    public List<T> getAllStudentsIncludingEmails() throws ServiceException;

    public StudentModel getStudentById(int id) throws ServiceException;
    
    @PreAuthorize(HAS_ROLE_STAFF_OR_ADMIN)
    public void addStudent(T model) throws ServiceException;
}
