package ua.com.foxminded.university.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.university.controller.DefaultControllerTest.ADMIN_EMAIL;
import static ua.com.foxminded.university.entity.Authority.ADMIN;
import static ua.com.foxminded.university.entity.RoleAuthority.ROLE_ADMIN;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.WebApplicationContext;

import ua.com.foxminded.university.dto.StudentDTO;
import ua.com.foxminded.university.dtomother.StudentDTOMother;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.entity.UserAuthority;
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.entitymother.StudentMother;
import ua.com.foxminded.university.entitymother.UserMother;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.repository.UserAuthorityRepository;
import ua.com.foxminded.university.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"prod", "testcontainers"})
@Transactional
class StudentControllerIntegrationTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserAuthorityRepository userAuthorityRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private WebApplicationContext context;
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    private Student student;
    private StudentDTO studentDto;
    
    @BeforeTransaction
    void init() {
        studentDto = StudentDTOMother.complete().build();
        student = StudentMother.complete().build();
        
        new TransactionTemplate(transactionManager).execute(transactionStatus -> {
            userRepository.deleteAll();
            User user = UserMother.complete().email(ADMIN_EMAIL).build();
            userRepository.saveAndFlush(user);
            UserAuthority userAuthority = UserAuthority.builder()
                    .roleAuthority(ROLE_ADMIN)
                    .user(user).build();
            userAuthorityRepository.save(userAuthority);
            studentRepository.saveAndFlush(student);
            return null;
        });
    }
    
    @AfterTransaction
    void cleanUp() {
        userRepository.deleteAll();
    }
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                                 .apply(springSecurity()).build();
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void create_ShouldAuthorizeCredentialsAndRedirect() throws Exception {
        mockMvc.perform(post("/students/create").flashAttr("studentModel", studentDto)
                                                .with(csrf()))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void list_ShouldAuthenticateCredentialsAndReturnStatusIsOk() throws Exception {
        mockMvc.perform(get("/students/list"))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().isOk());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void update_ShouldAuthorizeCredentialsAndRedirect() throws Exception {
        mockMvc.perform(post("/students/{studentId}/update", 
                             String.valueOf(student.getId()))
                    .flashAttr("studentModel", studentDto)
                    .with(csrf()))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void delete_ShouldAuthenticateCredentialsAndRedirect() throws Exception {
        mockMvc.perform(post("/students/delete")
                    .param("studentId", String.valueOf(student.getId()))
                    .with(csrf()))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
}
