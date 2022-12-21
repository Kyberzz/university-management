package ua.com.foxminded.university.buisness.model.service;

import java.util.List;

public interface CourseService<T> {
    
    public List<T> getAllCourses() throws ServiceException;
    public void updateCourse(T course) throws ServiceException;
    public T getTimetableListByCourseId(int id) throws ServiceException;
}
