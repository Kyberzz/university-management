package ua.com.foxminded.university.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.model.TeacherModel;

@ExtendWith(MockitoExtension.class)
class TeacherServiceImplTestTest {
    
    private static final int FIRST_ELEMENT = 0;
    private static final int TEACHER_ID = 2;
    private static final int COURSE_ID = 1;
    private static final String LAST_NAME = "Parker";
    private static final String FIRST_NAME = "Bruce";
    private static final String COURSE_NAME = "Biology";
    private static final String DESCRIPTION = "description of the course";
    
    @InjectMocks
    private TeacherServiceImpl teacherService;
    
    @Mock
    private TeacherDao teacherDao;

    @Test
    
    void getCourseListByTeacherId_GettingModelFromEntity_CorrectConversion() {
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setDescription(DESCRIPTION);
        courseEntity.setId(COURSE_ID);
        courseEntity.setName(COURSE_NAME);
        courseEntity.setTeacher(new TeacherEntity(TEACHER_ID));
        
        List<CourseEntity> courseList = new ArrayList<>();
        courseList.add(courseEntity);
        
        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setCourseList(courseList);
        teacherEntity.setFirstName(FIRST_NAME);
        teacherEntity.setId(TEACHER_ID);
        teacherEntity.setLastName(LAST_NAME);
        
        when(teacherDao.getCourseListByTeacherId(ArgumentMatchers.anyInt())).thenReturn(teacherEntity);
        TeacherModel teacherModel = teacherService.getCourseListByTeacherId(COURSE_ID);
        assertEquals(DESCRIPTION, teacherModel.getCourseList().get(FIRST_ELEMENT).getDescription());
        assertEquals(COURSE_ID, teacherModel.getCourseList().get(FIRST_ELEMENT).getId());
        assertEquals(COURSE_NAME, teacherModel.getCourseList().get(FIRST_ELEMENT).getName());
        assertEquals(FIRST_NAME, teacherModel.getFirstName());
        assertEquals(TEACHER_ID, teacherModel.getId());
        assertEquals(LAST_NAME, teacherModel.getLastName());
    }
}
