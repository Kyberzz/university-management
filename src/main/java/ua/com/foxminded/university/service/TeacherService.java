package ua.com.foxminded.university.service;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TeacherService<T> {
    
    public T getCourseListByTeacherId(int id) throws ServiceException;
}
