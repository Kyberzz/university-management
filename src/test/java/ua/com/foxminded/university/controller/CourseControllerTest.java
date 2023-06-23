package ua.com.foxminded.university.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.university.controller.CourseController.COURSES_ATTRIBUTE;
import static ua.com.foxminded.university.controller.CourseController.COURSE_ATTRIBUTE;
import static ua.com.foxminded.university.controller.CourseController.COURSE_TEMPLATE_PATH;
import static ua.com.foxminded.university.controller.CourseController.UPDATED_COURSE_ATTRIBUTE;
import static ua.com.foxminded.university.controller.DefaultControllerTest.*;
import static ua.com.foxminded.university.controller.TeacherController.TEACHERS_ATTRIBUTE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.dto.CourseDTO;
import ua.com.foxminded.university.dto.TeacherDTO;
import ua.com.foxminded.university.dtomother.CourseDTOMother;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.TeacherService;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class CourseControllerTest {
    
    private static final String EMAIL = "email@com";
    private static final int TEACHER_ID = 1;
    private static final int COURSE_ID = 1;
    
    @MockBean
    private TeacherService teacherServiceMock;
    
    @MockBean
    private CourseService courseServiceMock;
    
    @MockBean
    private LessonService lessonService;
    
    private MockMvc mockMvc;
    
    private CourseDTO course;
    private TeacherDTO teacher;
    
    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CourseController(
                teacherServiceMock, courseServiceMock, lessonService)).build();
        course = CourseDTOMother.complete().build();
        teacher = TeacherDTO.builder().id(TEACHER_ID).build();
    }
    
    @Test
    void getByTeacherId_ShouldRenderCourseView() throws Exception {
        when(teacherServiceMock.getTeacherByEmail(anyString())).thenReturn(teacher);
        
        mockMvc.perform(get("/courses/list/{teacherEmail}", EMAIL))
               .andDo(print())
               .andExpect(model().attributeExists(COURSE_ATTRIBUTE, COURSES_ATTRIBUTE))
               .andExpect(status().isOk());
        
        verify(teacherServiceMock).getTeacherByEmail(anyString());
        verify(courseServiceMock).getByTeacherId(anyInt());
    }
    
    @Test
    void deassignTeacherToCourse_ShouldRedirectToCourseView() throws Exception {
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
        when(courseServiceMock.getByIdWithLessonsAndTeachers(anyInt())).thenReturn(course);
        
        mockMvc.perform(get("/courses/{id}", COURSE_ID))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(model().attributeExists(COURSE_ATTRIBUTE, 
                                                  TEACHERS_ATTRIBUTE, 
                                                  UPDATED_COURSE_ATTRIBUTE))
               .andExpect(view().name(COURSE_TEMPLATE_PATH));
        
        verify(courseServiceMock).getByIdWithLessonsAndTeachers(anyInt());
    }
    
    @Test
    void deleteById_ShouldDeleteCourseAndRedirectToListView() throws Exception {
        mockMvc.perform(post("/courses/delete").param("courseId", String.valueOf(COURSE_ID)))
               .andExpect(redirectedUrl("/courses/list"));
        
        verify(courseServiceMock).deleteById(anyInt());
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
        mockMvc.perform(post("/courses/update").param("courseId", String.valueOf(COURSE_ID))
                                               .flashAttr(UPDATED_COURSE_ATTRIBUTE, course))
               .andExpect(redirectedUrl(new StringBuilder().append(CourseController.COURSES_PATH)
                                                           .append(COURSE_ID).toString()));
        
        verify(courseServiceMock).update(isA(CourseDTO.class));
    }
    
    @Test
    void create_ShouldReturnBadRequestStatus() throws Exception {
        mockMvc.perform(post("/courses/create").content(BAD_CONTENT))
               .andExpect(status().is4xxClientError())
               .andExpect(view().name("error"));
    }
    
    @Test
    void create_ShouldCreateCourseAndRedirectToListView() throws Exception {
        mockMvc.perform(post("/courses/create").flashAttr(COURSE_ATTRIBUTE, course))
               .andDo(print())
               .andExpect(redirectedUrl("/courses/list"));
        
        verify(courseServiceMock).create(isA(CourseDTO.class));
    }
    
    @Test
    void getAll_ShouldRenderCoursesView() throws Exception {
        mockMvc.perform(get("/courses/list"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("courses"))
               .andExpect(view().name("courses/list"));
        
        verify(courseServiceMock).getAll();
    }
}
