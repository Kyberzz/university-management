package ua.com.foxminded.university.repository;

public interface GroupCustomRepository<T> {
    
    public T findById(int id) throws RepositoryException;
}
