package ua.com.foxminded.university.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    
    private static final String BAD_CONTENT = "bad content";
    private static final int ID = 1;
    
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
    void get_ShouldRenderCourseView() throws Exception {
        when(courseServiceMock.getTimetableAndTeachersByCourseId(anyInt())).thenReturn(course);
        
        mockMvc.perform(get("/courses/{id}", ID))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("course"))
        .andExpect(view().name("courses/course"));
    }
    
    @Test
    void delete_ShouldDeleteCourseAndRedirectToListView() throws Exception {
        mockMvc.perform(post("/courses/delete").param("courseId", String.valueOf(ID)))
               .andExpect(redirectedUrl("/courses/list"));
        verify(courseServiceMock, times(1)).deleteById(anyInt());
    }
    
    @Test
    void update_ShouldReturnBadRequestStatus() throws Exception {
        mockMvc.perform(post("/courses/update").param("courseId", String.valueOf(ID))
                                               .content(BAD_CONTENT))
               .andExpect(status().is4xxClientError())
               .andExpect(view().name("error"));
    }
    
    @Test
    void update_ShouldUpdateCourseAndRedirectToCourseView() throws Exception {
        when(courseServiceMock.getById(anyInt())).thenReturn(course);
        
        mockMvc.perform(post("/courses/update").param("courseId", String.valueOf(ID))
                                               .flashAttr("courseModel", course))
               .andExpect(redirectedUrl("/courses/" + ID));
        
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
