package ua.com.foxminded.university.repository.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

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
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.TimetableRepository;

@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = RepositoryConfigTest.class)
@ExtendWith(SpringExtension.class)
class TimetableJdbcRepositoryTest {
    
    private static final String GROUP_NAME = "kt-77";
    private static final String COURSE_NAME = "Physics";
    private static final String UDATED_WEEK_DAY = "MONDAY";
    private static final String WEEK_DAY = "SUNDAY";
    private static final String COURSE_DESCRIPTION = "some description";
    private static final String NEW_TIMETABLE_DESCRIPTION = "new some description";
    private static final String TIMETABLE_DESCRIPTION = "some description";
    private static final long START_TIME = 36360000;
    private static final long END_TIME = 39360000;
    private static final int GROUP_ID = 1;
    private static final int NEW_GROUP_ID = 2;
    private static final int NEW_COURSE_ID = 2;
    private static final int COURSE_ID = 1;
    private static final int NEW_TIMETABLE_ID = 2;
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
    void insert_InsertingTimetableDataToDatabase_DatabaseHasCorrectData() throws RepositoryException {
        GroupEntity group = new GroupEntity();
        group.setId(GROUP_ID);
        
        CourseEntity course = new CourseEntity();
        course.setId(COURSE_ID);
        
        TimetableEntity timetable = new TimetableEntity();
        timetable.setStartTime(START_TIME);
        timetable.setEndTime(END_TIME);
        timetable.setDescription(TIMETABLE_DESCRIPTION);
        timetable.setWeekDay(DayOfWeek.SUNDAY);
        timetable.setGroup(group);
        timetable.setCourse(course);
       
        TimetableEntity timetableWithId = timetableRepository.save(timetable);
        
        TimetableEntity insertedTimetable = entityManager.find(TimetableEntity.class, 
                                                               NEW_TIMETABLE_ID);
        
        assertEquals(COURSE_ID, insertedTimetable.getCourse().getId());
        assertEquals(TIMETABLE_DESCRIPTION, insertedTimetable.getDescription());
        assertEquals(END_TIME, insertedTimetable.getEndTime());
        assertEquals(GROUP_ID, insertedTimetable.getGroup().getId());
        assertEquals(NEW_TIMETABLE_ID, timetableWithId.getId());
        assertEquals(START_TIME, insertedTimetable.getStartTime());
        assertEquals(WEEK_DAY, insertedTimetable.getWeekDay().toString());
    }
    
    @Test
    void getById_ReceivingTimetableDatabaseData_CorrectReceivedData() throws RepositoryException {
        Optional<TimetableEntity> receivedTimetable = timetableRepository.findById(TIMETABLE_ID);
        
        assertEquals(COURSE_ID, receivedTimetable.get().getCourse().getId());
        assertEquals(TIMETABLE_DESCRIPTION, receivedTimetable.get().getDescription());
        assertEquals(END_TIME, receivedTimetable.get().getEndTime());
        assertEquals(GROUP_ID, receivedTimetable.get().getGroup().getId());
        assertEquals(TIMETABLE_ID, receivedTimetable.get().getId());
        assertEquals(START_TIME, receivedTimetable.get().getStartTime());
        assertEquals(WEEK_DAY, receivedTimetable.get().getWeekDay().toString());
    }

    @Test
    void update_DeletingForeingKeys_TimetableHasNoForeingKeys() throws RepositoryException {
        TimetableEntity timetable = new TimetableEntity();
        timetable.setId(TIMETABLE_ID);
        timetable.setCourse(null);
        timetable.setEndTime(END_TIME);
        timetable.setGroup(null);
        timetable.setStartTime(START_TIME);
        timetable.setWeekDay(DayOfWeek.MONDAY);
        
        timetableRepository.save(timetable);
        
        TimetableEntity updatedTimetable = entityManager.find(TimetableEntity.class, TIMETABLE_ID);
        
        assertNull(updatedTimetable.getDescription());
        assertNull(updatedTimetable.getGroup());
        assertNull(updatedTimetable.getCourse());
    }

    @Test
    void update_UdatingTimetableDatabaseData_DatabaseHasUpdatedData() throws RepositoryException {
        CourseEntity course = new CourseEntity();
        course.setId(NEW_COURSE_ID);
       
        GroupEntity group = new GroupEntity();
        group.setId(NEW_GROUP_ID);
        
        TimetableEntity timetable = new TimetableEntity();
        timetable.setId(TIMETABLE_ID);
        timetable.setStartTime(START_TIME);
        timetable.setEndTime(END_TIME);
        timetable.setDescription(NEW_TIMETABLE_DESCRIPTION);
        timetable.setCourse(course);
        timetable.setGroup(group);
        timetable.setWeekDay(DayOfWeek.valueOf(UDATED_WEEK_DAY));
        
        timetableRepository.save(timetable);

        TimetableEntity updatedTimetable = entityManager.find(TimetableEntity.class, TIMETABLE_ID);
       
        assertEquals(NEW_COURSE_ID, updatedTimetable.getCourse().getId());
        assertEquals(NEW_TIMETABLE_DESCRIPTION, updatedTimetable.getDescription());
        assertEquals(END_TIME, updatedTimetable.getEndTime());
        assertEquals(NEW_GROUP_ID, updatedTimetable.getGroup().getId());
        assertEquals(TIMETABLE_ID, updatedTimetable.getId());
        assertEquals(START_TIME, updatedTimetable.getStartTime());
        assertEquals(UDATED_WEEK_DAY, updatedTimetable.getWeekDay().toString());
    }
    
    @Test
    void deleteById_DeletingTimetableDatabaseData_DatabaseHaNoData() throws RepositoryException {
        timetableRepository.deleteById(TIMETABLE_ID);
        
        TimetableEntity timetable = new TimetableEntity();
        timetable.setId(TIMETABLE_ID);
        boolean containStatus = entityManager.contains(timetable);
        assertFalse(containStatus);
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
