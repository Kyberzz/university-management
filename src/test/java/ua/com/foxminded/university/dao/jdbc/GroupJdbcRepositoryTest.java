package ua.com.foxminded.university.dao.jdbc;

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
import ua.com.foxminded.university.entity.DayOfWeek;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.GroupRepository;

@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = RepositoryConfigTest.class)
@ExtendWith(SpringExtension.class)
class GroupJdbcRepositoryTest {
    
    private static final String TIMETABLE_DESCRIPTION = "some timetable description";
    private static final String WEEK_DAY = "MONDAY";
    private static final String LAST_NAME = "Smith";
    private static final String FIST_NAME = "Alex";
    private static final String NEW_GROUP_NAME = "lt";
    private static final String GROUP_NAME = "rs-01";
    private static final int NEW_GROUP_ID = 2;
    private static final int STUDENT_ID = 1;
    private static final int FIRST_ELEMENT = 0;
    private static final int TIMETABLE_ID = 1;
    private static final int GROUP_ID = 1;
    private static final long END_TIME = 39360000;
    private static final long START_TIME = 36360000;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private GroupRepository groupRepository;

    @BeforeEach
    void inint() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        GroupEntity group = new GroupEntity();
        group.setName(GROUP_NAME);
        entityManager.persist(group);
       
        TimetableEntity timetable = new TimetableEntity();
        timetable.setStartTime(START_TIME);
        timetable.setEndTime(END_TIME);
        timetable.setDescription(TIMETABLE_DESCRIPTION);
        timetable.setWeekDay(DayOfWeek.valueOf(WEEK_DAY));
        timetable.setGroup(group);
        entityManager.persist(timetable);

        StudentEntity student = new StudentEntity();
        student.setFirstName(FIST_NAME);
        student.setLastName(LAST_NAME);
        student.setGroup(group);
        entityManager.persist(student);
        entityManager.getTransaction().commit();
    }

    @Test
    void getTimetableListByGroupId_GettingDataFromDatabase_CorrectRecevedData() throws RepositoryException {
        GroupEntity receivedGroup = groupRepository.getTimetableListByGroupId(GROUP_ID);
        
        assertEquals(GROUP_ID, receivedGroup.getId());
        assertEquals(GROUP_NAME, receivedGroup.getName());
        assertEquals(TIMETABLE_ID, receivedGroup.getTimetableList().get(FIRST_ELEMENT).getId());
        assertEquals(START_TIME, receivedGroup.getTimetableList().get(FIRST_ELEMENT).getStartTime());
        assertEquals(END_TIME, receivedGroup.getTimetableList().get(FIRST_ELEMENT).getEndTime());
        assertEquals(WEEK_DAY, receivedGroup.getTimetableList().get(FIRST_ELEMENT).getWeekDay().toString());
    }

    @Test
    void getStudentListByGroupId_GettingDataFromDatabase_CorrectReceivedData() throws RepositoryException {
        GroupEntity receivedGroup = groupRepository.getStudentListByGroupId(GROUP_ID);
        
        assertEquals(GROUP_ID, receivedGroup.getId());
        assertEquals(GROUP_NAME, receivedGroup.getName());
        assertEquals(STUDENT_ID, receivedGroup.getStudentList().get(FIRST_ELEMENT).getId());
        assertEquals(FIST_NAME, receivedGroup.getStudentList().get(FIRST_ELEMENT).getFirstName());
        assertEquals(LAST_NAME, receivedGroup.getStudentList().get(FIRST_ELEMENT).getLastName());
        assertEquals(GROUP_ID, receivedGroup.getStudentList().get(FIRST_ELEMENT).getGroup().getId());
    }

    @Test
    void insert_InsertingDataOfGroupToDatabase_DatabaseHasCorrectData() throws RepositoryException {
        GroupEntity group = new GroupEntity();
        group.setName(GROUP_NAME);
        
        GroupEntity groupWithId = groupRepository.insert(group);
        entityManagerFactory.createEntityManager();
        GroupEntity insertedGroup = entityManager.find(GroupEntity.class, NEW_GROUP_ID);
        
        assertEquals(NEW_GROUP_ID, groupWithId.getId());
        assertEquals(GROUP_NAME, insertedGroup.getName());
    }
 
    @Test
    void getById_ReceivingDatabaseDataOfGroup_CorrectReceivedData() throws RepositoryException {
        GroupEntity group = groupRepository.getById(GROUP_ID);
        
        assertEquals(GROUP_ID, group.getId());
        assertEquals(GROUP_NAME, group.getName());
    }
    
    @Test
    void update_UpdatingDatabaseDataOfGroup_DatabaseHasCorrectData() throws RepositoryException {
        GroupEntity group = new GroupEntity();
        group.setId(GROUP_ID);
        group.setName(NEW_GROUP_NAME);
        
        groupRepository.update(group);
        
        GroupEntity updatedGroup = entityManager.find(GroupEntity.class, GROUP_ID);
        assertEquals(GROUP_ID, updatedGroup.getId());
        assertEquals(NEW_GROUP_NAME, updatedGroup.getName());
    }
    
    @Test
    void deleteById_DeletingDatabaseDataOfGroup_DatabaseHasNoData() throws RepositoryException {
        groupRepository.deleteById(GROUP_ID);
        
        GroupEntity group = new GroupEntity();
        group.setId(GROUP_ID);
        
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        boolean containStatus = entityManager.contains(group);
        assertFalse(containStatus);
    }
}
