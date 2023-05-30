package ua.com.foxminded.university.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.entity.Timing;

public interface TimingRepository extends JpaRepository<Timing, Integer> {

    public List <Timing> findByTimetableId(int id);
    
    public Timing findById(int id);
}
