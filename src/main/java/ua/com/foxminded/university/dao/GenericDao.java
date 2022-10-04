package ua.com.foxminded.university.dao;

public interface GenericDao<T> {
    
    public void insert(T entity) throws DaoException; 
    public T getById(int id) throws DaoException;
    public void update(T entity) throws DaoException;
    public void deleteById(int id) throws DaoException;
}
