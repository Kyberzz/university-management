package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.univesity.exception.ServiceException;

public interface TeacherService<T> {
    
    public List<T> getAllTeachers() throws ServiceException;
    public T getCourseListByTeacherId(int id) throws ServiceException;
}
