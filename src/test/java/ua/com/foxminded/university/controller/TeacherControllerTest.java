package ua.com.foxminded.university.controller;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ua.com.foxminded.university.controller.DefaultController.SLASH;
import static ua.com.foxminded.university.controller.LessonController.LESSONS_ATTRIBUTE;
import static ua.com.foxminded.university.controller.LessonControllerTest.LESSON_ID;
import static ua.com.foxminded.university.controller.TeacherController.TEACHERS_ATTRIBUTE;
import static ua.com.foxminded.university.controller.TeacherController.TEACHERS_LIST_TEMLPATE_PATH;
import static ua.com.foxminded.university.controller.TeacherController.TEACHER_ATTRIBUTE;
import static ua.com.foxminded.university.controller.TeacherController.TEACHER_TEMPLATE_PATH;
import static ua.com.foxminded.university.controller.UserController.USERS_ATTRIBUTE;
import static ua.com.foxminded.university.controller.UserControllerTest.USER_ID;

import java.util.List;

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

import ua.com.foxminded.university.dto.LessonDTO;
import ua.com.foxminded.university.dto.TeacherDTO;
import ua.com.foxminded.university.dto.UserDTO;
import ua.com.foxminded.university.dtomother.LessonDTOMother;
import ua.com.foxminded.university.dtomother.UserDTOMother;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.service.UserService;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class TeacherControllerTest {
    
    public static final int TEACHER_ID = 1;
    
    @MockBean
    private TeacherService teacherServiceMock;
    
    @MockBean
    private UserService userServiceMock;
    
    @MockBean
    private LessonService lessonServiceMock;
    
    private MockMvc mockMvc;
    private TeacherDTO teacherDto;
    private List<TeacherDTO> teacherDtoList;
    private UserDTO userDto;
    private LessonDTO lessonDto;
    private List<LessonDTO> lessonsDtoList;
    private List<UserDTO> usersDtoList;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new TeacherController(teacherServiceMock, 
                                      userServiceMock,
                                      lessonServiceMock)).build();
        userDto = UserDTOMother.complete().build();
        teacherDto = TeacherDTO.builder().user(userDto).build();
        teacherDtoList = asList(teacherDto);
        lessonDto = LessonDTOMother.complete().build();
        lessonsDtoList = asList(lessonDto);
        usersDtoList = asList(userDto);
    }
   
   @Test
   void deleteTeacherLesson_ShouldRedirectToGetById() throws Exception {
       mockMvc.perform(post("/teachers/{teacherId}/delete-lesson", TEACHER_ID)
                   .param("lessonId", String.valueOf(LESSON_ID)))
              .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                          .append(TEACHER_TEMPLATE_PATH)
                                                          .append(SLASH)
                                                          .append(TEACHER_ID)                                                          .toString()));
       
       verify(lessonServiceMock).deleteById(anyInt());
   }

   @Test
   void getById() throws Exception {
       when(teacherServiceMock.getById(anyInt())).thenReturn(teacherDto);
       when(lessonServiceMock.getByTeacherId(anyInt())).thenReturn(lessonsDtoList);
       when(userServiceMock.getAll()).thenReturn(usersDtoList);
       
       mockMvc.perform(get("/teachers/teacher/{teacherId}", TEACHER_ID))
              .andDo(print())
              .andExpect(model().attributeExists(USERS_ATTRIBUTE, 
                                                 LESSONS_ATTRIBUTE, 
                                                 TEACHER_ATTRIBUTE))
              .andExpect(view().name(TEACHER_TEMPLATE_PATH));
       
       verify(teacherServiceMock).getById(anyInt());
       verify(lessonServiceMock).getByTeacherId(anyInt());
       verify(userServiceMock).getAll();
       verify(lessonServiceMock).addLessonTiming(ArgumentMatchers.<LessonDTO>anyList());
   }
    
    @Test
    void update_ShouldRedirectToGetById() throws Exception {
        when(teacherServiceMock.getById(anyInt())).thenReturn(teacherDto);
        
        mockMvc.perform(post("/teachers/{teacherId}/update", TEACHER_ID)
                    .param("userId", String.valueOf(USER_ID)))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(TEACHER_TEMPLATE_PATH)
                                                           .append(SLASH)
                                                           .append(TEACHER_ID)
                                                           .toString()));
        verify(teacherServiceMock).getById(anyInt());
        verify(teacherServiceMock).update(isA(TeacherDTO.class));
    }
    
    @Test
    void deleteById_ShouldRedirectToGetAllTeachers() throws Exception {
        mockMvc.perform(post("/teachers/delete/{teacherId}", TEACHER_ID))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(TEACHERS_LIST_TEMLPATE_PATH)
                                                           .toString()));
        verify(teacherServiceMock).deleteById(anyInt());
    }
    
    @Test
    void create_ShouldRedirectToGetAllTeachers() throws Exception {
        mockMvc.perform(post("/teachers/create").param("userId", String.valueOf(USER_ID)))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(TEACHERS_LIST_TEMLPATE_PATH)
                                                           .toString()));
        
        verify(teacherServiceMock).create(isA(TeacherDTO.class));
    }
    
    @Test
    void getAll_ShouldRenderTeachersListTemplate() throws Exception {
        when(teacherServiceMock.getAll()).thenReturn(teacherDtoList);
        
        mockMvc.perform(get("/teachers/list"))
               .andDo(print())
               .andExpect(model().attributeExists(USERS_ATTRIBUTE, 
                                                  TEACHER_ATTRIBUTE, 
                                                  TEACHERS_ATTRIBUTE))
               .andExpect(view().name(TEACHERS_LIST_TEMLPATE_PATH));
        
        verify(teacherServiceMock).getAll();
        verify(teacherServiceMock).sortByLastName(teacherDtoList);
        verify(userServiceMock).getAll();
    }
}
