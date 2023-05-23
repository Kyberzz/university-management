package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    public Teacher findCoursesById(Integer id);

    public Teacher findById(int id);
}
