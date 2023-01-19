package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.exception.ServiceException;

public interface GroupService<T> {
    
    public List<T> getAllGroups() throws ServiceException;
    public T getStudentListByGroupId(int id) throws ServiceException;
    public T getTimetableListByGroupId(int id) throws ServiceException;
}
