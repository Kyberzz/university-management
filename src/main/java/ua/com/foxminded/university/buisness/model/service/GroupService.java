package ua.com.foxminded.university.buisness.model.service;

public interface GroupService<T> {
    
    public T getStudentListByGroupId(int id) throws ServiceException;
    public T getTimetableListByGroupId(int id) throws ServiceException;
}
