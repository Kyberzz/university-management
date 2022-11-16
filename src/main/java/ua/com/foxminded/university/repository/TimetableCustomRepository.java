package ua.com.foxminded.university.repository;

public interface TimetableCustomRepository<T> {
    
    public T findById(int id) throws RepositoryException;
}
