package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.dto.LessonDTO;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Timetable;
import ua.com.foxminded.university.entity.Timing;
import ua.com.foxminded.university.entitymother.LessonMother;
import ua.com.foxminded.university.entitymother.TimetableMother;
import ua.com.foxminded.university.entitymother.TimingMother;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.TimetableRepository;
import ua.com.foxminded.university.repository.TimingRepository;
import ua.com.foxminded.university.service.impl.LessonServiceImpl;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext
class LessonServiceImplIntegrationTest {
    
    @Autowired
    private LessonServiceImpl lessonService;
    
    @Autowired
    private TimetableRepository timetableRepository;
    
    @Autowired
    private TimingRepository timingRepository;
    
    @Autowired
    private LessonRepository lessonRepository;
    
    private Timetable timetable;
    private Timing timing;
    private Lesson lesson;
    
    @BeforeTransaction
    void init() {
        timetable = TimetableMother.complete().build();
        timetableRepository.saveAndFlush(timetable);
        timing = TimingMother.complete().timetable(timetable).build();
        timingRepository.saveAndFlush(timing);
        lesson = LessonMother.complete().timetable(timetable).build();
        lessonRepository.saveAndFlush(lesson);
    }
    
    @Test
    void getById_ShouldAddPropertiesUsingUsingModelMapperConfiguration() 
            throws ServiceException {
        LessonDTO lessonModel = lessonService.getById(lesson.getId().intValue());
//        assertEquals(timingEntity.getStartTime(), lessonModel.getStartTime());
        assertEquals(888, lessonModel.getLessonOrder());
        
    }
}
