package ua.com.foxminded.university.dao;

public interface GenericDao<T> {
    
    public int insert(T entity); 
    public T getById(int id);
    public int update(T entity);
    public int deleteById(int id);
}
