package ua.com.foxminded.university.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.WeekDayEntity;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.model.WeekDayModel;


@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {
    
    private static final long START_TIME = 645689;
    private static final long END_TIME = 46548;
    private static final int FIRST_ELEMENT = 0;
    private static final int TIMETABLE_ID = 2;
    private static final int GROUP_ID = 2;
    private static final int TEACHER_ID = 1;
    private static final int COURSE_ID = 1;
    private static final String COURSE_NAME = "Thermodynamics";
    private static final String DESCRIPTION = "some description";
    
    @InjectMocks
    private CourseServiceImpl courseService;
    
    @Mock
    private CourseDao courseDaoMock;
    
    @Test
    void updateCourse_CollingDaoObject_CorrectCallQuantity() {
        CourseModel courseModel = new CourseModel();
        courseModel.setTeacher(new TeacherModel());
        courseService.updateCourse(courseModel);
        verify(courseDaoMock, times(1)).update(ArgumentMatchers.<CourseEntity>any());
    }
    
    @Test
    void getTimetableListByCourseId_GettingTimetableModel_CorrectData () {
        CourseEntity courseEntity = new CourseEntity();
        List<TimetableEntity> courseList = new ArrayList<>();
        TimetableEntity timetableEntity = new TimetableEntity();
        timetableEntity.setCourse(new CourseEntity(COURSE_ID));
        timetableEntity.setDescription(DESCRIPTION);
        timetableEntity.setEndTime(END_TIME);
        timetableEntity.setGroup(new GroupEntity(GROUP_ID));
        timetableEntity.setId(TIMETABLE_ID);
        timetableEntity.setStartTime(START_TIME);
        timetableEntity.setWeekDay(WeekDayEntity.WEDNESDAY);
        
        courseList.add(timetableEntity);
        
        courseEntity.setTimetableList(courseList);
        courseEntity.setDescription(DESCRIPTION);
        courseEntity.setId(COURSE_ID);
        courseEntity.setName(COURSE_NAME);
        courseEntity.setTeacher(new TeacherEntity(TEACHER_ID));
        
        when(courseDaoMock.getTimetableListByCourseId(ArgumentMatchers.anyInt())).thenReturn(courseEntity);
        CourseModel courseModel = courseService.getTimetableListByCourseId(COURSE_ID);
        assertEquals(DESCRIPTION, courseModel.getDescription());
        assertEquals(COURSE_ID, courseModel.getId());
        assertEquals(COURSE_NAME, courseModel.getName());
        assertEquals(TEACHER_ID, courseModel.getTeacher().getId());
   
        assertEquals(COURSE_ID, courseModel.getTimetableList().get(FIRST_ELEMENT).getCourse().getId());
        assertEquals(DESCRIPTION, courseModel.getTimetableList().get(FIRST_ELEMENT).getDescription());
        assertEquals(END_TIME, courseModel.getTimetableList().get(FIRST_ELEMENT).getEndTime());
        assertEquals(GROUP_ID, courseModel.getTimetableList().get(FIRST_ELEMENT).getGroup().getId());
        assertEquals(TIMETABLE_ID, courseModel.getTimetableList().get(FIRST_ELEMENT).getId());
        assertEquals(START_TIME, courseModel.getTimetableList().get(FIRST_ELEMENT).getStartTime());
        assertEquals(WeekDayModel.WEDNESDAY, courseModel.getTimetableList().get(FIRST_ELEMENT).getWeekDay());
    }
}
