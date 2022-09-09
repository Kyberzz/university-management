package ua.com.foxminded.university.service;

public interface CourseService<T> {
    
    public int updateCourse(T course) throws ServiceException;
    public T getTimetableListByCourseId(int id) throws ServiceException;
}
