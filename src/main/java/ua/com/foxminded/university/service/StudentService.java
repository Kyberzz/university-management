package ua.com.foxminded.university.service;

public interface StudentService<T> {
    
    public void updateStudent(T model) throws ServiceException;
}
