package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.TimingEntity;

public interface TimingRepository extends JpaRepository<TimingEntity, Integer> {

}
