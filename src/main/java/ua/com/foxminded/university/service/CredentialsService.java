package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.exception.ServiceException;

public interface CredentialsService<T> {
    
    public List<T> getAll() throws ServiceException;
    public T getByEmail(String email) throws ServiceException;
}
