package ua.com.foxminded.university.dao;

public interface GenericDao<T> {
    
    public int insert(T entity) throws DaoException; 
    public T getById(int id) throws DaoException;
    public int update(T entity) throws DaoException;
    public int deleteById(int id) throws DaoException;
}
