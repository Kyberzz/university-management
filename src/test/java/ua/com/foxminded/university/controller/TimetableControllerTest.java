package ua.com.foxminded.university.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ua.com.foxminded.university.controller.TimetableController.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.dto.TimetableDTO;
import ua.com.foxminded.university.dto.TimingDTO;
import ua.com.foxminded.university.dtomother.TimingDTOMother;
import ua.com.foxminded.university.service.TimetableService;
import ua.com.foxminded.university.service.TimingService;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class TimetableControllerTest {
    
    public static final int TIMIMING_ID = 1;
    public static final int TIMETABLE_ID = 1;
    public static final String TIMETABLE_NAME = "general";
    
    @Mock
    private TimetableService timetableServiceMock;
    
    @Mock
    private TimingService timingServiceMock;
    
    private TimetableDTO timetable;
    private List<TimetableDTO> timetables;
    private TimingDTO timing;
    
    private MockMvc mockMvc;
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TimetableController(
                timetableServiceMock, timingServiceMock)).build();
        timetable = TimetableDTO.builder().name(TIMETABLE_NAME).build();
        timetables = Arrays.asList(timetable);
        timing = TimingDTOMother.complete().build();
    }
    
    @Test
    void deleteTiming_ShouldRedirectToGetAll() throws Exception {
        mockMvc.perform(post("/timetables/delete-timing/{timetableId}/{timingId}", 
                        TIMETABLE_ID, TIMIMING_ID))
               .andDo(print())
               .andExpect(redirectedUrl(
                       new StringBuilder().append(SLASH)
                                          .append(TIMETABLES_LIST_TEMPLATE_PATH)
                                          .append(QUESTION_MARK)
                                          .append(TIMETABLE_ID_PARAMETER_NAME)
                                          .append(EQUAL_SIGN)
                                          .append(TIMETABLE_ID)
                                          .toString()));
        
        verify(timingServiceMock).deleteById(anyInt());
    }
    
    @Test
    void delete_ShouldRedirectToGetAll() throws Exception {
        mockMvc.perform(post("/timetables/delete/{timetableId}", TIMETABLE_ID))
               .andDo(print())
               .andExpect(redirectedUrl(
                       new StringBuilder().append(SLASH)
                                          .append(TIMETABLES_LIST_TEMPLATE_PATH)
                                          .append(QUESTION_MARK)
                                          .append(TIMETABLE_ID_PARAMETER_NAME)
                                          .append(EQUAL_SIGN)
                                          .append(STUB)
                                          .toString()));
        
        verify(timetableServiceMock).deleteById(anyInt());
    }
    
    @Test
    void addTiming_ShouldRedirectToGetAll() throws Exception {
        mockMvc.perform(post("/timetables/add-timing/{timetableId}", TIMETABLE_ID)
                    .flashAttr(TIMING_ATTRIBUTE, timing))
               .andDo(print())
               .andExpect(redirectedUrl(
                       new StringBuilder().append(SLASH)
                                          .append(TIMETABLES_LIST_TEMPLATE_PATH)
                                          .append(QUESTION_MARK)
                                          .append(TIMETABLE_ID_PARAMETER_NAME)
                                          .append(EQUAL_SIGN)
                                          .append(TIMETABLE_ID)
                                          .toString()));
        
        verify(timingServiceMock).create(isA(TimingDTO.class));
    }
    
    @Test
    void updateName_ShouldRedirectToGetAll() throws Exception {
        when(timetableServiceMock.getById(anyInt())).thenReturn(timetable);
        
        mockMvc.perform(post("/timetables/update-name/{timetableId}", TIMETABLE_ID)
                    .flashAttr(TIMETABLE_ATTRIBUTE, timetable))
               .andDo(print())
               .andExpect(redirectedUrl(
                       new StringBuilder().append(SLASH)
                                          .append(TIMETABLES_LIST_TEMPLATE_PATH)
                                          .append(QUESTION_MARK)
                                          .append(TIMETABLE_ID_PARAMETER_NAME)
                                          .append(EQUAL_SIGN)
                                          .append(TIMETABLE_ID)
                                          .toString()));
        verify(timetableServiceMock).getById(anyInt());
        verify(timetableServiceMock).update(isA(TimetableDTO.class));
    }
    
    @Test
    void create_SouldRedirectToGetAll() throws Exception {
        timetable.setId(TIMETABLE_ID);
        when(timetableServiceMock.create(timetable)).thenReturn(timetable);
        
        mockMvc.perform(post("/timetables/create")
                    .flashAttr(TIMETABLE_ATTRIBUTE, timetable))
               .andDo(print())
               .andExpect(redirectedUrl(
                       new StringBuilder().append(SLASH)
                                          .append(TIMETABLES_LIST_TEMPLATE_PATH)
                                          .append(QUESTION_MARK)
                                          .append(TIMETABLE_ID_PARAMETER_NAME)
                                          .append(EQUAL_SIGN)
                                          .append(TIMETABLE_ID)
                                          .toString()));
        
        verify(timetableServiceMock).create(isA(TimetableDTO.class));
    }
    
    @Test
    void getAll_ShouldRenderTimetablesListTemplate_TimetableIdIsNotNull() 
            throws Exception {
        when(timetableServiceMock.getAll()).thenReturn(timetables);
        when(timetableServiceMock.getByIdWithTimings(anyInt())).thenReturn(timetable);
        
        mockMvc.perform(get("/timetables/list")
                    .param("timetableId", String.valueOf(TIMETABLE_ID)))
               .andDo(print())
               .andExpect(model().attributeExists(TIMING_ATTRIBUTE, 
                                                  TIMETABLE_ATTRIBUTE, 
                                                  TIMETABLES_ATTRIBUTE))
               .andExpect(view().name(TIMETABLES_LIST_TEMPLATE_PATH));
        
        verify(timetableServiceMock).getAll();
        verify(timetableServiceMock).sortByName(ArgumentMatchers.<TimetableDTO>anyList());
        verify(timetableServiceMock).sortTimingsByStartTime(isA(TimetableDTO.class));
        verify(timetableServiceMock).getByIdWithTimings(anyInt());
    }
    
    @Test
    void getAll_ShouldRenderTimetablesListTemplate_WhenFirstIfBlockIsTrueAndSecondOneIsFalse() 
            throws Exception {
        when(timetableServiceMock.getAll()).thenReturn(timetables);
        
        mockMvc.perform(get("/timetables/list")
                    .param("timetableId", String.valueOf(STUB)))
               .andDo(print())
               .andExpect(model().attributeExists(TIMING_ATTRIBUTE, 
                                                  TIMETABLE_ATTRIBUTE, 
                                                  TIMETABLES_ATTRIBUTE))
               .andExpect(view().name(TIMETABLES_LIST_TEMPLATE_PATH));
        
        verify(timetableServiceMock).getAll();
        verify(timetableServiceMock).sortByName(ArgumentMatchers.<TimetableDTO>anyList());
        verify(timetableServiceMock).sortTimingsByStartTime(isA(TimetableDTO.class));
    }
    
    @Test
    void getAll_ShouldRenderTimetablesListTemplate_WhenFirstIfBlockIsTrueAndSecondOneIsTrue() 
            throws Exception {
        timetables = new ArrayList<>();
        when(timetableServiceMock.getAll()).thenReturn(timetables);
        
        mockMvc.perform(get("/timetables/list")
                    .param("timetableId", String.valueOf(STUB)))
               .andDo(print())
               .andExpect(model().attributeExists(TIMING_ATTRIBUTE, 
                                                  TIMETABLE_ATTRIBUTE, 
                                                  TIMETABLES_ATTRIBUTE))
               .andExpect(view().name(TIMETABLES_LIST_TEMPLATE_PATH));
        
        verify(timetableServiceMock).getAll();
        verify(timetableServiceMock).sortByName(ArgumentMatchers.<TimetableDTO>anyList());
        verify(timetableServiceMock).sortTimingsByStartTime(isA(TimetableDTO.class));
    }
}
