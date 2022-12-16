package ua.com.foxminded.university.buisness.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.buisness.entity.CourseEntity;

public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {
    
    public CourseEntity findTimetableListById(Integer id) throws RepositoryException;
    public CourseEntity findById(int id) throws RepositoryException;
}
