package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.TeacherEntity;

public interface TeacherRepository extends JpaRepository<TeacherEntity, Integer> {

    public TeacherEntity findCoursesById(Integer id);

    public TeacherEntity findById(int id);
}
