package ua.com.foxminded.university.service;

public interface GroupService<T> {
    
    public T getStudentListByGroupId(int id) throws ServiceException;
    public T getTimetableListByGroupId(int id) throws ServiceException;
}
