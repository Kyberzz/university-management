package ua.com.foxminded.university.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.service.GroupService;

@ExtendWith(MockitoExtension.class)
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
        mockMvc.perform(get("/index").param("getAllGroups", "#"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("groups"))
               .andExpect(view().name("groups/list"));
    }
}
