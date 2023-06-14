package ua.com.foxminded.university.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.entity.Authority.ADMIN;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.com.foxminded.university.entity.Authority;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.entitymother.GroupMother;
import ua.com.foxminded.university.entitymother.StudentMother;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.StudentRepository;

@SpringBootTest
@ActiveProfiles("prod")
@AutoConfigureMockMvc
@Testcontainers
class GroupControllerIntegrationTest extends DefaultControllerTest {
    
    public static final String GROUP_NAME = "kt-156";
    
    @Container
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14");
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    private Group group;
    private Student firstStudent;
    private Student secondStudent;
    private Student student;
    
    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    
    @BeforeTransaction
    void setUp() {
        group = GroupMother.complete().build();
        groupRepository.saveAndFlush(group);
        firstStudent = StudentMother.complete().build();
        secondStudent = StudentMother.complete().build();
        studentRepository.saveAndFlush(firstStudent);
        studentRepository.saveAndFlush(secondStudent);
        student = StudentMother.complete().group(group).build();
        studentRepository.saveAndFlush(student);
    }
    
    @AfterTransaction
    void tearDown() {
        groupRepository.deleteAll();
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void deassignGroup_ShouldAuthorizeCredentialsAndRedirect() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/deassign-group", group.getId())
                    .param("studentId", String.valueOf(student.getId()))
                    .with(csrf()))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void assignGroup_ShouldAuthorizeCredentialsAndRedirect() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/assign-group", group.getId())
                    .param("studentId", new StringBuilder().append(firstStudent.getId())
                                                            .append(",")
                                                            .append(secondStudent.getId())
                                                            .toString())
                    .with(csrf()))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void delete_ShouldAuthenticateCredantialsAndRedirect() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/delete", group.getId()).with(csrf()))
               .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void create_ShouldAuthenticateCredantialsAndRedirect() throws Exception {
        mockMvc.perform(post("/groups/create").param("name", GROUP_NAME)
                                              .with(csrf()))
               .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void update() throws Exception {
        mockMvc.perform(post("/groups/{groupId}/update", group.getId())
                    .param("name", GROUP_NAME)
                    .with(csrf()))
        .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
        .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getById_ShouldAuthoriseCredentialsAndReturnStatusIsOk() throws Exception {
        mockMvc.perform(get("/groups/{groupId}", group.getId()))
               .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
               .andExpect(status().isOk());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getAllGroups_ShouldAuthenticateCredentialsAndReternStatusIsOk() throws Exception {
        mockMvc.perform(get("/groups/list"))
               .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
               .andExpect(status().isOk());
    }
}
