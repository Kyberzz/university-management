package ua.com.foxminded.university.repository.impl;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.config.RepositoryConfigTest;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.DayOfWeek;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.TimetableRepository;

@Transactional
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = RepositoryConfigTest.class)
@ExtendWith(SpringExtension.class)
class TimetableCustomRepositoryImplTest {
    
    private static final String GROUP_NAME = "kt-77";
    private static final String COURSE_NAME = "Physics";
    private static final String WEEK_DAY = "SUNDAY";
    private static final String COURSE_DESCRIPTION = "some description";
    private static final String TIMETABLE_DESCRIPTION = "some description";
    private static final long START_TIME = 36360000;
    private static final long END_TIME = 39360000;
    private static final int GROUP_ID = 1;
    private static final int COURSE_ID = 1;
    private static final int TIMETABLE_ID = 1;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private TimetableRepository timetableRepository;
    
    @BeforeEach
    void init() {
        CourseEntity course = new CourseEntity();
        course.setDescription(COURSE_DESCRIPTION);
        course.setName(COURSE_NAME);
        entityManager.persist(course);
        
        CourseEntity secondCourse = new CourseEntity();
        entityManager.persist(secondCourse);
        
        GroupEntity group = new GroupEntity();
        group.setName(GROUP_NAME);
        entityManager.persist(group);
        
        GroupEntity secondGroup = new GroupEntity();
        entityManager.persist(secondGroup);
        
        TimetableEntity timetable = new TimetableEntity();
        timetable.setStartTime(START_TIME);
        timetable.setEndTime(END_TIME);
        timetable.setDescription(TIMETABLE_DESCRIPTION);
        timetable.setWeekDay(DayOfWeek.valueOf(WEEK_DAY));
        timetable.setCourse(course);
        timetable.setGroup(group);
        entityManager.persist(timetable);
    }
    
    
    @Test
    void findById_GettingTimetableById_CorrectRetrievedData() throws RepositoryException {
        TimetableEntity timetable = timetableRepository.findById(TIMETABLE_ID);
        
        assertEquals(TIMETABLE_ID, timetable.getId());
        assertEquals(TIMETABLE_DESCRIPTION, timetable.getDescription());
        assertEquals(START_TIME, timetable.getStartTime());
        assertEquals(END_TIME, timetable.getEndTime());
        assertEquals(COURSE_ID, timetable.getCourse().getId());
        assertEquals(GROUP_ID, timetable.getGroup().getId());
        assertEquals(WEEK_DAY, timetable.getWeekDay().toString());
    }

}
