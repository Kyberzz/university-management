package ua.com.foxminded.university.buisness.model.service;

import java.util.List;

public interface TeacherService<T> {
    
    public List<T> getAllTeachers() throws ServiceException;
    public T getCourseListByTeacherId(int id) throws ServiceException;
}
