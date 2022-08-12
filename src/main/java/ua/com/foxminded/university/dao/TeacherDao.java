package ua.com.foxminded.university.dao;

public interface TeacherDao<T> {
    
    public T getTeacherById(int id);
}
