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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.dto.GroupDTO;
import ua.com.foxminded.university.dto.StudentDTO;
import ua.com.foxminded.university.dtomother.GroupDTOMother;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.StudentService;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class GroupControllerTest {
    
    public static final String GROUP_NAME_PARAMETER = "name";
    public static final int GROUP_ID = 1;
    
    @MockBean
    private GroupService groupServiceMock;
    
    @MockBean
    private StudentService studentServiceMock;
    
    @MockBean
    private LessonService lessonService;
    
    private MockMvc mockMvc;
    private GroupDTO group;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new GroupController(
                groupServiceMock, studentServiceMock, lessonService)).build();
        group = GroupDTOMother.complete().build();
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
                    .param("studentId", "1,5"))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(GROUPS_PATH)
                                                           .append(GROUP_ID).toString()));
        verify(groupServiceMock).assignGroup(anyInt(), any(int[].class));
    }
    
    @Test
    void delete_ShouldRedirectToList() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/delete", GROUP_ID))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(GROUPS_LIST_TEMPLATE_PATH)
                                                           .toString()));
        verify(groupServiceMock).deleteById(anyInt());
    }
    
    @Test
    void create_ShouldRedirectToList() throws Exception {
        mockMvc.perform(post("/groups/create").param("name", group.getName()))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(GROUPS_LIST_TEMPLATE_PATH)
                                                           .toString()));
        verify(groupServiceMock).create(isA(GroupDTO.class));
    }
    
    @Test
    void update_ShouldRedirectToGetById() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/update", GROUP_ID)
                    .param(GROUP_NAME_PARAMETER, group.getName()))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(GROUPS_PATH)
                                                           .append(GROUP_ID).toString()));
        verify(groupServiceMock).update(isA(GroupDTO.class));
    }
    
    @Test
    void getById_ShouldRenderGroupTemplate() throws Exception {
        when(groupServiceMock.getGroupRelationsById(GROUP_ID)).thenReturn(group);
        mockMvc.perform(get("/groups/{groupId}", GROUP_ID))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(model().attributeExists(GROUP_ATTRIBUTE))
               .andExpect(view().name("groups/group"));
        verify(groupServiceMock).sortContainedStudentsByLastName(any(GroupDTO.class));
        verify(studentServiceMock).sortByLastName(ArgumentMatchers.<StudentDTO>anyList());
    }
    
    @Test
    void getAll_shouldRenderListTemplate() throws Exception {
        mockMvc.perform(get("/groups/list"))
               .andDo(print())
               .andExpect(model().attributeExists(GROUPS_ATTRIBUTE))
               .andExpect(view().name(GROUPS_LIST_TEMPLATE_PATH));
        
        verify(groupServiceMock).getAll();
    }
}
