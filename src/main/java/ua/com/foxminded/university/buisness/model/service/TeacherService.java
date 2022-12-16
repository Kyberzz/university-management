package ua.com.foxminded.university.buisness.model.service;

public interface TeacherService<T> {
    
    public T getCourseListByTeacherId(int id) throws ServiceException;
}
