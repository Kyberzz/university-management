package ua.com.foxminded.university.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.controller.DefaultControllerTest.ADMIN_EMAIL;
import static ua.com.foxminded.university.controller.DefaultControllerTest.STUDENT_EMAIL;
import static ua.com.foxminded.university.controller.StudentController.*;
import static ua.com.foxminded.university.entity.Authority.ADMIN;
import static ua.com.foxminded.university.entity.RoleAuthority.ROLE_ADMIN;
import static ua.com.foxminded.university.entity.RoleAuthority.ROLE_STUDENT;

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

import ua.com.foxminded.university.dto.GroupDTO;
import ua.com.foxminded.university.dto.StudentDTO;
import ua.com.foxminded.university.dto.UserDTO;
import ua.com.foxminded.university.dtomother.UserDTOMother;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.entity.UserAuthority;
import ua.com.foxminded.university.entitymother.GroupMother;
import ua.com.foxminded.university.entitymother.UserMother;
import ua.com.foxminded.university.repository.GroupRepository;
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
    
    @Autowired
    private GroupRepository groupRepository;
    
    private User studentUser;
    private Student student;
    private StudentDTO studentDto;
    private Group group;
    private UserDTO studentUserDto;
    
    @BeforeTransaction
    void init() {
        studentUserDto = UserDTOMother.complete().build();
        studentDto = StudentDTO.builder().user(studentUserDto).build();
        
        new TransactionTemplate(transactionManager).execute(transactionStatus -> {
            userRepository.deleteAll();
            User adminUser = UserMother.complete().email(ADMIN_EMAIL).build();
            userRepository.saveAndFlush(adminUser);
            UserAuthority adminUserAuthority = UserAuthority.builder()
                    .roleAuthority(ROLE_ADMIN)
                    .user(adminUser).build();
            userAuthorityRepository.save(adminUserAuthority);
            
            studentUser = UserMother.complete().email(STUDENT_EMAIL).build();
            userRepository.saveAndFlush(studentUser);
            
            UserAuthority studentUserAuthority = UserAuthority.builder()
                    .roleAuthority(ROLE_STUDENT)
                    .user(studentUser).build();
            userAuthorityRepository.saveAndFlush(studentUserAuthority);
            student = Student.builder().user(studentUser).build();
            studentRepository.saveAndFlush(student);
            
            group = GroupMother.complete().build();
            groupRepository.saveAndFlush(group);
            return null;
        });
    }
    
    @AfterTransaction
    void cleanUp() {
        userRepository.deleteAll();
        groupRepository.delete(group);
    }
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                                 .apply(springSecurity()).build();
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void create_ShouldAuthorizeCredentialsAndRedirect() throws Exception {
        studentDto.getUser().setId(studentUser.getId());
        
        mockMvc.perform(post("/students/create").flashAttr(STUDENT_ATTRIBUTE, studentDto)
                                                .with(csrf()))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getAll_ShouldAuthenticateCredentialsAndReturnStatusIsOk() throws Exception {
        mockMvc.perform(get("/students/list"))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().isOk());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void update_ShouldAuthorizeCredentialsAndRedirect() throws Exception {
        studentDto.setGroup(new GroupDTO());
        studentDto.getGroup().setId(group.getId());
        studentDto.getUser().setId(studentUser.getId());
        
        mockMvc.perform(post("/students/{studentId}/update", 
                             String.valueOf(student.getId()))
                    .flashAttr(STUDENT_ATTRIBUTE, studentDto)
                    .with(csrf()))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void delete_ShouldAuthenticateCredentialsAndRedirect() throws Exception {
        mockMvc.perform(post("/students/delete")
                    .param(STUDENT_ID_PARAMETER, String.valueOf(student.getId()))
                    .with(csrf()))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
}
