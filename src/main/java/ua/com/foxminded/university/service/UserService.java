package ua.com.foxminded.university.service;

import ua.com.foxminded.university.exception.ServiceException;

public interface UserService<T> {
    
    public T getActiveUserAuthorityByEmail(String email) throws ServiceException;
}
