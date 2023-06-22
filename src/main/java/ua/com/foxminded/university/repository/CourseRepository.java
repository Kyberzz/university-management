package ua.com.foxminded.university.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    
    public List<Course> findByTeachersId(int teacherId);
    
    public Course findTeachersAndLessonsById(int id);
    
    public Course findTimetablesById(Integer id);

    public Course findById(int id);
}
