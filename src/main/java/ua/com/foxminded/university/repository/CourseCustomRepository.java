package ua.com.foxminded.university.repository;

public interface CourseCustomRepository<T> {
    
    public T findById(int id) throws RepositoryException;
}
