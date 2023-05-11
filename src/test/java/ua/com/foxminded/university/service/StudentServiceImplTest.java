package ua.com.foxminded.university.service;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.entitymother.GroupEntityMother;
import ua.com.foxminded.university.entitymother.StudentEntityMother;
import ua.com.foxminded.university.entitymother.UserEntityMother;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.modelmother.GroupModelMother;
import ua.com.foxminded.university.modelmother.StudentModelMother;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.repository.UserRepository;
import ua.com.foxminded.university.service.impl.StudentServiceImpl;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
    
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
    
    private StudentModel studentModel;
    private StudentEntity studentEntity;
    private UserEntity userEntity;
    private GroupModel groupModel;
    private GroupEntity groupEntity;
    
    @BeforeEach
    void setUp() {
        studentModel = StudentModelMother.complete().build();
        studentEntity = StudentEntityMother.complete().build();
        userEntity = UserEntityMother.complete().build();
        groupModel = GroupModelMother.complete().id(GROUP_ID).build();
        groupEntity = GroupEntityMother.complete().build();
    }
    
    @Test
    void deleteById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        studentService.deleteById(STUDENT_ID);
        verify(studentRepositoryMock).deleteById(anyInt());
    }
    
    @Test
    void update_ShouldExecuteCorrectCallsQuantity_WhenStudentHasGroup() 
            throws ServiceException {
        studentEntity.getUser().setId(STUDENT_ID);
        groupModel.setId(GROUP_ID);
        studentModel.setGroup(groupModel);
        when(groupRepository.findById(anyInt())).thenReturn(groupEntity);
        when(modelMapperMock.map(studentModel, StudentEntity.class)).thenReturn(studentEntity);
        when(studentRepositoryMock.findById(anyInt())).thenReturn(studentEntity);
        when(userRepositoryMock.findById(anyInt())).thenReturn(userEntity);
        studentService.update(studentModel);
        verify(studentRepositoryMock).saveAndFlush(isA(StudentEntity.class));
    }
    
    @Test
    void update_ShouldExecuteCorrectCallsQuantity_WhenStudentHasNoGroup() 
            throws ServiceException {
        studentEntity.getUser().setId(STUDENT_ID);
        when(modelMapperMock.map(studentModel, StudentEntity.class)).thenReturn(studentEntity);
        when(studentRepositoryMock.findById(anyInt())).thenReturn(studentEntity);
        when(userRepositoryMock.findById(anyInt())).thenReturn(userEntity);
        studentService.update(studentModel);
        verify(studentRepositoryMock).saveAndFlush(isA(StudentEntity.class));
    }
    
    @Test
    void getById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(studentRepositoryMock.findById(anyInt())).thenReturn(studentEntity);
        studentService.getById(STUDENT_ID);
        verify(modelMapperMock).map(studentEntity, StudentModel.class);
    }
    
    @Test
    void getAll_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        List<StudentEntity> students = Arrays.asList(studentEntity);
        when(studentRepositoryMock.findAll()).thenReturn(students);
        studentService.getAll();
        verify(modelMapperMock).map(students, StudentServiceImpl.STUDENT_MODEL_LIST_TYPE);
    }
    
    @Test
    void create_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(modelMapperMock.map(studentModel, StudentEntity.class)).thenReturn(studentEntity);
        studentService.create(studentModel);
        verify(studentRepositoryMock).saveAndFlush(isA(StudentEntity.class));
    }
}
