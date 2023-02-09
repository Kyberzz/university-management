package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.exception.ServiceException;

public interface UserService<T> {
    
    public void deleteById(Integer id) throws ServiceException;
    public T getUserById(int id) throws ServiceException;
    public List<T> getNotAuthorizedUsers() throws ServiceException;
    public T getByEmail(String user) throws ServiceException;
    public List<T> getAllUsers() throws ServiceException;
    public void updateUser(T user) throws ServiceException;
    public T getActiveUserByEmail(String email) throws ServiceException;
}
