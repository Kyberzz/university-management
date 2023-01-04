package ua.com.foxminded.university.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.service.CourseService;

@SpringBootTest
class CourseControllerTest {
    
    @MockBean
    private CourseService<CourseModel> courseServiceMock;
    
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
