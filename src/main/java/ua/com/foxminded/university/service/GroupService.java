package ua.com.foxminded.university.service;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface GroupService<T> {
    
    public T getStudentListByGroupId(int id) throws ServiceException;
    public T getTimetableListByGroupId(int id) throws ServiceException;
}
