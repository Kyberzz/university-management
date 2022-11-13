package ua.com.foxminded.university.service.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.service.ServiceException;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {
    
    private static final int COURSE_ID = 1;
    
    @InjectMocks
    private CourseServiceImpl courseService;
    
    @Mock
    private CourseRepository courseRepositoryMock;
    
    @Test
    void updateCourse_CollingDaoObject_CorrectCallQuantity() throws ServiceException, 
                                                                    RepositoryException {
        CourseModel courseModel = new CourseModel();
        courseModel.setTeacher(new TeacherModel());
        courseService.updateCourse(courseModel);
        verify(courseRepositoryMock, times(1)).save(ArgumentMatchers.<CourseEntity>any());
    }
    
    @Test
    void getTimetableListByCourseId_GettingTimetableModel_CorrectCalQuantity() throws RepositoryException, 
                                                                                       ServiceException {
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setTeacher(new TeacherEntity());
        courseEntity.setTimetableList(new ArrayList<>());
        when(courseRepositoryMock.findTimetableListById(ArgumentMatchers.anyInt())).thenReturn(courseEntity);
        courseService.getTimetableListByCourseId(COURSE_ID);
        verify(courseRepositoryMock, times(1)).findTimetableListById(ArgumentMatchers.anyInt());
    }
}
