package ua.com.foxminded.university.repository;

public interface GenericRepository<T> {
    
    public T insert(T entity) throws RepositoryException; 
    public T getById(int id) throws RepositoryException;
    public T update(T entity) throws RepositoryException;
    public void deleteById(int id) throws RepositoryException;
}
