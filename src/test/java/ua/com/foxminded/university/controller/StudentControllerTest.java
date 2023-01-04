package ua.com.foxminded.university.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.service.StudentService;

@WebMvcTest
public class StudentControllerTest {
    
    @MockBean
    private StudentService<StudentModel> studentServiceMock;
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void shouldRenderStudentsList() throws Exception {
        mockMvc.perform(get("/index").param("getAllStudents", "#"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("students"))
               .andExpect(view().name("students/list"));
    }
}
