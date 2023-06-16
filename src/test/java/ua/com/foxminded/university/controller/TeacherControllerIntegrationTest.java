package ua.com.foxminded.university.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.controller.DefaultControllerTest.ADMIN_EMAIL;
import static ua.com.foxminded.university.controller.DefaultControllerTest.TEACHER_EMAIL;
import static ua.com.foxminded.university.entity.RoleAuthority.ROLE_ADMIN;
import static ua.com.foxminded.university.entity.RoleAuthority.ROLE_TEACHER;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Teacher;
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.entity.UserAuthority;
import ua.com.foxminded.university.entitymother.LessonMother;
import ua.com.foxminded.university.entitymother.UserMother;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.TeacherRepository;
import ua.com.foxminded.university.repository.UserAuthorityRepository;
import ua.com.foxminded.university.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@DirtiesContext
class TeacherControllerIntegrationTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserAuthorityRepository userAuthorityRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private LessonRepository lessonRepository;
    
    @Autowired
    private MockMvc mockMvc;
    
    private User adminUser;
    private User teacherUser;
    private UserAuthority adminUserAuthority;
    private UserAuthority teacherUserAuthority;
    private Teacher teacher;
    private Lesson lesson;
    
    @BeforeTransaction
    void init() {
        adminUser = UserMother.complete().email(ADMIN_EMAIL).build();
        userRepository.saveAndFlush(adminUser);
        
        teacherUser = UserMother.complete().email(TEACHER_EMAIL).build();
        userRepository.saveAndFlush(teacherUser);
        
        adminUserAuthority = UserAuthority.builder()
                                          .roleAuthority(ROLE_ADMIN)
                                          .user(adminUser).build();
        userAuthorityRepository.saveAndFlush(adminUserAuthority);
        
        teacherUserAuthority = UserAuthority.builder()
                                            .roleAuthority(ROLE_TEACHER)
                                            .user(teacherUser).build();
        userAuthorityRepository.saveAndFlush(teacherUserAuthority);
        
        teacher = Teacher.builder().user(teacherUser).build();
        teacherRepository.saveAndFlush(teacher);
        
        lesson = LessonMother.complete().teacher(teacher).build();
        lessonRepository.saveAndFlush(lesson);
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void deleteTeacherLesson_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/teachers/{teacherId}/delete-lesson", teacher.getId())
                    .param("lessonId", String.valueOf(lesson.getId()))
                    .with(csrf()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getById_ShouldReturnStatusIsOk() throws Exception {
        mockMvc.perform(get("/teachers/teacher/{teacherId}", teacher.getId()))
               .andExpect(status().isOk());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void update_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/teachers/{teacherId}/update", teacher.getId())
                    .param("userId", String.valueOf(adminUser.getId()))
                    .with(csrf()))
           .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void deleteById_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/teachers/delete/{teacherId}", teacher.getId())
                    .with(csrf()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void create_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/teachers/create")
                    .param("userId", String.valueOf(teacherUser.getId()))
                    .with(csrf()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getAll_ShouldReturnStatusIsOk() throws Exception {
        mockMvc.perform(get("/teachers/list"))
               .andExpect(status().isOk());
    }
}
