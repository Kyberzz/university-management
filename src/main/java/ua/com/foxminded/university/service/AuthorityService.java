package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.exception.ServiceException;

public interface AuthorityService<T> {
    
    public T getAllAuthoritySorts() throws ServiceException;
    public List<T> getAll() throws ServiceException;
}
