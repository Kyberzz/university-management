package ua.com.foxminded.university.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.service.CourseService;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class CourseControllerTest {
    
    @Mock
    private CourseService<CourseModel> courseServiceMock;
    
    @Autowired
    private MockMvc mockMvc;
    
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CourseController(courseServiceMock)).build();
    }


    @Test
    void shuldRenderCoursesListView() throws Exception {
        mockMvc.perform(get("/index").param("getAllCourses", "#"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("courses"))
               .andExpect(view().name("courses/list"));
    }
}
