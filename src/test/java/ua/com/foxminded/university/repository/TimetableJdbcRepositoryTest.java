package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.university.config.RepositoryConfigTest;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.DayOfWeek;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TimetableEntity;

@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = RepositoryConfigTest.class)
@ExtendWith(SpringExtension.class)
class TimetableJdbcRepositoryTest {
    
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
    void findCourseById_ReceivingTimetableDatabaseData_CorrectReceivedData() 
            throws RepositoryException {
        TimetableEntity receivedTimetableData = timetableRepository.findCourseById(TIMETABLE_ID);
        
        assertEquals(COURSE_DESCRIPTION, receivedTimetableData.getCourse().getDescription());
        assertEquals(COURSE_ID, receivedTimetableData.getCourse().getId());
        assertEquals(COURSE_NAME, receivedTimetableData.getCourse().getName());
        assertEquals(COURSE_DESCRIPTION, receivedTimetableData.getDescription());
        assertEquals(END_TIME, receivedTimetableData.getEndTime());
        assertEquals(GROUP_ID, receivedTimetableData.getGroup().getId());
        assertEquals(START_TIME, receivedTimetableData.getStartTime());
        assertEquals(TIMETABLE_ID, receivedTimetableData.getId());
        assertEquals(WEEK_DAY, receivedTimetableData.getWeekDay().toString());
    }
        
    @Test
    void findGroupById_ReceivingTimetableDatabaseData_CorrectReceivedData() throws RepositoryException {
        TimetableEntity timetable = timetableRepository.findGroupById(TIMETABLE_ID);
        
        assertEquals(GROUP_NAME, timetable.getGroup().getName());
        assertEquals(COURSE_ID, timetable.getCourse().getId());
        assertEquals(TIMETABLE_DESCRIPTION, timetable.getDescription());
        assertEquals(END_TIME, timetable.getEndTime());
        assertEquals(GROUP_ID, timetable.getGroup().getId());
        assertEquals(TIMETABLE_ID, timetable.getId());
        assertEquals(START_TIME, timetable.getStartTime());
        assertEquals(WEEK_DAY, timetable.getWeekDay().toString());
    }
}
