package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.university.config.TestAppConfig;
import ua.com.foxminded.university.dao.TimetableDao;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.WeekDayEntity;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = TestAppConfig.class)
@ExtendWith(SpringExtension.class)
class TimetableJdbcDaoTest {
    
    private static final String WEEK_DAY_COLUMN = "week_day";
    private static final String START_TIME_COLUMN = "start_time";
    private static final String TIMETABLE_ID_COLUMN = "id";
    private static final String GROUP_ID_COLUMN = "group_id";
    private static final String END_TIME_COLUMN = "end_time";
    private static final String TIMETABLE_DESCRIPTION_COLUMN = "description";
    private static final String COURSE_ID_COLUMN = "course_id";
    private static final String SELECT_TIMETABLE_BY_ID = "test.selectTimetableById";
    private static final String COURSE_NAME = "Physics";
    private static final String WEEK_DAY = "MONDAY";
    private static final String COURSE_DESCRIPTION = "some description";
    private static final String TIMETABLE_DESCRIPTION = "some description";
    private static final long START_TIME = 36360000;
    private static final long END_TIME = 39360000;
    private static final int TEACHER_ID = 1;
    private static final int GROUP_ID_NUMBER = 2;
    private static final int COURSE_ID_NUMBER = 1;
    private static final int TIMETABLE_ID = 2;
    private static final int TIMETABLE_ID_NUMBER = 1;
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    Environment timetableQueries;

    @Test
    void insert_InsertingTimetableDataToDatabase_DatabaseHasCorrectData() {
        TimetableEntity timetable = new TimetableEntity();
        timetable.setCourse(new CourseEntity(COURSE_ID_NUMBER));
        timetable.setDescription(TIMETABLE_DESCRIPTION);
        timetable.setEndTime(END_TIME);
        timetable.setGroup(new GroupEntity(GROUP_ID_NUMBER));
        timetable.setId(TIMETABLE_ID_NUMBER);
        timetable.setStartTime(START_TIME);
        timetable.setWeekDay(WeekDayEntity.valueOf(WEEK_DAY));
       
        TimetableDao timetableDao = new TimetableJdbcDao(jdbcTemplate, timetableQueries);
        timetableDao.insert(timetable);
        Map<String, Object> insertedTimetable = jdbcTemplate.queryForMap(
                timetableQueries.getProperty(SELECT_TIMETABLE_BY_ID), 
                TIMETABLE_ID_NUMBER);
        
        assertEquals(COURSE_ID_NUMBER, insertedTimetable.get(COURSE_ID_COLUMN));
        assertEquals(TIMETABLE_DESCRIPTION, insertedTimetable.get(TIMETABLE_DESCRIPTION_COLUMN));
        assertEquals(END_TIME, insertedTimetable.get(END_TIME_COLUMN));
        assertEquals(GROUP_ID_NUMBER, insertedTimetable.get(GROUP_ID_COLUMN));
        assertEquals(TIMETABLE_ID_NUMBER, insertedTimetable.get(TIMETABLE_ID_COLUMN));
        assertEquals(START_TIME, insertedTimetable.get(START_TIME_COLUMN));
        assertEquals(WEEK_DAY, insertedTimetable.get(WEEK_DAY_COLUMN));
    }
    
    @Test
    void getById_ReceivingTimetableDatabaseData_CorrectReceivedData() {
        TimetableDao timetableDao = new TimetableJdbcDao(jdbcTemplate, timetableQueries);
        TimetableEntity receivedTimetable = timetableDao.getById(TIMETABLE_ID_NUMBER);
        
        assertEquals(COURSE_ID_NUMBER, receivedTimetable.getCourse().getId());
        assertEquals(TIMETABLE_DESCRIPTION, receivedTimetable.getDescription());
        assertEquals(END_TIME, receivedTimetable.getEndTime());
        assertEquals(GROUP_ID_NUMBER, receivedTimetable.getGroup().getId());
        assertEquals(TIMETABLE_ID_NUMBER, receivedTimetable.getId());
        assertEquals(START_TIME, receivedTimetable.getStartTime());
        assertEquals(WEEK_DAY, receivedTimetable.getWeekDay().toString());
    }
    
