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

import ua.com.foxminded.university.entity.LessonEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.TimingEntity;
import ua.com.foxminded.university.entitymother.LessonEntityMother;
import ua.com.foxminded.university.entitymother.TimetableEntityMother;
import ua.com.foxminded.university.entitymother.TimingEntityMother;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.LessonModel;
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
    
    private TimetableEntity timetableEntity;
    private TimingEntity timingEntity;
    private LessonEntity lessonEntity;
    
    @BeforeTransaction
    void init() {
        timetableEntity = TimetableEntityMother.complete().build();
        timetableRepository.saveAndFlush(timetableEntity);
        timingEntity = TimingEntityMother.complete().timetable(timetableEntity).build();
        timingRepository.saveAndFlush(timingEntity);
        lessonEntity = LessonEntityMother.complete().timetable(timetableEntity).build();
        lessonRepository.saveAndFlush(lessonEntity);
    }
    
    @Test
    void getById_ShouldAddPropertiesUsingUsingModelMapperConfiguration() 
            throws ServiceException {
        LessonModel lessonModel = lessonService.getById(lessonEntity.getId().intValue());
        assertEquals(timingEntity.getStartTime(), lessonModel.getStartTime());
        
    }
}
