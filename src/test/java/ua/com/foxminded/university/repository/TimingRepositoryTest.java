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

@DataJpaTest
@ActiveProfiles("test")
class TimingRepositoryTest {
    
    @Autowired
    private TimingRepository timingRepository;
    
    @Autowired
    private TimetableRepository timetableRepository;
    
    private Timing timing;
    private Timetable timetable;
    
    @BeforeTransaction
    void init() {
        timetable = TimetableMother.complete().build();
        timetableRepository.saveAndFlush(timetable);
        timing = TimingMother.complete().timetable(timetable).build();
        timingRepository.saveAndFlush(timing);
    }

    @Test
    void findByTimetableId_ShouldReturnTimings() {
        List<Timing> timings = timingRepository.findByTimetableId(timetable.getId());
        assertEquals(timings.iterator().next(), timing);
    }
}
