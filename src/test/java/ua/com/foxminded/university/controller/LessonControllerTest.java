package ua.com.foxminded.university.controller;

import static ua.com.foxminded.university.controller.LessonController.*;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.dto.LessonDTO;
import ua.com.foxminded.university.modelmother.LessonDtoMother;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.TimetableService;
import ua.com.foxminded.university.service.TimingService;

@ExtendWith(SpringExtension.class)
class LessonControllerTest {
    
    public static final int TIMETABLE_ID = 1;
    
    @MockBean
    private LessonService timetableServiceMock;
    
    @MockBean
    private CourseService courseServiceMock;
    
    @MockBean
    private GroupService groupServiceMock;
    
    @MockBean
    private TimetableService timetableService;
    
    @MockBean
    private TimingService timingService;
    
    
    private MockMvc mockMvc;
    private LessonDTO lesson;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new LessonController(timetableServiceMock, 
                                     courseServiceMock, 
                                     groupServiceMock, 
                                     timetableService, 
                                     timingService)).build();

        lesson = LessonDtoMother.complete().build();
    }
    
    @Test
    void create_ShouldRedirectToGetDayTimetable() throws Exception {
        LocalDate localDate = LocalDate.now();
        LessonDTO lessonDto = LessonDtoMother.complete().build();
        mockMvc.perform(post("/timetables/create/timetable/{date}", localDate.toString())
                    .flashAttr(LESSON_MODEL_ATTRIBUTE, lessonDto))
               .andDo(print())
               .andExpect(model().attributeExists(LESSON_MODEL_ATTRIBUTE))
               .andExpect(redirectedUrl(new StringBuffer().append(DAY_LESSONS_PATH)
                                                          .append(localDate)
                                                          .append("?").toString()));
        verify(timetableServiceMock).create(isA(LessonDTO.class));
    }
    
    @Test
    void delete_ShouldRedirectToList() throws Exception {
        mockMvc.perform(post("/timetables/delete/{id}", TIMETABLE_ID)
                    .flashAttr(LESSON_MODEL_ATTRIBUTE, lesson))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(DAY_LESSONS_PATH)
                                                           .append(lesson.getDatestamp())
                                                           .append("?").toString()));
        verify(timetableServiceMock).deleteById(isA(Integer.class));
    }
    
    @Test
    void update_ShouldRedirectToGetDayTimetable() throws Exception {
        mockMvc.perform(post("/timetables/update/{id}", TIMETABLE_ID)
                    .flashAttr("timetableModel", lesson))
               .andDo(print())
               .andExpect(redirectedUrl(
                       new StringBuilder().append(DAY_LESSONS_PATH)
                                          .append(lesson.getDatestamp())
                                          .append("?").toString()));
        
        verify(timetableServiceMock).update(isA(LessonDTO.class));
    }
    
    @Test
    void getDayTimetable_ShouldRenderDayTimetableTemplate() throws Exception {
        mockMvc.perform(get("/timetables/day-timetables/{datestamp}", 
                            lesson.getDatestamp().toString()))
               .andDo(print())
               .andExpect(model().attributeExists(GROUPS_ATTRIBUTE, 
                                                  COURSES_ATTRIBUTE, 
                                                  DAY_LESSONS_ATTRIBUTE,
                                                  LESSON_MODEL_ATTRIBUTE))
               .andExpect(view().name(DAY_LESSONS_TEMPLATE));
        
        verify(timetableServiceMock).getDayLessons(isA(LocalDate.class));
        verify(courseServiceMock).getAll();
        verify(groupServiceMock).getAll();
    }
    
    @Test
    void back_ShouldRendirectToList() throws Exception {
        LocalDate localDate = LocalDate.now();
        when(timetableServiceMock.moveBack(isA(LocalDate.class))).thenReturn(localDate);
        mockMvc.perform(get("/timetables/{date}/back", localDate.toString()))
               .andDo(print())
               .andExpect(redirectedUrlPattern("/timetables/*/list?"));
    }
    
    @Test
    void next_ShouldRedirectToList() throws Exception {
        LocalDate localDate = LocalDate.now();
        when(timetableServiceMock.moveForward(isA(LocalDate.class))).thenReturn(localDate);
        mockMvc.perform(get("/timetables/{date}/next", localDate.toString()))
               .andDo(print())
               .andExpect(redirectedUrlPattern("/timetables/*/list?"));
    }

    @Test
    void list_ShouldRenderListTemplate() throws Exception {
        LocalDate localDate = LocalDate.now();
        mockMvc.perform(MockMvcRequestBuilders.get("/timetables/{date}/list", 
                    localDate.toString()))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists(GROUPS_ATTRIBUTE))
               .andExpect(model().attributeExists(COURSES_ATTRIBUTE))
               .andExpect(model().attributeExists(MONTH_LESSONS_ATTRIBUTE))
               .andExpect(model().attributeExists(LESSON_MODEL_ATTRIBUTE))
               .andExpect(MockMvcResultMatchers.view().name(DAY_LESSONS_TEMPLATE));
        
        verify(timetableServiceMock).getMonthLessons(LocalDate.now());
        verify(courseServiceMock).getAll();
        verify(groupServiceMock).getAll();
    }
}
