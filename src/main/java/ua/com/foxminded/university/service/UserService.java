package ua.com.foxminded.university.service;

import ua.com.foxminded.university.exception.ServiceException;

public interface UserService<T> {
    
    public void udateUser(T user) throws ServiceException;
    public T getActiveUserAuthorityByEmail(String email) throws ServiceException;
}
