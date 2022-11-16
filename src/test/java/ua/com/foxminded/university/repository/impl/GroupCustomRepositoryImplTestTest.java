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
import ua.com.foxminded.university.entity.DayOfWeek;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.RepositoryException;

@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = RepositoryConfigTest.class)
@ExtendWith(SpringExtension.class)
class GroupCustomRepositoryImplTestTest {
    
    private static final String TIMETABLE_DESCRIPTION = "some timetable description";
    private static final String WEEK_DAY = "MONDAY";
    private static final String LAST_NAME = "Smith";
    private static final String FIST_NAME = "Alex";
    private static final String GROUP_NAME = "rs-01";
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
    void findById_GettingGroupById_CorrectRetrievedData() throws RepositoryException {
        GroupEntity group = groupRepository.findById(GROUP_ID);
        assertEquals(GROUP_ID, group.getId());
        assertEquals(GROUP_NAME, group.getName());
    }
}
