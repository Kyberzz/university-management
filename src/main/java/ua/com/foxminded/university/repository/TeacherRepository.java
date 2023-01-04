package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.univesity.exception.RepositoryException;

public interface TeacherRepository extends JpaRepository<TeacherEntity, Integer> {
    
    public TeacherEntity findCourseListById(Integer id) throws RepositoryException;
    public TeacherEntity findById(int id) throws RepositoryException;
}
