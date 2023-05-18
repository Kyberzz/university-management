package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import ua.com.foxminded.university.config.TestConfig;
import ua.com.foxminded.university.entity.GroupEntity;

@DataJpaTest
@Import(TestConfig.class)
@ActiveProfiles("test")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, 
                         TransactionDbUnitTestExecutionListener.class})
@Transactional
@DatabaseSetup("classpath:test-data.xml")
class GroupRepositoryTest {
    
    private static final int STUDENTS_QUANTITY = 1;
    private static final int SCHEDULES_QUANTITY = 1;
    private static final int GROUP_ID = 101;
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Test
    void getGroupRelationsById_ShouldReturnGroupWithItsAllRelations() {
        GroupEntity receivedGroup = groupRepository.getGroupRelationsById(GROUP_ID);
        assertEquals(STUDENTS_QUANTITY, receivedGroup.getStudents().size());
        assertEquals(SCHEDULES_QUANTITY, receivedGroup.getSchedules().size());
    }

    @Test
    void findTimetablesById_ShouldReturnTimetalbesOwnedByGroup() {
        GroupEntity receivedGroup = groupRepository.findTimetablesById(GROUP_ID);
        assertEquals(SCHEDULES_QUANTITY, receivedGroup.getSchedules().size());
    }

    @Test
    void findStudentListById_ShouldReturnStudentsOwnedByGroupWithId() {
        GroupEntity receivedGroup = groupRepository.findStudentsById(GROUP_ID);
        assertEquals(STUDENTS_QUANTITY, receivedGroup.getStudents().size());
    }
    
    @Test
    void findById_ShouldRetrunGroupWithId() {
        GroupEntity receivedGroup = groupRepository.findById(GROUP_ID);
        assertEquals(GROUP_ID, receivedGroup.getId());
    }
}