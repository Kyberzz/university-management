package ua.com.foxminded.university.service;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entitymother.CourseEntityMother;
import ua.com.foxminded.university.entitymother.TeacherEntityMother;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.modelmother.CourseModelMother;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.TeacherRepository;
import ua.com.foxminded.university.service.impl.CourseServiceImpl;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {
    
    private static final Type TYPE = new TypeToken<List<CourseModel>>() {}.getType();
    private static final int ID = 1;
    private static final int TEACHER_ID = 1;
    private static final int COURSE_ID = 1;
    
    
    @InjectMocks
    private CourseServiceImpl courseService;
    
    @Mock
    private TeacherRepository teacherRepositoryMock;
    
    @Mock
    private CourseRepository courseRepositoryMock;
    
    @Mock
    private ModelMapper modelMapperMock;
    
    private CourseEntity courseEntity;
    private CourseModel courseModel;
    private TeacherEntity teacherEntity;

    @BeforeEach
    void setUp() {
        courseModel = CourseModelMother.complete().build();
        courseEntity = CourseEntityMother.complete().teachers(new HashSet<>()).build();
        teacherEntity = TeacherEntityMother.complete().courses(new HashSet<>()).build();
    }
    
    @Test
    void deassignTeacherToCourse_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(courseRepositoryMock.findById(anyInt())).thenReturn(courseEntity);
        when(teacherRepositoryMock.findById(anyInt())).thenReturn(teacherEntity);
        
        courseService.deassignTeacherToCourse(TEACHER_ID, COURSE_ID);
        
        verify(courseRepositoryMock).saveAndFlush(isA(CourseEntity.class));
    }
    
    @Test
    void assignTeacherToCourse_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(courseRepositoryMock.findById(anyInt())).thenReturn(courseEntity);
        when(teacherRepositoryMock.findById(anyInt())).thenReturn(teacherEntity);
        courseService.assignTeacherToCourse(TEACHER_ID, COURSE_ID);
        
        verify(courseRepositoryMock).saveAndFlush(isA(CourseEntity.class));
    }
    
    @Test
    void getTimetableAndTeachersByCourseId_ShouldExecuteCorrecCallsQuantity() 
            throws ServiceException {
        when(courseRepositoryMock.getCourseWithDependencies(anyInt())).thenReturn(courseEntity);
        courseService.getTimetableAndTeachersByCourseId(ID);
        verify(modelMapperMock, times(1)).map(courseEntity, CourseModel.class);
    }
    
    void update_ShouldExcecuteCorrectCallsQuantity() throws ServiceException {
        courseService.update(courseModel);
        InOrder inOrder = Mockito.inOrder(modelMapperMock, courseRepositoryMock);
        
        inOrder.verify(modelMapperMock, times(1)).map(courseModel, CourseModel.class);
        inOrder.verify(courseRepositoryMock, times(1)).saveAndFlush(courseEntity);
    }
    
    @Test
    void getAll_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        CourseEntity course = CourseEntityMother.complete().build();
        List<CourseEntity> courses = Arrays.asList(course);
        when(courseRepositoryMock.findAll()).thenReturn(courses);
        
        courseService.getAll();
        verify(modelMapperMock, times(1)).map(courses, TYPE);
    }
    
    @Test
    void create_ShouldExcecuteCorrectCallsQuantity() throws ServiceException {
        when(modelMapperMock.map(courseModel, CourseEntity.class)).thenReturn(courseEntity);
        courseService.create(courseModel);
        verify(courseRepositoryMock).saveAndFlush(ArgumentMatchers.isA(CourseEntity.class));
    }
    
    @Test
    void deleteById_ShouldExcecuteCorrectCallsQauntity() throws ServiceException {
        courseService.deleteById(ID);
        verify(courseRepositoryMock, times(1)).deleteById(anyInt());
    }
    
    @Test
    void getById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(courseRepositoryMock.findById(anyInt())).thenReturn(courseEntity);
        courseService.getById(ID);
        verify(modelMapperMock, times(1)).map(courseEntity, CourseModel.class);
    }
}
