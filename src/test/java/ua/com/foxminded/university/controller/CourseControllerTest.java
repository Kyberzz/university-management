package ua.com.foxminded.university.controller;

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

import ua.com.foxminded.university.service.CourseService;

@ExtendWith(SpringExtension.class)
class CourseControllerTest {
    
    @MockBean
    private CourseService courseServiceMock;
    
    private MockMvc mockMvc;
    
    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CourseController(courseServiceMock))
                                 .build();
    }
    
    @Test
    void getAllCourses_ShuldRenderCoursesView() throws Exception {
        mockMvc.perform(get("/courses/list"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("courses"))
               .andExpect(view().name("courses/list"));
    }
}
