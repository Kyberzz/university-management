package ua.com.foxminded.university.service;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entitymother.CourseEntityMother;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.modelmother.CourseModelMother;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.service.impl.CourseServiceImpl;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {
    
    private static final Type TYPE = new TypeToken<List<CourseModel>>() {}.getType();
    private static final int ID = 1;
    
    @InjectMocks
    private CourseServiceImpl courseService;
    
    @Mock
    private CourseRepository courseRepositoryMock;
    
    @Mock
    private ModelMapper modelMapperMock;
    
    private CourseEntity entity;
    private CourseModel model;

    @BeforeEach
    void setUp() {
        model = CourseModelMother.complete().build();
    }
    
    @Test
    void getTimetableAndTeachersByCourseId_ShouldExecuteCorrecCallsQuantity() 
            throws ServiceException {
        courseService.getTimetableAndTeachersByCourseId(ID);
        verify(modelMapperMock, times(1)).map(entity, CourseModel.class);
    }
    
    void update_ShouldExcecuteCorrectCallsQuantity() throws ServiceException {
        courseService.update(model);
        InOrder inOrder = Mockito.inOrder(modelMapperMock, courseRepositoryMock);
        
        inOrder.verify(modelMapperMock, times(1)).map(model, CourseModel.class);
        inOrder.verify(courseRepositoryMock, times(1)).saveAndFlush(entity);
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
        courseService.create(model);
        InOrder inOrder = Mockito.inOrder(modelMapperMock, courseRepositoryMock);
        
        inOrder.verify(modelMapperMock, times(1)).map(model, CourseEntity.class);
        inOrder.verify(courseRepositoryMock, times(1)).saveAndFlush(entity);
    }
    
    @Test
    void deleteById_ShouldExcecuteCorrectCallsQauntity() throws ServiceException {
        courseService.deleteById(ID);
        verify(courseRepositoryMock, times(1)).deleteById(anyInt());
    }
    
    @Test
    void getById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        InOrder inOrder = Mockito.inOrder(courseRepositoryMock, modelMapperMock);
        courseService.getById(ID);
        inOrder.verify(courseRepositoryMock, times(1)).findById(anyInt());
        inOrder.verify(modelMapperMock, times(1)).map(entity, CourseModel.class);
    }
}
