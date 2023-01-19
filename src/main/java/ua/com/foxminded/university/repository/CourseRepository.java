package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.exception.RepositoryException;

public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {
    
    public CourseEntity findTimetableListById(Integer id) throws RepositoryException;
    public CourseEntity findById(int id) throws RepositoryException;
}
