package ua.com.foxminded.university.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.university.controller.DefaultControllerTest.AUTHORIZED_EMAIL;
import static ua.com.foxminded.university.controller.StudentControllerTest.STUDENT_ID;
import static ua.com.foxminded.university.entity.RoleAuthority.ROLE_ADMIN;
import static ua.com.foxminded.university.model.Authority.ADMIN;

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

import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.UserAuthorityEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.entitymother.StudentEntityMother;
import ua.com.foxminded.university.entitymother.UserEntityMother;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.modelmother.StudentModelMother;
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
    
    private StudentEntity studentEntity;
    private StudentModel studentModel;
    
    @BeforeTransaction
    void init() {
        studentModel = StudentModelMother.complete().build();
        studentEntity = StudentEntityMother.complete().build();
        
        new TransactionTemplate(transactionManager).execute(transactionStatus -> {
            userRepository.deleteAll();
            UserEntity user = UserEntityMother.complete().email(AUTHORIZED_EMAIL).build();
            userRepository.saveAndFlush(user);
            UserAuthorityEntity userAuthority = UserAuthorityEntity.builder()
                    .roleAuthority(ROLE_ADMIN)
                    .user(user).build();
            userAuthorityRepository.save(userAuthority);
            studentRepository.saveAndFlush(studentEntity);
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
    @WithUserDetails(AUTHORIZED_EMAIL)
    void create_ShouldAuthorizeCredentialsAndRedirect() throws Exception {
        mockMvc.perform(post("/students/create").flashAttr("studentModel", studentModel)
                                                .with(csrf()))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void list_ShouldAuthenticateCredentialsAndReturnStatusIsOk() throws Exception {
        mockMvc.perform(get("/students/list"))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().isOk());
    }
    
    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void update_ShouldAuthorizeCredentialsAndRedirect() throws Exception {
        mockMvc.perform(post("/students/{studentId}/update", 
                             String.valueOf(studentEntity.getId()))
                    .flashAttr("studentModel", studentModel)
                    .with(csrf()))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void delete_ShouldAuthenticateCredentialsAndRedirect() throws Exception {
        mockMvc.perform(post("/students/delete")
                    .param("studentId", String.valueOf(studentEntity.getId()))
                    .with(csrf()))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
}
