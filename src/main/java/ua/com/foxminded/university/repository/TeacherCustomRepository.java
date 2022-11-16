package ua.com.foxminded.university.repository;

public interface TeacherCustomRepository<T> {
    
    public T findById(int id) throws RepositoryException;
}
