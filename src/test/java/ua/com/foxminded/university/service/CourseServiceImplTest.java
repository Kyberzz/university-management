package ua.com.foxminded.university.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entitymother.CourseEntityMother;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.service.impl.CourseServiceImpl;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {
    
    private static final int ID = 1;
    
    @InjectMocks
    private CourseServiceImpl courseService;
    
    @Mock
    private CourseRepository courseRepositoryMock;
    
    private CourseEntity entity;
    
    @Mock
    private ModelMapper modelMapperMock;
    
    private static final Type TYPE = new TypeToken<List<CourseModel>>() {}.getType();
    
    @Test
    void getTimetableAndTeachersByCourseId_ShouldExecuteCorrecCallsQuantity() 
            throws ServiceException {
        courseService.getTimetableAndTeachersByCourseId(ID);
        verify(modelMapperMock, times(1)).map(entity, CourseModel.class);
    }

    @Test
    void getAll_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        CourseEntity course = CourseEntityMother.complete().build();
        List<CourseEntity> courses = Arrays.asList(course);
        ModelMapper modelMapper = new ModelMapper();
        when(courseRepositoryMock.findAll()).thenReturn(courses);
        when(modelMapperMock.getConfiguration()).thenReturn(modelMapper.getConfiguration());
        
        courseService.getAll();
        verify(modelMapperMock, times(1)).getConfiguration();
        verify(modelMapperMock, times(1)).map(courses, TYPE);
    }
}
