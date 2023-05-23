package ua.com.foxminded.university.controller;

import static ua.com.foxminded.university.controller.LessonController.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.com.foxminded.university.dto.GroupDTO;
import ua.com.foxminded.university.dto.LessonDTO;
import ua.com.foxminded.university.entity.Authority;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entitymother.LessonMother;
import ua.com.foxminded.university.modelmother.GroupDtoMother;
import ua.com.foxminded.university.modelmother.LessonDtoMother;

@SpringBootTest
@ActiveProfiles("prod")
@AutoConfigureMockMvc
@Testcontainers
class LessonControllerIntegrationTest extends DefaultControllerTest {
    
    @Container
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14");
    
    private LocalDate localDate;
    private Lesson lesson;
    private LessonDTO lessonDto;
    private GroupDTO groupDto;
    
    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    
    @BeforeEach
    void setUp() {
        localDate = LocalDate.now();
        lesson = LessonMother.complete().build();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(lesson);
        entityManager.getTransaction().commit();
        entityManager.close();
        
        lessonDto = LessonDtoMother.complete()
                                   .id(lesson.getId()).build();
        
        groupDto = GroupDtoMother.complete().build();
    }
    
    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void create_ShouldAuthorizeCredentialsAndRedirect() throws Exception {
        
        mockMvc.perform(post("/timetables/create/timetable/{date}", localDate.toString())
                    .flashAttr(LESSON_MODEL_ATTRIBUTE, lessonDto)
                    .with(csrf()))
               .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void delete_ShouldAuthanticateCredentialsAndRedirect() throws Exception {
        lessonDto.setDatestamp(localDate);
        mockMvc.perform(post("/timetables/delete/{id}", lesson.getId())
                    .flashAttr(LESSON_MODEL_ATTRIBUTE, lessonDto)
                    .with(csrf()))
               .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void update_ShouldAuthorizeCredentialsAndRedirect() throws Exception {
        mockMvc.perform(post("/timetables/update/{id}", lessonDto.getId())
                    .flashAttr(LESSON_MODEL_ATTRIBUTE, lessonDto)
                    .with(csrf()))
               .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void getDayTimetable_ShouldAuthorizeCredentialsAndRenderTemplate() throws Exception {
        mockMvc.perform(get("/timetables/day-timetables/{date}", localDate.toString()))
               .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
               .andExpect(status().isOk());
    }
    
    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void back_ShouldAuthorizeCredentialsAndRedirect() throws Exception {
        mockMvc.perform(get("/timetables/{date}/back", localDate.toString()))
               .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void next_ShouldAuthorizeCredentialsAndRedirect() throws Exception {
        mockMvc.perform(get("/timetables/{date}/next", localDate.toString()))
               .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
               .andExpect(status().is3xxRedirection());
    }
    
    @Test
    @WithUserDetails(AUTHORIZED_EMAIL)
    void list_ShouldAuthorizeCredentialsAndReturnStatusIsOk() throws Exception {
        mockMvc.perform(get("/timetables/{date}/list", localDate.toString()))
               .andExpect(authenticated().withRoles(Authority.ADMIN.toString()))
               .andExpect(status().isOk());
    }
}
