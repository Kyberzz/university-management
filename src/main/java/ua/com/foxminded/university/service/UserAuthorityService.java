package ua.com.foxminded.university.service;

import ua.com.foxminded.university.exception.ServiceException;

public interface UserAuthorityService<T> {
    
    public T saveUserAuthority(T model) throws ServiceException;
}
