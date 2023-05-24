package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.BeforeTransaction;

import ua.com.foxminded.university.dto.TimingDTO;
import ua.com.foxminded.university.entity.Timetable;
import ua.com.foxminded.university.entity.Timing;
import ua.com.foxminded.university.entitymother.TimetableMother;
import ua.com.foxminded.university.entitymother.TimingMother;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.repository.TimetableRepository;
import ua.com.foxminded.university.repository.TimingRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TimingServiceImplIntegreaionTest {
    
    @Autowired
    private TimingService timingService;
    
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
    void getById_ShouldAddEndTimeUsingModelMapperConfig() throws ServiceException {
        TimingDTO timingDto = timingService.getById(timing.getId());
        assertEquals(timing.getStartTime().plus(timing.getLessonDuration()), 
                     timingDto.getEndTime());
        assertEquals(timing.getStartTime(), timingDto.getStartTime());
    }
}
