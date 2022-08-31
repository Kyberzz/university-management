package ua.com.foxminded.university.service;

public interface CourseService<T> {
    
    public int updateCourse(T course);
    public T getTimetableListByCourseId(int id);
}
