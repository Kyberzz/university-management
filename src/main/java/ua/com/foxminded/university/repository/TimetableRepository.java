package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.TimetableEntity;

public interface TimetableRepository extends JpaRepository<TimetableEntity, Integer> {

    public TimetableEntity findCourseById(Integer id);

    public TimetableEntity findGroupById(Integer id);

    public TimetableEntity findById(int id);
}
