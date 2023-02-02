package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.exception.ServiceException;

public interface UserService<T> {
    
    public List<T> getAllUsers() throws ServiceException;
    public void udateUser(T user) throws ServiceException;
    public T getActiveUserAuthorityByEmail(String email) throws ServiceException;
}
