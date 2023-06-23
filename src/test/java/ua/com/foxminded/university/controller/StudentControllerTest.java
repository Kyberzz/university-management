package ua.com.foxminded.university.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ua.com.foxminded.university.controller.DefaultController.SLASH;
import static ua.com.foxminded.university.controller.DefaultControllerTest.ERROR_VIEW;
import static ua.com.foxminded.university.controller.GroupController.GROUPS_ATTRIBUTE;
import static ua.com.foxminded.university.controller.StudentController.STUDENTS_ATTRIBUTE;
import static ua.com.foxminded.university.controller.StudentController.STUDENTS_LIST_TEMPLATE_PATH;
import static ua.com.foxminded.university.controller.StudentController.STUDENT_ATTRIBUTE;
import static ua.com.foxminded.university.controller.UserControllerTest.USER_ID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.dto.StudentDTO;
import ua.com.foxminded.university.dto.UserDTO;
import ua.com.foxminded.university.dtomother.UserDTOMother;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;
import ua.com.foxminded.university.service.UserService;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class StudentControllerTest {
    
    public static final int STUDENT_ID = 1;
    
    @MockBean
    private StudentService studentServiceMock;
    
    @MockBean
    private GroupService groupServiceMock;
    
    @MockBean
    private UserService userServiceMock;
    
    private MockMvc mockMvc;
    private StudentDTO studentDto;
    private UserDTO studentUserDto;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new StudentController(
                studentServiceMock, groupServiceMock, userServiceMock)).build();
        studentUserDto = UserDTOMother.complete().build();
        studentDto = StudentDTO.builder().user(studentUserDto).build();
    }
    
    @Test
    void create_ShouldReturnBadRequestStatus() throws Exception {
        studentDto.setUser(null);
        mockMvc.perform(post("/students/create").flashAttr("studentModel", studentDto))
               .andDo(print())
               .andExpect(status().is4xxClientError())
               .andExpect(view().name(ERROR_VIEW));
    }
    
    @Test
    void create_ShouldRedirectToGetAll() throws Exception {
        studentDto.getUser().setId(USER_ID);
        when(userServiceMock.createUserPerson(isA(UserDTO.class))).thenReturn(studentUserDto);
        
        mockMvc.perform(post("/students/create")
                    .flashAttr(STUDENT_ATTRIBUTE, studentDto))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(STUDENTS_LIST_TEMPLATE_PATH)
                                                           .toString()));
        
        verify(userServiceMock).createUserPerson(isA(UserDTO.class));
        verify(studentServiceMock).create(isA(StudentDTO.class));
        verify(userServiceMock).updateEmail(anyInt(), anyString());
    }
    
    @Test
    void getAll_ShouldCallServicesAndRenderListView() throws Exception {
        mockMvc.perform(get("/students/list"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(model().attributeExists(STUDENT_ATTRIBUTE, 
                                                  STUDENTS_ATTRIBUTE, 
                                                  GROUPS_ATTRIBUTE))
               .andExpect(view().name(STUDENTS_LIST_TEMPLATE_PATH));
        
        verify(studentServiceMock).getAll();
        verify(groupServiceMock).getAll();
    }
    
    @Test
    void update_ShouldReturnBadRequestStatus() throws Exception {
        studentDto.setUser(null);
        mockMvc.perform(post("/students/{studentId}/update", STUDENT_ID)
                    .flashAttr("studentModel", studentDto))
               .andDo(print())
               .andExpect(status().is4xxClientError())
               .andExpect(view().name(ERROR_VIEW));
    }
    
    @Test
    void update_ShouldRedirectToGetAll() throws Exception {
        studentDto.getUser().setId(USER_ID);
        when(studentServiceMock.getById(anyInt())).thenReturn(studentDto);
        
        mockMvc.perform(post("/students/{studentId}/update", STUDENT_ID)
                    .flashAttr(STUDENT_ATTRIBUTE, studentDto))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(STUDENTS_LIST_TEMPLATE_PATH)
                                                           .toString()));
        verify(studentServiceMock).getById(anyInt());
        verify(studentServiceMock).update(isA(StudentDTO.class));
        verify(userServiceMock).updateEmail(anyInt(), anyString());
        verify(userServiceMock).updateUserPerson(isA(UserDTO.class));
    }
    
    @Test
    void delete_ShouldCallStudentServiceAndRedirect() throws Exception {
        mockMvc.perform(post("/students/delete").param("studentId", String.valueOf(STUDENT_ID)))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(STUDENTS_LIST_TEMPLATE_PATH)
                                                           .toString()));
        verify(studentServiceMock).deleteById(anyInt());
    }
}
