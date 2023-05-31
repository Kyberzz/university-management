package ua.com.foxminded.university.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    
    public List <Teacher> findByCoursesId(int id);
    
    public Teacher findByUserId(int id);

    public Teacher findCoursesById(int id);

    public Teacher findById(int id);
}
