package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.exception.ServiceException;

public interface CourseService<T> {
    
    public void deleteById(int id) throws ServiceException;
    
    public T getById(int id) throws ServiceException;
    
    public List<T> getAll() throws ServiceException;
    
    public void create(T course) throws ServiceException;
    
    public void update(T course) throws ServiceException;

    public T getTimetableListByCourseId(int id) throws ServiceException;
}
