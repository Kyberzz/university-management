package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.CourseEntity;

public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {

    public CourseEntity findTimetablesById(Integer id);

    public CourseEntity findById(int id);
}
