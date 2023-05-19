package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.LessonTimingEntity;

public interface TimingRepository extends JpaRepository<LessonTimingEntity, Integer> {

}
