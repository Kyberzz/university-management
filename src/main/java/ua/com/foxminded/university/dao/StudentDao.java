package ua.com.foxminded.university.dao;

public interface StudentDao<T> {
    
    public T getStudentById(int id);
}
