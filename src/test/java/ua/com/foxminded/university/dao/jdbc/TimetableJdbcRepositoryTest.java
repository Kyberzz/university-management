package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManagerFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.university.config.AppConfigTest;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.WeekDayEntity;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.TimetableRepository;
import ua.com.foxminded.university.repository.jdbc.TimetableJdbcRepository;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = AppConfigTest.class)
@ExtendWith(SpringExtension.class)
class TimetableJdbcRepositoryTest {
    
    private static final String COURSE_NAME = "Physics";
    private static final String EXPECTED_WEEK_DAY = "MONDAY";
    private static final String COURSE_DESCRIPTION = "some description";
    private static final String TIMETABLE_DESCRIPTION = "some description";
    private static final long START_TIME = 36360000;
    private static final long END_TIME = 39360000;
    private static final int TEACHER_ID = 1;
    private static final int GROUP_ID_NUMBER = 2;
    private static final int COURSE_ID_NUMBER = 1;
    private static final int TIMETABLE_ID = 2;
    private static final int EXPECTED_TIMETABLE_ID_NUMBER = 6;
    private static final int TIMETABLE_ID_NUMBER = 1;
    
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private Environment environment;
    
    @Test
    void insert_InsertingTimetableDataToDatabase_DatabaseHasCorrectData() throws RepositoryException {
        TimetableEntity timetable = new TimetableEntity();
        CourseEntity course = new CourseEntity();
        course.setId(COURSE_ID_NUMBER);
        timetable.setCourse(course);
        timetable.setDescription(TIMETABLE_DESCRIPTION);
        timetable.setEndTime(END_TIME);
        GroupEntity group = new GroupEntity();
        group.setId(GROUP_ID_NUMBER);
        timetable.setGroup(group);
        timetable.setStartTime(START_TIME);
        timetable.setWeekDay(WeekDayEntity.MONDAY);
       
        TimetableRepository timetableDao = new TimetableJdbcRepository(entityManagerFactory, environment);
        TimetableEntity timetableWithId = timetableDao.insert(timetable);
        
        TimetableEntity insertedTimetable = entityManagerFactory.createEntityManager()
                .find(TimetableEntity.class, EXPECTED_TIMETABLE_ID_NUMBER);
        
        assertEquals(COURSE_ID_NUMBER, insertedTimetable.getCourse().getId());
        assertEquals(TIMETABLE_DESCRIPTION, insertedTimetable.getDescription());
        assertEquals(END_TIME, insertedTimetable.getEndTime());
        assertEquals(GROUP_ID_NUMBER, insertedTimetable.getGroup().getId());
        assertEquals(EXPECTED_TIMETABLE_ID_NUMBER, timetableWithId.getId());
        assertEquals(START_TIME, insertedTimetable.getStartTime());
        assertEquals(EXPECTED_WEEK_DAY, insertedTimetable.getWeekDay().toString());
    }
    
    @Test
    void getById_ReceivingTimetableDatabaseData_CorrectReceivedData() throws RepositoryException {
        TimetableRepository timetableDao = new TimetableJdbcRepository(entityManagerFactory, environment);
        TimetableEntity receivedTimetable = timetableDao.getById(TIMETABLE_ID_NUMBER);
        
        assertEquals(COURSE_ID_NUMBER, receivedTimetable.getCourse().getId());
        assertEquals(TIMETABLE_DESCRIPTION, receivedTimetable.getDescription());
        assertEquals(END_TIME, receivedTimetable.getEndTime());
        assertEquals(GROUP_ID_NUMBER, receivedTimetable.getGroup().getId());
        assertEquals(TIMETABLE_ID_NUMBER, receivedTimetable.getId());
        assertEquals(START_TIME, receivedTimetable.getStartTime());
        assertEquals(EXPECTED_WEEK_DAY, receivedTimetable.getWeekDay().toString());
    }
    
