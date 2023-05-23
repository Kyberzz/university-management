package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.com.foxminded.university.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    
    
    @Query("select c from Course c left join fetch c.teachers " + 
           "left join fetch c.lessons " + 
           "where c.id = ?1")
    public Course getCourseRelationsById(int id);
    
    public Course findTimetablesById(Integer id);

    public Course findById(int id);
}
