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

import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.TimetableService;

@ExtendWith(SpringExtension.class)
class TimetableControllerTest {
    
    @MockBean
    private TimetableService timetableServiceMock;
    
    @MockBean
    private CourseService courseServiceMock;
    
    @MockBean
    private GroupService groupService;
    
    private MockMvc mockMvc;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new TimetableController(timetableServiceMock, 
                                        courseServiceMock, 
                                        groupService)).build();
    }

    @Test
    void shouldRenderTimetablesList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/timetables/list"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.model().attributeExists("timetables"))
               .andExpect(MockMvcResultMatchers.view().name("timetables/list"));
    }
}