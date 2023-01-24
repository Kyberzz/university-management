package ua.com.foxminded.university.service;

import ua.com.foxminded.university.exception.ServiceException;

public interface UserService<T> {
    
    public T getUserAuthorityByEmail(String email) throws ServiceException;
}
