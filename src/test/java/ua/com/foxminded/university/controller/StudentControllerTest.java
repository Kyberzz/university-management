package ua.com.foxminded.university.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;

@ExtendWith(SpringExtension.class)
class StudentControllerTest {
    
    @MockBean
    private StudentService<StudentModel> studentServiceMock;
    
    @MockBean
    private GroupService<GroupModel> groupServiceMock;
    
    private MockMvc mockMvc;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new StudentController(studentServiceMock, 
                                                                        groupServiceMock))
                                 .build();
    }
    
    @Test
    void getAllStudents_shouldRenderStudentsList() throws Exception {
        mockMvc.perform(get("/students/list"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("students"))
               .andExpect(view().name("students/list"));
    }
}
