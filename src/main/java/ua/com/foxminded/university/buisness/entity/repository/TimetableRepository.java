package ua.com.foxminded.university.buisness.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.buisness.entity.TimetableEntity;

public interface TimetableRepository extends JpaRepository<TimetableEntity, Integer> {
    
    public TimetableEntity findCourseById(Integer id) throws RepositoryException;
    public TimetableEntity findGroupById(Integer id) throws RepositoryException;
    public TimetableEntity findById(int id) throws RepositoryException;
}
