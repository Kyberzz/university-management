package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.exception.ServiceException;

public interface UserService<T> {

    public T getById(int id) throws ServiceException; 
    
    public void createUser(T user) throws ServiceException;

    public void updateUser(T user) throws ServiceException;

    public T getByEmail(String user) throws ServiceException;

    public void deleteByEmail(String email) throws ServiceException;

    public List<T> getNotAuthorizedUsers() throws ServiceException;

    public List<T> getAll() throws ServiceException;
}
