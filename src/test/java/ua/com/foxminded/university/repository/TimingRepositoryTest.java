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

@DataJpaTest
@ActiveProfiles("test")
class TimingRepositoryTest {
    
    @Autowired
    private TimingRepository timingRepository;
    
    @Autowired
    private TimetableRepository timetableRepository;
    
    private TimingEntity timingEntity;
    private TimetableEntity timetableEntity;
    
    @BeforeTransaction
    void init() {
        timetableEntity = TimetableEntityMother.complete().build();
        timetableRepository.saveAndFlush(timetableEntity);
        timingEntity = TimingEntityMother.complete().timetable(timetableEntity).build();
        timingRepository.saveAndFlush(timingEntity);
    }

    @Test
    void test() {
        List<TimingEntity> timings = timingRepository.findByTimetableId(timetableEntity.getId());
        assertEquals(timings.iterator().next(), timingEntity);
    }
}
