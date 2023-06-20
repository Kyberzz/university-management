package ua.com.foxminded.university.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.service.impl.StudentServiceImplTest.LAST_NAME_A;
import static ua.com.foxminded.university.service.impl.StudentServiceImplTest.LAST_NAME_B;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import ua.com.foxminded.university.dto.GroupDTO;
import ua.com.foxminded.university.dto.PersonDTO;
import ua.com.foxminded.university.dto.StudentDTO;
import ua.com.foxminded.university.dto.UserDTO;
import ua.com.foxminded.university.dtomother.GroupDTOMother;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.entitymother.GroupMother;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {
    
    public static final int STUDENT_ID = 1;
    public static final int GROUP_ID = 1;
    
    @InjectMocks
    private GroupServiceImpl groupService;
    
    @Mock
    private GroupRepository groupRepository;
    
    @Mock
    private StudentRepository studentRepository;
    
    @Mock
    private ModelMapper modelMapper;
    
    private Group group;
    private List<Group> groups;
    private GroupDTO groupDto;
    private Student student;
    
    @BeforeEach
    void setUp() {
        group = GroupMother.complete().build();
        groups = Arrays.asList(group);
        groupDto = GroupDTOMother.complete().build();
        student = new Student();
    }
    
    @Test
    void deassignGroup() {
        when(studentRepository.findById(anyInt())).thenReturn(student);
        groupService.deassignGroup(STUDENT_ID);
        verify(studentRepository).saveAndFlush(isA(Student.class));
    }
    
    @Test
    void sortStudentsByLastName() {
        PersonDTO firstPerson = PersonDTO.builder().lastName(LAST_NAME_A).build();
        PersonDTO secondPerson = PersonDTO.builder().lastName(LAST_NAME_B).build();
        UserDTO firstUser = UserDTO.builder().person(firstPerson).build();
        UserDTO secondUser = UserDTO.builder().person(secondPerson).build();
        
        StudentDTO firstStudent = StudentDTO.builder().user(firstUser).build();
        StudentDTO secondStudent = StudentDTO.builder().user(secondUser).build();
        Set<StudentDTO> students = new LinkedHashSet<>();
        students.add(secondStudent);
        students.add(firstStudent);
        groupDto.setStudents(students);
        groupService.sortContainedStudentsByLastName(groupDto);
        Set<StudentDTO> expectedResult = new LinkedHashSet<>(Arrays.asList(firstStudent, 
                                                                             secondStudent));
        assertEquals(expectedResult, groupDto.getStudents());
    }
    
    @Test
    void assignGroup_ShouldAssignGroupToStudents() { 
        int[] studentIds = {STUDENT_ID};
        when(studentRepository.findById(anyInt())).thenReturn(student);
        groupService.assignGroup(GROUP_ID, studentIds);
        verify(studentRepository).saveAndFlush(isA(Student.class));
    }
    
    @Test
    void getGroupRelationsById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(groupRepository.getGroupRelationsById(anyInt())).thenReturn(group);
        groupService.getGroupRelationsById(GROUP_ID);
        verify(modelMapper).map(group, GroupDTO.class);
    }
    
    @Test
    void deleteById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        groupService.deleteById(GROUP_ID);
        verify(groupRepository).deleteById(anyInt());
    }
    
    @Test
    void create_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(modelMapper.map(groupDto, Group.class)).thenReturn(group);
        groupService.create(groupDto);
        verify(groupRepository).saveAndFlush(isA(Group.class));
    }
    
    @Test
    void update_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        group.setId(GROUP_ID);
        when(modelMapper.map(groupDto, Group.class)).thenReturn(group);
        when(groupRepository.findById(anyInt())).thenReturn(group);
        groupService.update(groupDto);
        verify(groupRepository).saveAndFlush(isA(Group.class));
    }
    
    @Test
    void getById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(groupRepository.findById(anyInt())).thenReturn(group);
        groupService.getById(GROUP_ID);
        verify(modelMapper).map(group, GroupDTO.class);
    }
    
    @Test
    void getAll_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(groupRepository.findAll()).thenReturn(groups);
        groupService.getAll();
        verify(modelMapper).map(groups, GroupServiceImpl.GROUP_MODEL_LIST_TYPE);
    }
    
    @Test
    void getStudentsByGroupId_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(groupRepository.findStudentsById(anyInt())).thenReturn(group);
        groupService.getStudentsByGroupId(GROUP_ID);
        verify(modelMapper).map(group, GroupDTO.class);
    }
    
    @Test
    void getTimetablesByGroupId_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(groupRepository.findTimetablesById(anyInt())).thenReturn(group);
        groupService.getTimetablesByGroupId(GROUP_ID);
        verify(modelMapper).map(group, GroupDTO.class);
    }
}