    @Test
    void update_UdatingTimetableDatabaseData_DatabaseHasUpdatedData() {
        TimetableEntity timetable = new TimetableEntity();
        timetable.setCourse(new CourseEntity(COURSE_ID_NUMBER));
        timetable.setDescription(TIMETABLE_DESCRIPTION);
        timetable.setEndTime(END_TIME);
        timetable.setGroup(new GroupEntity(GROUP_ID_NUMBER));
        timetable.setId(TIMETABLE_ID);
        timetable.setStartTime(START_TIME);
        timetable.setWeekDay(WeekDayEntity.valueOf(WEEK_DAY));
        TimetableDao timetableDao = new TimetableJdbcDao(jdbcTemplate, timetableQueries);
        timetableDao.update(timetable);
        Map<String, Object> updatedTimetable = jdbcTemplate.queryForMap(timetableQueries.getProperty(
                SELECT_TIMETABLE_BY_ID), 
                TIMETABLE_ID);
       
        assertEquals(COURSE_ID_NUMBER, updatedTimetable.get(COURSE_ID_COLUMN));
        assertEquals(TIMETABLE_DESCRIPTION, updatedTimetable.get(TIMETABLE_DESCRIPTION_COLUMN));
        assertEquals(END_TIME, updatedTimetable.get(END_TIME_COLUMN));
        assertEquals(GROUP_ID_NUMBER, updatedTimetable.get(GROUP_ID_COLUMN));
        assertEquals(TIMETABLE_ID, updatedTimetable.get(TIMETABLE_ID_COLUMN));
        assertEquals(START_TIME, updatedTimetable.get(START_TIME_COLUMN));
        assertEquals(WEEK_DAY, updatedTimetable.get(WEEK_DAY_COLUMN));
    }
    
    @Test
    void deleteById_DeletingTimetableDatabaseData_DatabaseHaNoData() {
        TimetableDao timetableDao = new TimetableJdbcDao(jdbcTemplate, timetableQueries);
        timetableDao.deleteById(TIMETABLE_ID_NUMBER);
        jdbcTemplate.query(timetableQueries.getProperty(SELECT_TIMETABLE_BY_ID), 
                                                        preparedStatement -> preparedStatement.setInt(1, TIMETABLE_ID_NUMBER),
                                                        resultSet -> {
                                                            assertTrue(!resultSet.next());
                                                        });
    }
    
    @Test
    void getCourseByTimetableId_ReceivingTimetableDatabaseData_CorrectReceivedData() {
        TimetableDao timetableDao = new TimetableJdbcDao(jdbcTemplate, timetableQueries);
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
        assertEquals(WEEK_DAY, receivedTimetableData.getWeekDay().toString());
    }
    
    @Test
    void getGroupByTimetableId_ReceivingTimetableDatabaseData_CorrectReceivedData() {
        TimetableDao timetableDao = new TimetableJdbcDao(jdbcTemplate, timetableQueries);
        TimetableEntity timetable = timetableDao.getGroupByTimetableId(TIMETABLE_ID_NUMBER);
        
        assertEquals(COURSE_ID_NUMBER, timetable.getCourse().getId());
        assertEquals(TIMETABLE_DESCRIPTION, timetable.getDescription());
        assertEquals(END_TIME, timetable.getEndTime());
        assertEquals(GROUP_ID_NUMBER, timetable.getGroup().getId());
        assertEquals(TIMETABLE_ID_NUMBER, timetable.getId());
        assertEquals(START_TIME, timetable.getStartTime());
        assertEquals(WEEK_DAY, timetable.getWeekDay().toString());
    }
}
