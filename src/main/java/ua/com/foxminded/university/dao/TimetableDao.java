package ua.com.foxminded.university.dao;

public interface TimetableDao<T> {
    
    public T getTimetableByStudentId(int id);
    
    public T getTimetableByTeacherId(int id);
}
