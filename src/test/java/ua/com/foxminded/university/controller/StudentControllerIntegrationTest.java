package ua.com.foxminded.university.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.service.StudentService;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)

class StudentControllerIntegrationTest {
    
    @Autowired
    private StudentService<StudentModel> studentService;
    
    @Autowired
    private MockMvc mockMvc;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new StudentController(studentService))
                                 .build();
    }

    @Test
    void getAllStudents_ShouldReturnAllStudentsIncludinEmail() throws Exception {
        
        mockMvc.perform(get("/students/list"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("students"))
        .andExpect(view().name("students/list"));
    }

}
