package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.service.TeacherService;

@ExtendWith(SpringExtension.class)
class TeacherControllerTest {
    
    @MockBean
    private TeacherService teacherServiceMock;
    
    private MockMvc mockMvc;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TeacherController(teacherServiceMock))
                                 .build();
    }

    @Test
    void shuldRenderTeachersListView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/list"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.model().attributeExists("teachers"))
               .andExpect(MockMvcResultMatchers.view().name("teachers/list"));
    }
}
