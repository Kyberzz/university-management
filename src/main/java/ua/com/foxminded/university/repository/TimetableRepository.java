package ua.com.foxminded.university.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.com.foxminded.university.entity.Timetable;

public interface TimetableRepository extends JpaRepository<Timetable, Integer> {
    
    @Query("select t from Timetable t left join fetch t.timings where t.id = ?1")
    public Timetable getByIdWithTimings(int id);
    
    @Query("select t from Timetable t left join fetch t.timings")
    public List<Timetable> getAllWithTimings();
    
    public Timetable findById(int id);
}