   /* 
    @Test
    void update_DeletingForeingKeys_TimetableHasNoForeingKeys() throws RepositoryException {
        TimetableEntity timetable = new TimetableEntity();
        timetable.setId(TIMETABLE_ID_NUMBER);
      //  timetable.setCourse(course);
        timetable.setEndTime(END_TIME);
    //    timetable.setGroup(new GroupEntity());
        timetable.setStartTime(START_TIME);
        timetable.setWeekDay(WeekDayEntity.valueOf(EXPECTED_WEEK_DAY));
        
        TimetableJdbcRepository timetableDao = new TimetableJdbcRepository(entityManagerFactory, environment);
        timetableDao.update(timetable);
        
        TimetableEntity updatedTimetable = entityManagerFactory.createEntityManager()
                .find(TimetableEntity.class, TIMETABLE_ID_NUMBER);
        
        assertNull(updatedTimetable.getDescription());
        assertNull(updatedTimetable.getGroup());
        assertNull(updatedTimetable.getCourse());
    }

    @Test
    void update_UdatingTimetableDatabaseData_DatabaseHasUpdatedData() throws RepositoryException {
        TimetableEntity timetable = new TimetableEntity();
        timetable.setId(TIMETABLE_ID);
        CourseEntity course = new CourseEntity();
        course.setId(COURSE_ID_NUMBER);
        timetable.setCourse(course);
        timetable.setDescription(TIMETABLE_DESCRIPTION);
        timetable.setEndTime(END_TIME);
        GroupEntity group = new GroupEntity();
        group.setId(GROUP_ID_NUMBER);
        timetable.setGroup(group);
        timetable.setStartTime(START_TIME);
        timetable.setWeekDay(WeekDayEntity.valueOf(EXPECTED_WEEK_DAY));
        TimetableRepository timetableDao = new TimetableJdbcRepository(entityManagerFactory, environment);
        timetableDao.update(timetable);

        TimetableEntity updatedTimetable = entityManagerFactory.createEntityManager()
                                                               .find(TimetableEntity.class, TIMETABLE_ID);
       
        assertEquals(COURSE_ID_NUMBER, updatedTimetable.getCourse().getId());
        assertEquals(TIMETABLE_DESCRIPTION, updatedTimetable.getDescription());
        assertEquals(END_TIME, updatedTimetable.getEndTime());
        assertEquals(GROUP_ID_NUMBER, updatedTimetable.getGroup().getId());
        assertEquals(TIMETABLE_ID, updatedTimetable.getId());
        assertEquals(START_TIME, updatedTimetable.getStartTime());
        assertEquals(EXPECTED_WEEK_DAY, updatedTimetable.getWeekDay().toString());
    }
    */
    @Test
    void deleteById_DeletingTimetableDatabaseData_DatabaseHaNoData() throws RepositoryException {
        TimetableRepository timetableDao = new TimetableJdbcRepository(entityManagerFactory, environment);
        timetableDao.deleteById(TIMETABLE_ID_NUMBER);
        TimetableEntity timetable = new TimetableEntity();
        timetable.setId(TIMETABLE_ID_NUMBER);
        boolean containStatus = entityManagerFactory.createEntityManager()
                                                    .contains(timetable);
        assertFalse(containStatus);
    }
    
    @Test
    void getCourseByTimetableId_ReceivingTimetableDatabaseData_CorrectReceivedData() 
            throws RepositoryException {
        TimetableRepository timetableDao = new TimetableJdbcRepository(entityManagerFactory, environment);
        TimetableEntity receivedTimetableData = timetableDao.getCourseByTimetableId(TIMETABLE_ID_NUMBER);
        
        assertEquals(COURSE_DESCRIPTION, receivedTimetableData.getCourse().getDescription());
        assertEquals(COURSE_ID_NUMBER, receivedTimetableData.getCourse().getId());
        assertEquals(COURSE_NAME, receivedTimetableData.getCourse().getName());
        assertEquals(TEACHER_ID, receivedTimetableData.getCourse().getTeacher().getId());
        assertEquals(COURSE_DESCRIPTION, receivedTimetableData.getDescription());
        assertEquals(END_TIME, receivedTimetableData.getEndTime());
        assertEquals(GROUP_ID_NUMBER, receivedTimetableData.getGroup().getId());
        assertEquals(START_TIME, receivedTimetableData.getStartTime());
        assertEquals(TIMETABLE_ID_NUMBER, receivedTimetableData.getId());
        assertEquals(EXPECTED_WEEK_DAY, receivedTimetableData.getWeekDay().toString());
    }
        
    @Test
    void getGroupByTimetableId_ReceivingTimetableDatabaseData_CorrectReceivedData() throws RepositoryException {
        TimetableRepository timetableDao = new TimetableJdbcRepository(entityManagerFactory, environment);
        TimetableEntity timetable = timetableDao.getGroupByTimetableId(TIMETABLE_ID_NUMBER);
        
        assertEquals(COURSE_ID_NUMBER, timetable.getCourse().getId());
        assertEquals(TIMETABLE_DESCRIPTION, timetable.getDescription());
        assertEquals(END_TIME, timetable.getEndTime());
        assertEquals(GROUP_ID_NUMBER, timetable.getGroup().getId());
        assertEquals(TIMETABLE_ID_NUMBER, timetable.getId());
        assertEquals(START_TIME, timetable.getStartTime());
        assertEquals(EXPECTED_WEEK_DAY, timetable.getWeekDay().toString());
    }
}
