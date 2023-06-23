package ua.com.foxminded.university.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.service.impl.UserServiceImplTest.USER_ID;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import ua.com.foxminded.university.dto.GroupDTO;
import ua.com.foxminded.university.dto.StudentDTO;
import ua.com.foxminded.university.dto.UserDTO;
import ua.com.foxminded.university.dto.UserPersonDTO;
import ua.com.foxminded.university.dtomother.GroupDTOMother;
import ua.com.foxminded.university.dtomother.PersonDTOMother;
import ua.com.foxminded.university.dtomother.UserDTOMother;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.entity.UserPerson;
import ua.com.foxminded.university.entitymother.UserMother;
import ua.com.foxminded.university.entitymother.UserPersonMother;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
    
    public static final String LAST_NAME_A = "A";
    public static final String LAST_NAME_B = "B";
    public static final int GROUP_ID = 1;
    public static final int STUDENT_ID = 1;
    
    @InjectMocks
    private StudentServiceImpl studentService;
    
    @Mock
    private StudentRepository studentRepositoryMock;
    
    @Mock
    private UserRepository userRepositoryMock;
    
    @Mock
    private GroupRepository groupRepository;
    
    @Mock
    private ModelMapper modelMapperMock;
    
    private StudentDTO studentDto;
    private Student student;
    private User user;
    private GroupDTO groupDto;
    private UserPerson person;
    
    @BeforeEach
    void setUp() {
        UserPersonDTO personDto = PersonDTOMother.complete().build();
        UserDTO userDto = UserDTOMother.complete().person(personDto).build();
        studentDto = StudentDTO.builder().user(userDto).build();
        person = UserPersonMother.complete().build();
        user = UserMother.complete().person(person).build();
        student = Student.builder().user(user).build();
        groupDto = GroupDTOMother.complete().id(GROUP_ID).build();
    }
    
    @Test
    void sortByLastName_ShouldSortCorrectly() {
        studentDto.getUser().getPerson().setLastName(LAST_NAME_A);
        UserPersonDTO person = PersonDTOMother.complete().lastName(LAST_NAME_B).build();
        UserDTO user = UserDTOMother.complete().person(person).build();
        StudentDTO studentB = StudentDTO.builder().user(user).build();
        List<StudentDTO> list = Arrays.asList(studentB, studentDto);
        studentService.sortByLastName(list);
        List<StudentDTO> expectedList = Arrays.asList(studentDto, studentB);
        
        assertEquals(expectedList, list);
    }
    
    @Test
    void deleteById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        studentService.deleteById(STUDENT_ID);
        verify(studentRepositoryMock).deleteById(anyInt());
    }
    
    @Test
    void update_ShouldUpdateGroupAndUserRelationships() throws ServiceException {
        student.getUser().setId(STUDENT_ID);
        groupDto.setId(GROUP_ID);
        studentDto.setGroup(groupDto);
        studentDto.setUser(UserDTO.builder().id(USER_ID).build());
        when(modelMapperMock.map(studentDto, Student.class)).thenReturn(student);
        when(studentRepositoryMock.findById(anyInt())).thenReturn(student);
        
        studentService.update(studentDto);
        
        verify(modelMapperMock).map(studentDto, Student.class);
        verify(studentRepositoryMock).findById(anyInt());
        verify(studentRepositoryMock).saveAndFlush(isA(Student.class));
    }
    
    @Test
    void getById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(studentRepositoryMock.findById(anyInt())).thenReturn(student);
        studentService.getById(STUDENT_ID);
        verify(modelMapperMock).map(student, StudentDTO.class);
    }
    
    @Test
    void getAll_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        List<Student> students = Arrays.asList(student);
        when(studentRepositoryMock.findAll()).thenReturn(students);
        studentService.getAll();
        verify(modelMapperMock).map(students, StudentServiceImpl.STUDENT_MODEL_LIST_TYPE);
    }
    
    @Test
    void create_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(modelMapperMock.map(studentDto, Student.class)).thenReturn(student);
        studentService.create(studentDto);
        verify(studentRepositoryMock).saveAndFlush(isA(Student.class));
    }
}
