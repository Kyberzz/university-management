package ua.com.foxminded.university.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.com.foxminded.university.entity.TimingEntity;

public interface TimingRepository extends JpaRepository<TimingEntity, Integer> {

    @Query("select t from TimingEntity t left join fetch t.timetable ti where ti.id = ?1")
    public List <TimingEntity> findByTimetableId(int id);
    
    public TimingEntity findById(int id);
}
