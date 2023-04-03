package ua.com.foxminded.university.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.RoleAuthority;
import ua.com.foxminded.university.entity.UserAuthorityEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.entitymother.CourseEntityMother;
import ua.com.foxminded.university.entitymother.UserEntityMother;
import ua.com.foxminded.university.model.Authority;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.modelmother.CourseModelMother;
import ua.com.foxminded.university.repository.UserAuthorityRepository;
import ua.com.foxminded.university.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("prod")
@Transactional
class CourseControllerIntegrationTest {
    
    public static final String AUTHORIZED_EMAIL = "authorized@email";
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserAuthorityRepository userAuthorityRepository;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14");
    
    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    private CourseEntity courseEntity;
    private UserEntity authorizedUser;
    private CourseModel courseModel;
    
    @BeforeTransaction
    void init() {
        UserEntity user = UserEntityMother.complete().email(AUTHORIZED_EMAIL).build();
        authorizedUser = userRepository.saveAndFlush(user);
        UserAuthorityEntity userAuthority = UserAuthorityEntity.builder()
                .roleAuthority(RoleAuthority.ROLE_ADMIN)
                .user(authorizedUser)
                .build();
        userAuthorityRepository.saveAndFlush(userAuthority);
    }
    
    @AfterTransaction
    void cleanUp() {
        userRepository.deleteAll();
    }
    
    @BeforeEach
    void setUp() {
        courseEntity = CourseEntityMother.complete().build();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(courseEntity);
        entityManager.getTransaction().commit();
        entityManager.close();
        
        courseModel = CourseModelMother.complete().build();
    }
    
    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void get_ShouldAuthenticateCredentialsAndReturnStatusIsOk() throws Exception {
        mockMvc.perform(get("/courses/{id}", courseEntity.getId()))
               .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
               .andExpect(status().isOk());
    }
    
    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void delete_ShouldAuthenticateCredentialsAndRedirect() throws Exception {
        String courseId = courseEntity.getId().toString();
        
        mockMvc.perform(post("/courses/delete").param("courseId", courseId)
                                               .with(csrf()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void update_ShouldAuthenticateCredentialsAndRedirect() throws Exception {
        String courseId = String.valueOf(courseEntity.getId().toString());
        
        mockMvc.perform(post("/courses/update").param("courseId", courseId)
                                               .flashAttr("courseModel", courseModel)
                                               .with(csrf()))
               .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void create_ShouldAuthenticateCredentialsAndReternStatusIsOk() throws Exception {
        mockMvc.perform(post("/courses/create").flashAttr("courseModel", courseModel)
                .with(csrf()))
        .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
        .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void list_ShouldAuthenticateCredentialsAndReturnStatusIsOk() throws Exception {
        mockMvc.perform(get("/courses/list"))
               .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
               .andExpect(status().isOk());
    }
}
