package ua.com.foxminded.university.repository;

public interface StudentCustomRepository<T> {
    
    public T findById(int id) throws RepositoryException;
}
