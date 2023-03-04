package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.config.RepositoryTestConfig;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.exception.RepositoryException;

@ActiveProfiles("test")
@ContextConfiguration(classes = RepositoryTestConfig.class)
@Transactional
@ExtendWith(SpringExtension.class)
class GroupRepositoryTest {
    
    private static final String TIMETABLE_DESCRIPTION = "some timetable description";
    private static final String WEEK_DAY = "MONDAY";
    private static final String LAST_NAME = "Smith";
    private static final String FIST_NAME = "Alex";
    private static final String GROUP_NAME = "rs-01";
    private static final int STUDENT_ID = 1;
    private static final int FIRST_ELEMENT = 0;
    private static final int TIMETABLE_ID = 1;
    private static final int GROUP_ID = 1;
    private static final int MINUTE = 0;
    private static final int END_TIME = 9;
    private static final int START_TIME = 8;
    
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
        timetable.setStartTime(LocalTime.of(START_TIME, MINUTE));
        timetable.setEndTime(LocalTime.of(END_TIME, MINUTE));
        timetable.setDescription(TIMETABLE_DESCRIPTION);
        timetable.setDayOfWeek(DayOfWeek.valueOf(WEEK_DAY));
        timetable.setGroup(group);
        entityManager.persist(timetable);

        StudentEntity student = new StudentEntity();
        student.setUser(new UserEntity());
        student.getUser().setFirstName(FIST_NAME);
        student.getUser().setLastName(LAST_NAME);
        student.setGroup(group);
        entityManager.persist(student);
        entityManager.getTransaction().commit();
    }

    @Test
    void findTimetableListById_GettingDataFromDatabase_CorrectRecevedData() throws RepositoryException {
        GroupEntity receivedGroup = groupRepository.findTimetableListById(GROUP_ID);
        
        assertEquals(GROUP_ID, receivedGroup.getId());
        assertEquals(GROUP_NAME, receivedGroup.getName());
        assertEquals(TIMETABLE_DESCRIPTION, receivedGroup.getTimetableList().get(FIRST_ELEMENT)
                                                                            .getDescription());
        assertEquals(TIMETABLE_ID, receivedGroup.getTimetableList().get(FIRST_ELEMENT).getId());
        assertEquals(LocalTime.of(START_TIME, MINUTE), 
                     receivedGroup.getTimetableList().get(FIRST_ELEMENT).getStartTime());
        assertEquals(LocalTime.of(END_TIME, MINUTE), 
                     receivedGroup.getTimetableList().get(FIRST_ELEMENT).getEndTime());
        assertEquals(WEEK_DAY, receivedGroup.getTimetableList().get(FIRST_ELEMENT).getDayOfWeek()
                                                                                  .toString());
    }

    @Test
    void findStudentListById_GettingDataFromDatabase_CorrectReceivedData() throws RepositoryException {
        GroupEntity receivedGroup = groupRepository.findStudentListById(GROUP_ID);
        
        assertEquals(GROUP_ID, receivedGroup.getId());
        assertEquals(GROUP_NAME, receivedGroup.getName());
        assertEquals(STUDENT_ID, receivedGroup.getStudentList().get(FIRST_ELEMENT).getId());
        assertEquals(FIST_NAME, receivedGroup.getStudentList().get(FIRST_ELEMENT)
                                                              .getUser().getFirstName());
        assertEquals(LAST_NAME, receivedGroup.getStudentList().get(FIRST_ELEMENT)
                                                              .getUser().getLastName());
        assertEquals(GROUP_ID, receivedGroup.getStudentList().get(FIRST_ELEMENT)
                                                             .getGroup().getId());
    }
    
    @Test
    void findById_GettingGroupById_CorrectRetrievedData() throws RepositoryException {
        GroupEntity group = groupRepository.findById(GROUP_ID);
        assertEquals(GROUP_ID, group.getId());
        assertEquals(GROUP_NAME, group.getName());
    }
}
