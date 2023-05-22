package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.BeforeTransaction;

import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.TimingEntity;
import ua.com.foxminded.university.entitymother.TimetableEntityMother;
import ua.com.foxminded.university.entitymother.TimingEntityMother;

@ActiveProfiles("test")
@DataJpaTest
class TimetableRepositoryTest {
    
    @Autowired
    private TimetableRepository timetableRepository;
    
    @Autowired
    private TimingRepository timingRepository;
    
    private TimetableEntity timetable;
    private TimingEntity timingEntity;
    
    @BeforeTransaction
    void init() {
        timetable = TimetableEntityMother.complete().build();
        timetableRepository.saveAndFlush(timetable);
        timingEntity = TimingEntityMother.complete().timetable(timetable).build();
        timingRepository.saveAndFlush(timingEntity);
    }
    
    @Test
    void getByIdWithTimingRelationship_ShouldReturnTimngsRelationship() {
        List<TimetableEntity> timetables = timetableRepository.getAllWithTimings();
        TimetableEntity persistedTimetable = timetables.iterator().next();
        
        assertEquals(timetable.getId(), 
                     persistedTimetable.getTimings().iterator().next().getId());
    }

    @Test
    void findById_ShouldReturnEntity() {
        TimetableEntity persistedTimetable = timetableRepository.findById(
                timetable.getId().intValue());
        assertEquals(timetable.getId(), persistedTimetable.getId());
    }
}
