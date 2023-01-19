package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.university.config.RepositoryTestConfig;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.exception.RepositoryException;

@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = RepositoryTestConfig.class)
@ExtendWith(SpringExtension.class)
class TimetableRepositoryTest {
    
    private static final String GROUP_NAME = "kt-77";
    private static final String COURSE_NAME = "Physics";
    private static final String WEEK_DAY = "SUNDAY";
    private static final String COURSE_DESCRIPTION = "some description";
    private static final String TIMETABLE_DESCRIPTION = "some description";
    private static final int MINUTE = 0;
    private static final int START_TIME = 8;
    private static final int END_TIME = 9;
    private static final int GROUP_ID = 1;
    private static final int COURSE_ID = 1;
    private static final int TIMETABLE_ID = 1;
    
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
        timetable.setStartTime(LocalTime.of(START_TIME, MINUTE));
        timetable.setEndTime(LocalTime.of(END_TIME, MINUTE));
        timetable.setDescription(TIMETABLE_DESCRIPTION);
        timetable.setDayOfWeek(DayOfWeek.valueOf(WEEK_DAY));
        timetable.setCourse(course);
        timetable.setGroup(group);
        entityManager.persist(timetable);
    }
    
    @Test
    void findCourseById_ReceivingTimetableDatabaseData_CorrectReceivedData() 
            throws RepositoryException {
        TimetableEntity receivedTimetableData = timetableRepository.findCourseById(TIMETABLE_ID);
        
        assertEquals(COURSE_DESCRIPTION, receivedTimetableData.getCourse().getDescription());
        assertEquals(COURSE_ID, receivedTimetableData.getCourse().getId());
        assertEquals(COURSE_NAME, receivedTimetableData.getCourse().getName());
        assertEquals(COURSE_DESCRIPTION, receivedTimetableData.getDescription());
        assertEquals(LocalTime.of(END_TIME, MINUTE), receivedTimetableData.getEndTime());
        assertEquals(GROUP_ID, receivedTimetableData.getGroup().getId());
        assertEquals(LocalTime.of(START_TIME, MINUTE), receivedTimetableData.getStartTime());
        assertEquals(TIMETABLE_ID, receivedTimetableData.getId());
        assertEquals(WEEK_DAY, receivedTimetableData.getDayOfWeek().toString());
    }
        
    @Test
    void findGroupById_ReceivingTimetableDatabaseData_CorrectReceivedData() throws RepositoryException {
        TimetableEntity timetable = timetableRepository.findGroupById(TIMETABLE_ID);
        
        assertEquals(GROUP_NAME, timetable.getGroup().getName());
        assertEquals(COURSE_ID, timetable.getCourse().getId());
        assertEquals(TIMETABLE_DESCRIPTION, timetable.getDescription());
        assertEquals(LocalTime.of(END_TIME, MINUTE), timetable.getEndTime());
        assertEquals(GROUP_ID, timetable.getGroup().getId());
        assertEquals(TIMETABLE_ID, timetable.getId());
        assertEquals(LocalTime.of(START_TIME, MINUTE), timetable.getStartTime());
        assertEquals(WEEK_DAY, timetable.getDayOfWeek().toString());
    }
    
    @Test
    void findById_GettingTimetableById_CorrectRetrievedData() throws RepositoryException {
        TimetableEntity timetable = timetableRepository.findById(TIMETABLE_ID);
        
        assertEquals(TIMETABLE_ID, timetable.getId());
        assertEquals(TIMETABLE_DESCRIPTION, timetable.getDescription());
        assertEquals(LocalTime.of(START_TIME, MINUTE), timetable.getStartTime());
        assertEquals(LocalTime.of(END_TIME, MINUTE), timetable.getEndTime());
        assertEquals(COURSE_ID, timetable.getCourse().getId());
        assertEquals(GROUP_ID, timetable.getGroup().getId());
        assertEquals(WEEK_DAY, timetable.getDayOfWeek().toString());
    }
}
