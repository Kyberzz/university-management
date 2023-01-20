package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.service.TimetableService;

@WebMvcTest
class TimetableControllerTest {
    
    @MockBean
    private TimetableService<TimetableModel> timetableService;
    
    @Autowired
    private MockMvc mockMvc;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TimetableController(timetableService)).build();
    }

    @Test
    void shouldRenderTimetablesList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/timetables/list"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.model().attributeExists("timetables"))
               .andExpect(MockMvcResultMatchers.view().name("timetables/list"));
    }
}
