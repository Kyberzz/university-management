package ua.com.foxminded.university.service;

public interface TeacherService<T> {
    
    public T getCourseListByTeacherId(int id) throws ServiceException;
}
