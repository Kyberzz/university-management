package ua.com.foxminded.university.controller;

import static ua.com.foxminded.university.controller.GroupController.*;
import static ua.com.foxminded.university.controller.StudentControllerTest.STUDENT_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.modelmother.GroupModelMother;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;

@ExtendWith(SpringExtension.class)
class GroupControllerTest {
    
    public static final int GROUP_ID = 1;
    
    @MockBean
    private GroupService groupServiceMock;
    
    @MockBean
    private StudentService studentServiceMock;
    
    private MockMvc mockMvc;
    private GroupModel groupModel;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new GroupController(
                groupServiceMock, studentServiceMock)).build();
        groupModel = GroupModelMother.complete().build();
    }
    
    @Test
    void deassignGroup_ShouldRedirectToGetById() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/deassign-group", GROUP_ID)
                    .param("studentId", String.valueOf(STUDENT_ID)))
        .andDo(print())
        .andExpect(redirectedUrl(new StringBuilder().append(GROUPS_PATH)
                                                    .append(GROUP_ID).toString()));
        verify(groupServiceMock).deassignGroup(anyInt());
    }
    
    @Test
    void assignGroup_ShouldRedirectToGetById() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/assign-group", GROUP_ID)
                    .param("studentIds", "1,5"))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(GROUPS_PATH)
                                                           .append(GROUP_ID) .toString()));
        verify(groupServiceMock).assignGroup(anyInt(), any(int[].class));
    }
    
    @Test
    void delete_ShouldRedirectToList() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/delete", GROUP_ID))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(GROUPS_PATH)
                                                           .append(LIST_TEMPLATE).toString()));
        verify(groupServiceMock).deleteById(anyInt());
    }
    
    @Test
    void create_ShouldRedirectToList() throws Exception {
        mockMvc.perform(post("/groups/create").param("name", groupModel.getName()))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(GROUPS_PATH)
                                                           .append(LIST_TEMPLATE).toString()));
        verify(groupServiceMock).create(isA(GroupModel.class));
    }
    
    @Test
    void update_ShouldRedirectToGetById() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/update", GROUP_ID)
                    .param("name", groupModel.getName()))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(GROUPS_PATH)
                                                           .append(GROUP_ID).toString()));
        verify(groupServiceMock).update(isA(GroupModel.class));
    }
    
    @Test
    void getById_ShouldRenderGroupTemplate() throws Exception {
        when(groupServiceMock.getGroupRelationsById(GROUP_ID)).thenReturn(groupModel);
        mockMvc.perform(get("/groups/{groupId}", GROUP_ID))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(model().attributeExists(GROUP_MODEL_ATTRIBUTE))
               .andExpect(view().name("groups/group"));
        verify(groupServiceMock).sortStudentsByLastName(any(GroupModel.class));
        verify(studentServiceMock).sortByLastName(ArgumentMatchers.<StudentModel>anyList());
    }
    
    @Test
    void getAllGroups_shouldRenderListTemplate() throws Exception {
        mockMvc.perform(get("/groups/list"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("groups"))
               .andExpect(view().name("groups/list"));
        
        verify(groupServiceMock).getAll();
    }
}
