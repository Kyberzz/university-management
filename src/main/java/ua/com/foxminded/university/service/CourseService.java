package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.univesity.exception.ServiceException;

public interface CourseService<T> {
    
    public List<T> getAllCourses() throws ServiceException;
    public void updateCourse(T course) throws ServiceException;
    public T getTimetableListByCourseId(int id) throws ServiceException;
}
