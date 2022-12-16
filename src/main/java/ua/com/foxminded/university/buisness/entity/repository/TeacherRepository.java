package ua.com.foxminded.university.buisness.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.buisness.entity.TeacherEntity;

public interface TeacherRepository extends JpaRepository<TeacherEntity, Integer> {
    
    public TeacherEntity findCourseListById(Integer id) throws RepositoryException;
    public TeacherEntity findById(int id) throws RepositoryException;
}
