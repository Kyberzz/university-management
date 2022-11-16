package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.CourseEntity;

public interface CourseRepository extends JpaRepository<CourseEntity, Integer>, 
                                          CourseCustomRepository<CourseEntity> {
    
    public CourseEntity findTimetableListById(Integer id) throws RepositoryException;
}
