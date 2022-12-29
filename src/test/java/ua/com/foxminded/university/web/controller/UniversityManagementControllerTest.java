package ua.com.foxminded.university.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.university.buisness.model.CourseModel;
import ua.com.foxminded.university.buisness.model.GroupModel;
import ua.com.foxminded.university.buisness.model.StudentModel;
import ua.com.foxminded.university.buisness.model.TeacherModel;
import ua.com.foxminded.university.buisness.model.TimetableModel;
import ua.com.foxminded.university.buisness.model.service.CourseService;
import ua.com.foxminded.university.buisness.model.service.GroupService;
import ua.com.foxminded.university.buisness.model.service.StudentService;
import ua.com.foxminded.university.buisness.model.service.TeacherService;
import ua.com.foxminded.university.buisness.model.service.TimetableService;

@WebMvcTest
class UniversityManagementControllerTest {
    
    @MockBean
    TeacherService<TeacherModel> teacherService;
    
    @MockBean 
    CourseService<CourseModel> coureseService;
    
    @MockBean
    TimetableService<TimetableModel> timetableService;
    
    @MockBean
    GroupService<GroupModel> groupService;
    
    @MockBean
    StudentService<StudentModel> studentService;
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void getIndex_gettingIndexPage_CorrectResponce() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(view().name("index"));
    }
    
    @Test
    void getAllTimetables_gettingTimetables_CorrectResponce() throws Exception {
        mockMvc.perform(get("/index").param("getAllTimetables", "#"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("timetables"))
               .andExpect(view().name("timetables"));
    }
    
    @Test
    void getAllTeachers_gettingTeachers_CorrectResponce() throws Exception {
        mockMvc.perform(get("/index").param("getAllTeachers", "#"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("teachers"))
               .andExpect(view().name("teachers"));
    }
    
    @Test
    void getAllCourses_gettingCourses_CorrectResponce() throws Exception {
        mockMvc.perform(get("/index").param("getAllCourses", "#"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("courses"))
               .andExpect(view().name("courses"));
    }
    
    @Test 
    void getAllGroups_gettingGroups_CorrectResponce() throws Exception {
        mockMvc.perform(get("/index").param("getAllGroups", "#"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("groups"))
               .andExpect(view().name("groups"));
    }
    
    @Test
    void getAllStudents_gettingStudents_CorrectResponce() throws Exception {
        mockMvc.perform(get("/index").param("getAllStudents", "#"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("students"))
               .andExpect(view().name("students"));
    }
}
