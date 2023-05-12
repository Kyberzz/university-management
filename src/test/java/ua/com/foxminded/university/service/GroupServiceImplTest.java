package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.service.StudentServiceImplTest.*;

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

import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entitymother.GroupEntityMother;
import ua.com.foxminded.university.entitymother.StudentEntityMother;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.model.PersonModel;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.modelmother.GroupModelMother;
import ua.com.foxminded.university.modelmother.StudentModelMother;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.service.impl.GroupServiceImpl;

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
    
    private GroupEntity groupEntity;
    private List<GroupEntity> groupEntities;
    private GroupModel groupModel;
    private StudentEntity studentEntity;
    
    @BeforeEach
    void setUp() {
        groupEntity = GroupEntityMother.complete().build();
        groupEntities = Arrays.asList(groupEntity);
        groupModel = GroupModelMother.complete().build();
        studentEntity = StudentEntityMother.complete().build();
    }
    
    @Test
    void deassignGroup() {
        when(studentRepository.findById(anyInt())).thenReturn(studentEntity);
        groupService.deassignGroup(STUDENT_ID);
        verify(studentRepository).saveAndFlush(isA(StudentEntity.class));
    }
    
    @Test
    void sortStudentsByLastName() {
        PersonModel person_A = PersonModel.builder().lastName(LAST_NAME_A).build();
        PersonModel person_B = PersonModel.builder().lastName(LAST_NAME_B).build();
        UserModel user_A = UserModel.builder().person(person_A).build();
        UserModel user_B = UserModel.builder().person(person_B).build();
        
        StudentModel student_A = StudentModelMother.complete().user(user_A).build();
        StudentModel student_B = StudentModelMother.complete().user(user_B).build();
        Set<StudentModel> students = new LinkedHashSet<>();
        students.add(student_B);
        students.add(student_A);
        groupModel.setStudents(students);
        groupService.sortStudentsByLastName(groupModel);
        Set<StudentModel> expectedResult = new LinkedHashSet<>(Arrays.asList(student_A, 
                                                                             student_B));
        assertEquals(expectedResult, groupModel.getStudents());
    }
    
    @Test
    void assignGroup_ShouldAssignGroupToStudents() { 
        int[] studentIds = {STUDENT_ID};
        when(studentRepository.findById(anyInt())).thenReturn(studentEntity);
        groupService.assignGroup(GROUP_ID, studentIds);
        verify(studentRepository).saveAndFlush(isA(StudentEntity.class));
    }
    
    @Test
    void getGroupRelationsById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(groupRepository.getGroupRelationsById(anyInt())).thenReturn(groupEntity);
        groupService.getGroupRelationsById(GROUP_ID);
        verify(modelMapper).map(groupEntity, GroupModel.class);
    }
    
    @Test
    void deleteById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        groupService.deleteById(GROUP_ID);
        verify(groupRepository).deleteById(anyInt());
    }
    
    @Test
    void create_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(modelMapper.map(groupModel, GroupEntity.class)).thenReturn(groupEntity);
        groupService.create(groupModel);
        verify(groupRepository).saveAndFlush(isA(GroupEntity.class));
    }
    
    @Test
    void update_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        groupEntity.setId(GROUP_ID);
        when(modelMapper.map(groupModel, GroupEntity.class)).thenReturn(groupEntity);
        when(groupRepository.findById(anyInt())).thenReturn(groupEntity);
        groupService.update(groupModel);
        verify(groupRepository).saveAndFlush(isA(GroupEntity.class));
    }
    
    @Test
    void getById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(groupRepository.findById(anyInt())).thenReturn(groupEntity);
        groupService.getById(GROUP_ID);
        verify(modelMapper).map(groupEntity, GroupModel.class);
    }
    
    @Test
    void getAll_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(groupRepository.findAll()).thenReturn(groupEntities);
        groupService.getAll();
        verify(modelMapper).map(groupEntities, GroupServiceImpl.GROUP_MODEL_LIST_TYPE);
    }
    
    @Test
    void getStudentsByGroupId_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(groupRepository.findStudentsById(anyInt())).thenReturn(groupEntity);
        groupService.getStudentsByGroupId(GROUP_ID);
        verify(modelMapper).map(groupEntity, GroupModel.class);
    }
    
    @Test
    void getTimetablesByGroupId_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(groupRepository.findTimetablesById(anyInt())).thenReturn(groupEntity);
        groupService.getTimetablesByGroupId(GROUP_ID);
        verify(modelMapper).map(groupEntity, GroupModel.class);
    }
}