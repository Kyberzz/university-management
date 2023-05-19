package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.TimetableEntity;

public interface TimetableRepository extends JpaRepository<TimetableEntity, Integer> {

}
