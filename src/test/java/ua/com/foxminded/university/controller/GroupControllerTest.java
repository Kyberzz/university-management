package ua.com.foxminded.university.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.service.GroupService;

@WebMvcTest
class GroupControllerTest {
    
    @Mock
    private GroupService<GroupModel> groupServiceMock;
    
    private MockMvc mockMvc;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new GroupController(groupServiceMock)).build();
    }
    
    @Test
    void shouldRenderGroupsList() throws Exception {
        mockMvc.perform(get("/groups/list"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("groups"))
               .andExpect(view().name("groups/list"));
    }
}
