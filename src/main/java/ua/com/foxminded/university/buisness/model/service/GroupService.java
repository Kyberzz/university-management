package ua.com.foxminded.university.buisness.model.service;

import java.util.List;

public interface GroupService<T> {
    
    public List<T> getAllGroups() throws ServiceException;
    public T getStudentListByGroupId(int id) throws ServiceException;
    public T getTimetableListByGroupId(int id) throws ServiceException;
}
