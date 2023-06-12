package ua.com.foxminded.university.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.com.foxminded.university.controller.CourseController.COURSE_ATTRIBUTE;
import static ua.com.foxminded.university.controller.CourseController.UPDATED_COURSE_ATTRIBUTE;
import static ua.com.foxminded.university.entity.Authority.ADMIN;
import static ua.com.foxminded.university.entity.Authority.TEACHER;
import static ua.com.foxminded.university.entity.RoleAuthority.ROLE_ADMIN;
import static ua.com.foxminded.university.entity.RoleAuthority.ROLE_TEACHER;

import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
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

import ua.com.foxminded.university.dto.CourseDTO;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Teacher;
import ua.com.foxminded.university.entity.UserAuthority;
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.entitymother.CourseMother;
import ua.com.foxminded.university.entitymother.UserMother;
import ua.com.foxminded.university.modelmother.CourseDTOMother;
import ua.com.foxminded.university.repository.UserAuthorityRepository;
import ua.com.foxminded.university.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("prod")
@Transactional
@DirtiesContext
class CourseControllerIntegrationTest {
    
    public static final int COURSE_ID = 1;
    public static final int TEACHER_ID = 1;
    public static final String ADMIN_EMAIL = "admin@email";
    public static final String TEACHER_EMAIL = "teacher@email";

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14");
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserAuthorityRepository userAuthorityRepository;
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    private Course course;
    private User adminUser;
    private User teacherUser;
    private CourseDTO courseDto;
    private Teacher teacher;
    
    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    
    @BeforeTransaction
    void init() {
        User admin = UserMother.complete().email(ADMIN_EMAIL).build();
        adminUser = userRepository.saveAndFlush(admin);
        UserAuthority adminUserAuthority = UserAuthority.builder()
                                                        .roleAuthority(ROLE_ADMIN)
                                                        .user(adminUser).build();
        userAuthorityRepository.saveAndFlush(adminUserAuthority);
        
        User teacher = UserMother.complete().email("teacher@email").build();
        teacherUser = userRepository.saveAndFlush(teacher);
        UserAuthority teacherUserAuthority = UserAuthority.builder()
                                                          .roleAuthority(ROLE_TEACHER)
                                                          .user(teacherUser).build();
        userAuthorityRepository.saveAndFlush(teacherUserAuthority);
    }
    
    @AfterTransaction
    void cleanUp() {
        userRepository.delete(adminUser);
        userRepository.delete(teacherUser);
    }
    
    @BeforeEach
    void setUp() {
        course = CourseMother.complete().build();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(course);
        teacher = Teacher.builder().user(teacherUser)
                                   .courses(new HashSet<>()).build();
        course.setTeachers(new HashSet<>());
        teacher.addCourse(course);
        entityManager.persist(teacher);
        entityManager.getTransaction().commit();
        entityManager.close();
        
        courseDto = CourseDTOMother.complete().build();
    }
    
    @Test
    @WithUserDetails(TEACHER_EMAIL)
    void getByTeacherId_ShouldAuthenticateCredentialsAndReturnStatusIsOk() throws Exception {
        mockMvc.perform(get("/courses/list/{teacherEmail}", teacher.getUser().getEmail()))
               .andDo(print())
               .andExpect(authenticated().withRoles(TEACHER.toString()))
               .andExpect(status().is2xxSuccessful());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void deassignTeacherToCourse_ShouldAuthenticateCredentialsAndRedirect() throws Exception {
        mockMvc.perform(post("/courses/{courseId}/deassign-teacher", COURSE_ID)
                    .param("teacherId", String.valueOf(TEACHER_ID))
                    .with(csrf()))
        .andDo(print())
        .andExpect(authenticated().withRoles(ADMIN.toString()))
        .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void assignTeacherToCourse_ShouldAuthenticateCredentialsAndRedirect() throws Exception {
        mockMvc.perform(post("/courses/{courseId}/assign-teacher", COURSE_ID)
                    .param("teacherId", String.valueOf(TEACHER_ID))
                    .with(csrf()))
               .andDo(print())
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getById_ShouldAuthenticateCredentialsAndReturnStatusIsOk() throws Exception {
        mockMvc.perform(get("/courses/{courseId}", course.getId()))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().isOk());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void deleteById_ShouldAuthenticateCredentialsAndRedirect() throws Exception {
        String courseId = course.getId().toString();
        
        mockMvc.perform(post("/courses/delete").param("courseId", courseId)
                                               .with(csrf()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void update_ShouldAuthenticateCredentialsAndRedirect() throws Exception {
        mockMvc.perform(post("/courses/update").param("courseId", course.getId().toString())
                                               .flashAttr(UPDATED_COURSE_ATTRIBUTE, courseDto)
                                               .with(csrf()))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void create_ShouldAuthenticateCredentialsAndReternStatusIsOk() throws Exception {
        mockMvc.perform(post("/courses/create").flashAttr(COURSE_ATTRIBUTE, courseDto)
                                               .with(csrf()))
        .andExpect(authenticated().withRoles(ADMIN.toString()))
        .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getAll_ShouldAuthenticateCredentialsAndReturnStatusIsOk() throws Exception {
        mockMvc.perform(get("/courses/list"))
               .andExpect(authenticated().withRoles(ADMIN.toString()))
               .andExpect(status().isOk());
    }
}
