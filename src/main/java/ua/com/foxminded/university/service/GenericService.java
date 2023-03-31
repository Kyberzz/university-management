package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.exception.ServiceException;

public interface GenericService <T> {
    
    public void deleteById(Integer id) throws ServiceException; 
    
    public void create(T model) throws ServiceException;
    
    public void update(T model) throws ServiceException;
    
    public T getById(int id) throws ServiceException;
    
    public List<T> getAll() throws ServiceException;
}
