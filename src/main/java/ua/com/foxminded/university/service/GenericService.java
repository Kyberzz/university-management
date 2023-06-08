package ua.com.foxminded.university.service;

import java.util.List;

public interface GenericService <T> {
    
    public void deleteById(Integer id); 
    
    public T create(T model);
    
    public void update(T model);
    
    public T getById(int id);
    
    public List<T> getAll();
}