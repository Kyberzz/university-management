package ua.com.foxminded.university.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.com.foxminded.university.entity.Timing;

public interface TimingRepository extends JpaRepository<Timing, Integer> {

    @Query("select t from Timing t left join fetch t.timetable ti where ti.id = ?1")
    public List <Timing> findByTimetableId(int id);
    
    public Timing findById(int id);
}
