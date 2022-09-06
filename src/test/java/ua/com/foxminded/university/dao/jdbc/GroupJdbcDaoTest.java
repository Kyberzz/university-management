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
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.jdbc.mapper.GroupMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.StudentMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.TimetableMapper;
import ua.com.foxminded.university.entity.GroupEntity;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = TestAppConfig.class)
@ExtendWith(SpringExtension.class)
class GroupJdbcDaoTest {
    
    private static final String GROUP_NAME_COLUMN = "name";
    private static final String GROUP_ID_COLUMN = "id";
    private static final String SELECT_GROUP_BY_ID = "test.selectGroupById";
    private static final String EXPECTED_WEEK_DAY = "MONDAY";
    private static final String EXPECTED_STUDENT_LAST_NAME = "Smith";
    private static final String EXPECTED_STUDENT_FIST_NAME = "Julitta";
    private static final String NEW_GROUP_NAME = "kt-53";
    private static final String EXPECTED_GROUP_NAME = "kt-52";
    private static final long EXPECTED_ENDTIME = 39360000;
    private static final long EXPECTED_START_TIME = 36360000;
    private static final int INSERTED_GROUP_ID = 3;
    private static final int EXPECTED_COURSE_ID = 1;
    private static final int EXPECTED_TIMETABLE_ID = 1;
    private static final int FIST_ELEMENT = 0;
    private static final int EXPECTED_STUDENT_ID = 2;
    private static final int GROUP_ID_NUMBER = 2;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private Environment groupQueries;
    
    @Autowired
    private GroupMapper groupMapper;
    
    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private TimetableMapper timetableMapper;
    
    @Test
    void insert_InsertingDataOfGroupToDatabase_DatabaseHasCorrectData() {
        GroupDao groupDao = new GroupJdbcDao(groupQueries, jdbcTemplate, groupMapper, 
                                             studentMapper, timetableMapper);
        GroupEntity group = new GroupEntity();
        group.setName(NEW_GROUP_NAME);
        groupDao.insert(group);
        String sqlSelectGroupById = groupQueries.getProperty(SELECT_GROUP_BY_ID);
        Map<String, Object> insertedGroup = jdbcTemplate.queryForMap(sqlSelectGroupById, 
                                                                     INSERTED_GROUP_ID);
        assertEquals(NEW_GROUP_NAME, insertedGroup.get(GROUP_NAME_COLUMN));
    }
    
    @Test
    void getById_ReceivingDatabaseDataOfGroup_CorrectReceivedData() {
        GroupDao groupDao = new GroupJdbcDao(groupQueries, jdbcTemplate, groupMapper, 
                                             studentMapper, timetableMapper);
        GroupEntity group = groupDao.getById(GROUP_ID_NUMBER);
        
        assertEquals(GROUP_ID_NUMBER, group.getId());
        assertEquals(EXPECTED_GROUP_NAME, group.getName());
    }
    
    @Test
    void update_UpdatingDatabaseDataOfGroup_DatabaseHasCorrectData() {
        GroupDao groupDao = new GroupJdbcDao(groupQueries, jdbcTemplate, groupMapper, 
                                             studentMapper, timetableMapper);
        GroupEntity group = new GroupEntity();
        group.setId(GROUP_ID_NUMBER);
        group.setName(EXPECTED_GROUP_NAME);
        groupDao.update(group);
        String sqlSelectGroupById = groupQueries.getProperty(SELECT_GROUP_BY_ID);
        
        Map<String, Object> updatedGroup = jdbcTemplate.queryForMap(sqlSelectGroupById,
                                                                    GROUP_ID_NUMBER);
        assertEquals(group.getId(), updatedGroup.get(GROUP_ID_COLUMN));
        assertEquals(group.getName(), updatedGroup.get(GROUP_NAME_COLUMN));
    }
    
    @Test
    void deleteById_DeletingDatabaseDataOfGroup_DatabaseHasNoData() {
        GroupDao groupDao = new GroupJdbcDao(groupQueries, jdbcTemplate, groupMapper, 
                                             studentMapper, timetableMapper);
        groupDao.deleteById(GROUP_ID_NUMBER);
        String sqlSelectGroupById = groupQueries.getProperty(SELECT_GROUP_BY_ID);
        jdbcTemplate.query(sqlSelectGroupById,
                           preparedStatement -> preparedStatement.setInt(1, GROUP_ID_NUMBER),
                           resultSet -> {
                               assertTrue(!resultSet.next());
                           });
    }
    
    @Test
    void getTimetableListByGroupId_GettingDataFromDatabase_CorrectRecevedData() {
        GroupDao groupDao = new GroupJdbcDao(groupQueries, jdbcTemplate, groupMapper, 
                                             studentMapper, timetableMapper);
        GroupEntity receivedGroupData = groupDao.getTimetableListByGroupId(GROUP_ID_NUMBER);
        
        assertEquals(GROUP_ID_NUMBER, receivedGroupData.getId());
        assertEquals(EXPECTED_GROUP_NAME, receivedGroupData.getName());
        assertEquals(EXPECTED_TIMETABLE_ID, receivedGroupData.getTimetableList()
                                                             .get(FIST_ELEMENT)
                                                             .getId());
        assertEquals(EXPECTED_START_TIME, receivedGroupData.getTimetableList()
                                                           .get(FIST_ELEMENT)
                                                           .getStartTime());
        assertEquals(EXPECTED_ENDTIME, receivedGroupData.getTimetableList()
                                                        .get(FIST_ELEMENT)
                                                        .getEndTime());
        assertEquals(EXPECTED_WEEK_DAY, receivedGroupData.getTimetableList()
                                                         .get(FIST_ELEMENT)
                                                         .getWeekDay()
                                                         .toString());
        assertEquals(EXPECTED_COURSE_ID, receivedGroupData.getTimetableList()
                                                       .get(FIST_ELEMENT)
                                                       .getCourse()
                                                       .getId());  
    }
    
    @Test
    void getStudentListByGroupId_GettingDataFromDatabase_CorrectReceivedData() {
        GroupDao groupDao = new GroupJdbcDao(groupQueries, jdbcTemplate, groupMapper, 
                                             studentMapper, timetableMapper);
        GroupEntity receivedGroupData = groupDao.getStudentListByGroupId(GROUP_ID_NUMBER);
        
        assertEquals(GROUP_ID_NUMBER, receivedGroupData.getId());
        assertEquals(EXPECTED_GROUP_NAME, receivedGroupData.getName());
        assertEquals(EXPECTED_STUDENT_ID, receivedGroupData.getStudentList()
                                                            .get(FIST_ELEMENT)
                                                            .getId());
        assertEquals(EXPECTED_STUDENT_FIST_NAME, receivedGroupData.getStudentList()
                                                                   .get(FIST_ELEMENT)
                                                                   .getFirstName());
        assertEquals(EXPECTED_STUDENT_LAST_NAME, receivedGroupData.getStudentList()
                                                                   .get(FIST_ELEMENT)
                                                                   .getLastName());
        assertEquals(GROUP_ID_NUMBER, receivedGroupData.getStudentList()
                                                           .get(FIST_ELEMENT)
                                                           .getGroup()
                                                           .getId());
    }
}
