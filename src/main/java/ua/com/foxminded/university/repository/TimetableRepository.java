package ua.com.foxminded.university.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.com.foxminded.university.entity.TimetableEntity;

public interface TimetableRepository extends JpaRepository<TimetableEntity, Integer> {
    
    @Query("select t from TimetableEntity t left join fetch t.timings")
    public List<TimetableEntity> getAllWithTimings();
    
    public TimetableEntity findById(int id);
}
