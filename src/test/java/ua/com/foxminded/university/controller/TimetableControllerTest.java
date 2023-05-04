package ua.com.foxminded.university.controller;

import static ua.com.foxminded.university.controller.TimetableController.*;

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

import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.modelmother.TimetableModelMother;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.TimetableService;

@ExtendWith(SpringExtension.class)
class TimetableControllerTest {
    
    public static final int TIMETABLE_ID = 1;
    
    @MockBean
    private TimetableService timetableServiceMock;
    
    @MockBean
    private CourseService courseServiceMock;
    
    @MockBean
    private GroupService groupServiceMock;
    
    private MockMvc mockMvc;
    private TimetableModel timetableModel;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new TimetableController(timetableServiceMock, 
                                        courseServiceMock, 
                                        groupServiceMock)).build();

        timetableModel = TimetableModelMother.complete().build();
    }
    
    @Test
    void create_ShouldRedirectToGetDayTimetable() throws Exception {
        LocalDate localDate = LocalDate.now();
        TimetableModel timetableModel = TimetableModelMother.complete().build();
        mockMvc.perform(post("/timetables/create/timetable/{date}", localDate.toString())
                    .flashAttr(TIMETABLE_MODEL_ATTRIBUTE, timetableModel))
               .andDo(print())
               .andExpect(model().attributeExists(TIMETABLE_MODEL_ATTRIBUTE))
               .andExpect(redirectedUrl(new StringBuffer().append(DAY_TIMETABLES_PATH)
                                                          .append(localDate)
                                                          .append("?").toString()));
        verify(timetableServiceMock).create(isA(TimetableModel.class));
    }
    
    @Test
    void delete_ShouldRedirectToList() throws Exception {
        mockMvc.perform(post("/timetables/delete/{id}", TIMETABLE_ID)
                    .flashAttr(TIMETABLE_MODEL_ATTRIBUTE, timetableModel))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(DAY_TIMETABLES_PATH)
                                                           .append(timetableModel.getDatestamp())
                                                           .append("?").toString()));
        verify(timetableServiceMock).deleteById(isA(Integer.class));
    }
    
    @Test
    void update_ShouldRedirectToGetDayTimetable() throws Exception {
        mockMvc.perform(post("/timetables/update/{id}", TIMETABLE_ID)
                    .flashAttr("timetableModel", timetableModel))
               .andDo(print())
               .andExpect(redirectedUrl(
                       new StringBuilder().append(DAY_TIMETABLES_PATH)
                                          .append(timetableModel.getDatestamp())
                                          .append("?").toString()));
        
        verify(timetableServiceMock).update(isA(TimetableModel.class));
    }
    
    @Test
    void getDayTimetable_ShouldRenderDayTimetableTemplate() throws Exception {
        mockMvc.perform(get("/timetables/day-timetables/{datestamp}", 
                            timetableModel.getDatestamp().toString()))
               .andDo(print())
               .andExpect(model().attributeExists(GROUPS_ATTRIBUTE, 
                                                  COURSES_ATTRIBUTE, 
                                                  DAY_TIMETABLE_ATTRIBUTE,
                                                  TIMETABLE_MODEL_ATTRIBUTE))
               .andExpect(view().name(DAY_TIMETABLE_TEMPLATE));
        
        verify(timetableServiceMock).getDayTimetalbe(isA(LocalDate.class));
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
               .andExpect(model().attributeExists(MONTH_TIMETABLE_ATTRIBUTE))
               .andExpect(model().attributeExists(TIMETABLE_MODEL_ATTRIBUTE))
               .andExpect(MockMvcResultMatchers.view().name(TIMETABLES_LIST_TEMPLATE));
        
        verify(timetableServiceMock).getMonthTimetable(LocalDate.now());
        verify(courseServiceMock).getAll();
        verify(groupServiceMock).getAll();
    }
}