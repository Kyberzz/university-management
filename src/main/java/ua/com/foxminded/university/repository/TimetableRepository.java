package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.univesity.exception.RepositoryException;

public interface TimetableRepository extends JpaRepository<TimetableEntity, Integer> {
    
    public TimetableEntity findCourseById(Integer id) throws RepositoryException;
    public TimetableEntity findGroupById(Integer id) throws RepositoryException;
    public TimetableEntity findById(int id) throws RepositoryException;
}
