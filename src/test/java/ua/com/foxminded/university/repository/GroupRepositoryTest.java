package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.entitymother.GroupEntityMother;
import ua.com.foxminded.university.entitymother.StudentEntityMother;
import ua.com.foxminded.university.entitymother.TimetableEntityMother;
import ua.com.foxminded.university.entitymother.UserEntityMother;
import ua.com.foxminded.university.exception.RepositoryException;

@DataJpaTest
@ActiveProfiles("test")
class GroupRepositoryTest {
    
    private static final int STUDENTS_QUANTITY = 1;
    private static final int TIMETALBES_QUANTITY = 1;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private GroupRepository groupRepository;
    
    private TimetableEntity timetable;
    private GroupEntity group;
    private UserEntity user;
    private StudentEntity student;

    @BeforeEach
    void inint() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        group = GroupEntityMother.complete().build();
        entityManager.persist(group);
        
        timetable = TimetableEntityMother.complete()
                                         .group(group)
                                         .build();
        entityManager.persist(timetable);
        
        user = UserEntityMother.complete().build();
        student = StudentEntityMother.complete()
                                     .group(group)
                                     .user(user)
                                     .build();
        entityManager.persist(student);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Test
    void findTimetableListById_ShouldReturnTimetalbesOwnedByGroup() throws RepositoryException {
        GroupEntity receivedGroup = groupRepository.findTimetableListById(group.getId());
        assertEquals(TIMETALBES_QUANTITY, receivedGroup.getTimetableList().size());
    }

    @Test
    void findStudentListById_ShouldReturnStudentsOwnedByGroupWithId() 
            throws RepositoryException {
        GroupEntity receivedGroup = groupRepository.findStudentListById(group.getId()
                                                                             .intValue());
        assertEquals(STUDENTS_QUANTITY, receivedGroup.getStudents().size());
    }
    
    @Test
    void findById_ShouldRetrunGroupWithId() throws RepositoryException {
        GroupEntity receivedGroup = groupRepository.findById(group.getId().intValue());
        assertEquals(group.getId(), receivedGroup.getId());
    }
}
