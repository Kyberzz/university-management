package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.BeforeTransaction;

import ua.com.foxminded.university.entity.Timetable;
import ua.com.foxminded.university.entity.Timing;
import ua.com.foxminded.university.entitymother.TimetableMother;
import ua.com.foxminded.university.entitymother.TimingMother;

@ActiveProfiles("test")
@DataJpaTest
class TimetableRepositoryTest {
    
    @Autowired
    private TimetableRepository timetableRepository;
    
    @Autowired
    private TimingRepository timingRepository;
    
    private Timetable timetable;
    private Timing timing;
    
    @BeforeTransaction
    void init() {
        timetable = TimetableMother.complete().build();
        timetableRepository.saveAndFlush(timetable);
        timing = TimingMother.complete().timetable(timetable).build();
        timingRepository.saveAndFlush(timing);
    }
    
    @Test
    void getByIdWithTimings_ShouldReturnTimingsRelationship() {
        Timetable persistedTimetable = timetableRepository.getByIdWithTimings(timetable.getId());
        assertEquals(timing, persistedTimetable.getTimings().iterator().next());
    }
    
    @Test
    void getAllWithTimings_ShouldReturnTimingsRelationship() {
        List<Timetable> timetables = timetableRepository.getAllWithTimings();
        Timetable persistedTimetable = timetables.iterator().next();
        
        assertEquals(timetable.getId(), 
                     persistedTimetable.getTimings().iterator().next().getId());
    }

    @Test
    void findById_ShouldReturnEntity() {
        Timetable persistedTimetable = timetableRepository.findById(
                timetable.getId().intValue());
        assertEquals(timetable.getId(), persistedTimetable.getId());
    }
}
