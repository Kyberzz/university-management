package ua.com.foxminded.university.service;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StudentService<T> {
    
    public int updateStudent(T model) throws ServiceException;
}
