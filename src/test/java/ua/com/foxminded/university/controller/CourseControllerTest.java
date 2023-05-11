package ua.com.foxminded.university.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.university.controller.DefaultControllerTest.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.modelmother.CourseModelMother;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.TeacherService;

@ExtendWith(SpringExtension.class)
class CourseControllerTest {
    
    private static final int TEACHER_ID = 1;
    private static final int COURSE_ID = 1;
    
    @MockBean
    private TeacherService teacherServiceMock;
    
    @MockBean
    private CourseService courseServiceMock;
    
    private MockMvc mockMvc;
    
    private CourseModel course;
    
    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CourseController(
                teacherServiceMock, courseServiceMock)).build();
        course = CourseModelMother.complete().build();
    }
    
    @Test
    void deassignTeacherToCourse() throws Exception {
        mockMvc.perform(post("/courses/{courseId}/deassign-teacher", COURSE_ID).param(
                    "teacherId", String.valueOf(TEACHER_ID)))
               .andDo(print())
               .andExpect(redirectedUrl("/courses/" + COURSE_ID));
        
        verify(courseServiceMock).deassignTeacherToCourse(TEACHER_ID, COURSE_ID);
    }
    
    @Test
    void assignTeacherToCourse_ShouldRedirectToCourseView() throws Exception {
        
        mockMvc.perform(post("/courses/{courseId}/assign-teacher", COURSE_ID).param(
                    "teacherId", String.valueOf(TEACHER_ID)))
               .andDo(print())
               .andExpect(redirectedUrl("/courses/" + COURSE_ID));
        
        verify(courseServiceMock).assignTeacherToCourse(COURSE_ID, TEACHER_ID);
    }
    
    @Test
    void getById_ShouldRenderCourseView() throws Exception {
        when(courseServiceMock.getTimetableAndTeachersByCourseId(anyInt())).thenReturn(course);
        
        mockMvc.perform(get("/courses/{id}", COURSE_ID))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("courseModel", 
                                                  "allTeachers", 
                                                  "updatedCourse"))
               .andExpect(view().name("courses/course"));
    }
    
    @Test
    void delete_ShouldDeleteCourseAndRedirectToListView() throws Exception {
        mockMvc.perform(post("/courses/delete").param("courseId", String.valueOf(COURSE_ID)))
               .andExpect(redirectedUrl("/courses/list"));
        verify(courseServiceMock, times(1)).deleteById(anyInt());
    }
    
    @Test
    void update_ShouldReturnBadRequestStatus() throws Exception {
        mockMvc.perform(post("/courses/update").param("courseId", String.valueOf(COURSE_ID))
                                               .content(BAD_CONTENT))
               .andExpect(status().is4xxClientError())
               .andExpect(view().name(ERROR_VIEW));
    }
    
    @Test
    void update_ShouldUpdateCourseAndRedirectToCourseView() throws Exception {
        when(courseServiceMock.getById(anyInt())).thenReturn(course);
        
        mockMvc.perform(post("/courses/update").param("courseId", String.valueOf(COURSE_ID))
                                               .flashAttr("courseModel", course))
               .andExpect(redirectedUrl("/courses/" + COURSE_ID));
        
        verify(courseServiceMock, times(1)).update(isA(CourseModel.class));
    }
    @Test
    void create_ShouldReturnBadRequestStatus() throws Exception {
        mockMvc.perform(post("/courses/create").content(BAD_CONTENT))
               .andExpect(status().is4xxClientError())
               .andExpect(view().name("error"));
    }
    
    @Test
    void create_ShouldCreateCourseAndRedirectToListView() throws Exception {
        mockMvc.perform(post("/courses/create").flashAttr("courseModel", course))
               .andExpect(redirectedUrl("/courses/list"));
        
        verify(courseServiceMock, times(1)).create(isA(CourseModel.class));
    }
    
    
    @Test
    void list_ShouldRenderCoursesView() throws Exception {
        mockMvc.perform(get("/courses/list"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("courses"))
               .andExpect(view().name("courses/list"));
        
        verify(courseServiceMock, times(1)).getAll();
    }
}