package ua.com.foxminded.university.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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
import static ua.com.foxminded.university.controller.CourseController.COURSE_ID_PARAMETER_NAME;
import static ua.com.foxminded.university.controller.CourseControllerIntegrationTest.COURSE_ID;
import static ua.com.foxminded.university.controller.DefaultController.AMPERSAND_SIGN;
import static ua.com.foxminded.university.controller.DefaultController.EQUAL_SIGN;
import static ua.com.foxminded.university.controller.DefaultController.QUESTION_MARK;
import static ua.com.foxminded.university.controller.DefaultController.SLASH;
import static ua.com.foxminded.university.controller.DefaultController.STUB;
import static ua.com.foxminded.university.controller.GroupController.GROUPS_ATTRIBUTE;
import static ua.com.foxminded.university.controller.GroupController.GROUP_ID;
import static ua.com.foxminded.university.controller.GroupController.GROUP_ID_PARAMETER_NAME;
import static ua.com.foxminded.university.controller.LessonController.*;
import static ua.com.foxminded.university.controller.StudentControllerTest.STUDENT_ID;
import static ua.com.foxminded.university.controller.TeacherController.TEACHER_ID;
import static ua.com.foxminded.university.controller.TimetableController.TIMETABLE_ID_PARAMETER_NAME;
import static ua.com.foxminded.university.controller.TimetableControllerTest.TIMETABLE_NAME;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.dto.GroupDTO;
import ua.com.foxminded.university.dto.LessonDTO;
import ua.com.foxminded.university.dto.StudentDTO;
import ua.com.foxminded.university.dto.TeacherDTO;
import ua.com.foxminded.university.dto.TimetableDTO;
import ua.com.foxminded.university.dto.UserDTO;
import ua.com.foxminded.university.dtomother.GroupDTOMother;
import ua.com.foxminded.university.dtomother.LessonDTOMother;
import ua.com.foxminded.university.dtomother.UserDTOMother;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.StudentService;
import ua.com.foxminded.university.service.TeacherService;
import ua.com.foxminded.university.service.TimetableService;
import ua.com.foxminded.university.service.TimingService;

@ExtendWith(SpringExtension.class)
class LessonControllerTest {
    
    public static final int LESSON_ID = 1;
    public static final int TIMETABLE_ID = 1;
    
    @MockBean
    private LessonService lessonServiceMock;
    
    @MockBean
    private CourseService courseServiceMock;
    
    @MockBean
    private GroupService groupServiceMock;
    
    @MockBean
    private TimetableService timetableServiceMock;
    
    @MockBean
    private TimingService timingServiceMock;
    
    @MockBean
    private TeacherService teacherServiceMock;
    
    @MockBean
    private StudentService studentServiceMock;
    
    private MockMvc mockMvc;
    private LessonDTO lesson;
    private UserDTO user;
    private TeacherDTO teacher;
    private StudentDTO student;
    private List<LessonDTO> lessons;
    private GroupDTO group;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new LessonController(lessonServiceMock, 
                                     courseServiceMock, 
                                     groupServiceMock, 
                                     timetableServiceMock, 
                                     teacherServiceMock, 
                                     studentServiceMock)).build();

        lesson = LessonDTOMother.complete().build();
        user = UserDTOMother.complete().build();
        teacher = new TeacherDTO();
        student = new StudentDTO();
        lessons = Arrays.asList(lesson);
        group = GroupDTOMother.complete().build();
    }
    
    @Test
    void edit_ShouldRedirectToGetDayLessons() throws Exception {
        lesson.setTimetable(TimetableDTO.builder().id(TIMETABLE_ID)
                                                  .name(TIMETABLE_NAME).build());
        when(lessonServiceMock.getById(anyInt())).thenReturn(lesson);
        
        mockMvc.perform(post("/lessons/{lessonId}/update", LESSON_ID)
                    .flashAttr(LESSON_ATTRIBUTE, lesson))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(DAY_LESSONS_TEMPLATE_PATH)
                                                           .append(SLASH)
                                                           .append(lesson.getDatestamp())
                                                           .append(QUESTION_MARK)
                                                           .append(TIMETABLE_ID_PARAMETER_NAME)
                                                           .append(EQUAL_SIGN)
                                                           .append(TIMETABLE_ID)
                                                           .append(AMPERSAND_SIGN)
                                                           .append(COURSE_ID_PARAMETER_NAME)
                                                           .append(EQUAL_SIGN)
                                                           .append(STUB)
                                                           .toString()));
        
        verify(lessonServiceMock).getById(anyInt());
        verify(lessonServiceMock).update(isA(LessonDTO.class));
    }
    
    @Test
    void getGroupScheduleForDate_ShouldRedirectToGetGroupWeekSchedule() throws Exception {
        mockMvc.perform(get("/lessons/group-week-schedule/{email}", user.getEmail())
                    .param(DATE_PARAMETER, lesson.getDatestamp().toString()))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(GROUP_WEEK_SCHEDULE_PATH)
                                                           .append(SLASH)
                                                           .append(lesson.getDatestamp())
                                                           .append(SLASH)
                                                           .append(user.getEmail())
                                                           .toString()));
    }
    
    @Test
    void getPreviousWeekGroupSchedule_ShouldRedirectToGetGroupWeekSchedule() throws Exception {
        when(lessonServiceMock.moveWeekBack(isA(LocalDate.class)))
            .thenReturn(lesson.getDatestamp());
        
        mockMvc.perform(get("/lessons/group-week-schedule/{date}/{email}/back", 
                            lesson.getDatestamp(), user.getEmail()))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(GROUP_WEEK_SCHEDULE_PATH)
                                                           .append(SLASH)
                                                           .append(lesson.getDatestamp())
                                                           .append(SLASH)
                                                           .append(user.getEmail())
                                                           .toString()));

        verify(lessonServiceMock).moveWeekBack(isA(LocalDate.class));
    }
    
    @Test
    void getNextWeekGroupSchedule_ShouldRedirectToGetGroupWeekSchedule() throws Exception {
        when(lessonServiceMock.moveWeekForward(isA(LocalDate.class)))
            .thenReturn(lesson.getDatestamp());
        
        mockMvc.perform(get("/lessons/group-week-schedule/{date}/{email}/next", 
                            lesson.getDatestamp(), user.getEmail()))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(GROUP_WEEK_SCHEDULE_PATH)
                                                           .append(SLASH)
                                                           .append(lesson.getDatestamp())
                                                           .append(SLASH)
                                                           .append(user.getEmail())
                                                           .toString()));

        verify(lessonServiceMock).moveWeekForward(isA(LocalDate.class));
    }
    
    @Test
    void getGroupWeekSchedule_ShouldRenderWeekScheduleTemplate() throws Exception {
        group.setId(GROUP_ID);
        student.setGroup(group);
        student.setId(STUDENT_ID);
        List<List<LessonDTO>> weekLessons = Arrays.asList(lessons);

        when(studentServiceMock.getByEmail(anyString())).thenReturn(student);
        when(lessonServiceMock.getWeekLessonsOwnedByGroup(isA(LocalDate.class), anyInt()))
            .thenReturn(weekLessons);
        
        mockMvc.perform(get("/lessons/group-week-schedule/{date}/{email}", 
                            lesson.getDatestamp(), user.getEmail()))
               .andDo(print())
               .andExpect(model().attributeExists(WEEK_LESSONS_ATTRIBUTE, 
                                                  LESSON_ATTRIBUTE))
               .andExpect(view().name(WEEK_SCHEDULE_TEMPLATE_PATH));
        
        verify(studentServiceMock).getByEmail(anyString());
        verify(lessonServiceMock).getWeekLessonsOwnedByGroup(isA(LocalDate.class), anyInt());
    }
    
    @Test
    void getTeacherScheduleForDate_ShouldRedirectToTeacherWeekSchedule() throws Exception {
        mockMvc.perform(get("/lessons/teacher-week-schedule/{email}", user.getEmail())
                    .param("date", lesson.getDatestamp().toString()))
               .andDo(print())
               .andExpect(redirectedUrl(
                       new StringBuilder().append(SLASH)
                                          .append(TEACHER_WEEK_SCHEDULE_PATH)
                                          .append(SLASH)
                                          .append(lesson.getDatestamp())
                                          .append(SLASH)
                                          .append(user.getEmail())                                          .toString()));
    }
    
    @Test
    void getPreviousWeekTeacherSchedule_ShouldRedirectToTecherWeekSchdule() throws Exception {
        when(lessonServiceMock.moveWeekBack(isA(LocalDate.class)))
            .thenReturn(lesson.getDatestamp());
        mockMvc.perform(get("/lessons/teacher-week-schedule/{date}/{email}/back", 
                        lesson.getDatestamp(), user.getEmail()))
               .andDo(print())
               .andExpect(redirectedUrl(
                       new StringBuilder().append(SLASH)
                                          .append(TEACHER_WEEK_SCHEDULE_PATH)
                                          .append(SLASH)
                                          .append(lesson.getDatestamp())
                                          .append(SLASH)
                                          .append(user.getEmail())
                                          .toString()));
        
        verify(lessonServiceMock).moveWeekBack(isA(LocalDate.class));
    }
    
    @Test
    void getNextWeekTeacherSchedule_ShouldRedirectToTeacherWeekSchedule() throws Exception {
        when(lessonServiceMock.moveWeekForward(isA(LocalDate.class)))
            .thenReturn(lesson.getDatestamp());
        mockMvc.perform(get("/lessons/teacher-week-schedule/{date}/{email}/next", 
                            lesson.getDatestamp(), user.getEmail()))
               .andDo(print())
               .andExpect(redirectedUrl(
                       new StringBuilder().append(SLASH)
                                          .append(TEACHER_WEEK_SCHEDULE_PATH)
                                          .append(SLASH)
                                          .append(lesson.getDatestamp())
                                          .append(SLASH)
                                          .append(user.getEmail())
                                          .toString()));
       
        verify(lessonServiceMock).moveWeekForward(isA(LocalDate.class));
    }
    
    @Test
    void getTeacherWeekSchedule_ShouldRenderWeekScheduleTemplate() throws Exception {
        teacher.setId(TEACHER_ID);
        when(teacherServiceMock.getTeacherByEmail(anyString())).thenReturn(teacher);
        
        mockMvc.perform(get("/lessons/teacher-week-schedule/{date}/{email}", 
                            lesson.getDatestamp(), user.getEmail() ))
               .andDo(print())
               .andExpect(model().attributeExists(WEEK_LESSONS_ATTRIBUTE, LESSON_ATTRIBUTE))
               .andExpect(view().name(WEEK_SCHEDULE_TEMPLATE_PATH));
        
        verify(teacherServiceMock).getTeacherByEmail(anyString());
        verify(lessonServiceMock).getWeekLessonsOwnedByTeacher(isA(LocalDate.class), anyInt());
    }
    
    @Test
    void applyTimetable_ShouldRedirectToGetDayLessons() throws Exception {
        mockMvc.perform(post("/lessons/{date}/apply-timetable", lesson.getDatestamp())
                    .param(TIMETABLE_ID_PARAMETER_NAME, String.valueOf(TIMETABLE_ID)))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(DAY_LESSONS_TEMPLATE_PATH)
                                                           .append(SLASH)
                                                           .append(lesson.getDatestamp())
                                                           .append(QUESTION_MARK)
                                                           .append(TIMETABLE_ID_PARAMETER_NAME)
                                                           .append(EQUAL_SIGN)
                                                           .append(TIMETABLE_ID)
                                                           .append(AMPERSAND_SIGN)
                                                           .append(COURSE_ID_PARAMETER_NAME)
                                                           .append(EQUAL_SIGN)
                                                           .append(STUB)
                                                           .toString()));
        
        verify(lessonServiceMock).applyTimetable(isA(LocalDate.class), anyInt());
    }
    
    @Test
    void create_ShouldRedirectToGetDayTimetable() throws Exception {
        TimetableDTO timetableDto = TimetableDTO.builder().id(TIMETABLE_ID).build();
        LessonDTO lessonDto = LessonDTOMother.complete().build();
        lessonDto.setTimetable(timetableDto);
        
        mockMvc.perform(post("/lessons/{date}/create", lesson.getDatestamp().toString())
                    .param(TIMETABLE_ID_PARAMETER_NAME, String.valueOf(TIMETABLE_ID))    
                    .param(COURSE_ID_PARAMETER_NAME, String.valueOf(COURSE_ID))
                    .param(GROUP_ID_PARAMETER_NAME, String.valueOf(GROUP_ID))
                    .flashAttr(LESSON_ATTRIBUTE, lessonDto))
               .andDo(print())
               .andExpect(model().attributeExists(LESSON_ATTRIBUTE))
               .andExpect(redirectedUrl(new StringBuffer().append(SLASH)
                                                          .append(DAY_LESSONS_TEMPLATE_PATH)
                                                          .append(SLASH)                                                          .append(lessonDto.getDatestamp())
                                                          .append(QUESTION_MARK)
                                                          .append(TIMETABLE_ID_PARAMETER_NAME)
                                                          .append(EQUAL_SIGN)
                                                          .append(lessonDto.getTimetable()
                                                                           .getId())
                                                          .append(AMPERSAND_SIGN)
                                                          .append(COURSE_ID_PARAMETER_NAME)
                                                          .append(EQUAL_SIGN)
                                                          .append(COURSE_ID)
                                                          .toString()));
        verify(lessonServiceMock).create(isA(LessonDTO.class));
    }
    
    @Test
    void deleteById_ShouldRedirectToListTemplate() throws Exception {
        TimetableDTO timetable = TimetableDTO.builder().id(TIMETABLE_ID).build();
        lesson.setTimetable(timetable);
        
        mockMvc.perform(post("/lessons/delete/{lessonId}", TIMETABLE_ID)
               .flashAttr(LESSON_ATTRIBUTE, lesson))
        .andDo(print())
        .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                    .append(DAY_LESSONS_TEMPLATE_PATH)
                                                    .append(SLASH)
                                                    .append(lesson.getDatestamp())
                                                    .append(QUESTION_MARK)
                                                    .append(TIMETABLE_ID_PARAMETER_NAME)
                                                    .append(EQUAL_SIGN)
                                                    .append(TIMETABLE_ID)
                                                    .append(AMPERSAND_SIGN)
                                                    .append(COURSE_ID_PARAMETER_NAME)
                                                    .append(EQUAL_SIGN)
                                                    .append(STUB)
                                                    .toString()));
            
        verify(lessonServiceMock).deleteById(isA(Integer.class));
    }

    @Test
    void getDayLessons_ShouldRenderDayTimetableTemplate_WhenTimetableIdAndCourseIdAreStubs() 
            throws Exception {
        List<LessonDTO> lessons = Arrays.asList(lesson);
        when(lessonServiceMock.getDayLessons(isA(LocalDate.class))).thenReturn(lessons);

        mockMvc.perform(get("/lessons/day-lessons/{date}", 
                            lesson.getDatestamp().toString())
                    .param(TIMETABLE_ID_PARAMETER_NAME, String.valueOf(STUB))
                    .param(COURSE_ID_PARAMETER_NAME, String.valueOf(STUB)))
               .andDo(print())
               .andExpect(model().attributeExists(GROUPS_ATTRIBUTE, 
                                                  COURSES_ATTRIBUTE, 
                                                  DAY_LESSONS_ATTRIBUTE,
                                                  LESSON_ATTRIBUTE))
               .andExpect(view().name(DAY_LESSONS_TEMPLATE_PATH));
        
        verify(lessonServiceMock).getDayLessons(isA(LocalDate.class));
        verify(lessonServiceMock).addLessonTiming(ArgumentMatchers.<LessonDTO>anyList());
        verify(lessonServiceMock).sortByLessonOrder(ArgumentMatchers.<LessonDTO>anyList());
        verify(courseServiceMock).getAll();
        verify(groupServiceMock).getAll();
        verify(timetableServiceMock).getAll();
    }
    
    @Test
    void getDayLessons_ShouldRenderDayTimetableTemplate_WhenTimetableIdAndCourseIdAreNotStubs() 
            throws Exception {
        List<LessonDTO> lessons = Arrays.asList(lesson);
        when(lessonServiceMock.getDayLessons(isA(LocalDate.class))).thenReturn(lessons);

        mockMvc.perform(get("/lessons/day-lessons/{date}", 
                            lesson.getDatestamp().toString())
                    .param(TIMETABLE_ID_PARAMETER_NAME, String.valueOf(TIMETABLE_ID))
                    .param(COURSE_ID_PARAMETER_NAME, String.valueOf(COURSE_ID)))
               .andDo(print())
               .andExpect(model().attributeExists(GROUPS_ATTRIBUTE, 
                                                  COURSES_ATTRIBUTE, 
                                                  DAY_LESSONS_ATTRIBUTE,
                                                  LESSON_ATTRIBUTE))
               .andExpect(view().name(DAY_LESSONS_TEMPLATE_PATH));
        
        verify(lessonServiceMock).getDayLessons(isA(LocalDate.class));
        verify(lessonServiceMock).addLessonTiming(ArgumentMatchers.<LessonDTO>anyList());
        verify(lessonServiceMock).sortByLessonOrder(ArgumentMatchers.<LessonDTO>anyList());
        verify(courseServiceMock).getAll();
        verify(groupServiceMock).getAll();
        verify(timetableServiceMock).getAll();
        verify(timetableServiceMock).getByIdWithTimings(anyInt());
        verify(teacherServiceMock).getByCoursesId(anyInt());
        verify(courseServiceMock).getById(anyInt());
    }
    
    @Test
    void back_ShouldRendirectToList() throws Exception {
        LocalDate localDate = LocalDate.now();
        when(lessonServiceMock.moveMonthBack(isA(LocalDate.class))).thenReturn(localDate);
        mockMvc.perform(get("/lessons/{date}/back", localDate.toString()))
               .andDo(print())
               .andExpect(redirectedUrlPattern("/lessons/month-lessons/*"));
        
        verify(lessonServiceMock).moveMonthBack(isA(LocalDate.class));
    }
    
    @Test
    void next_ShouldRedirectToList() throws Exception {
        LocalDate localDate = LocalDate.now();
        when(lessonServiceMock.moveMonthForward(isA(LocalDate.class))).thenReturn(localDate);
        mockMvc.perform(get("/lessons/{date}/next", localDate.toString()))
               .andDo(print())
               .andExpect(redirectedUrlPattern("/lessons/month-lessons/*"));
        
        verify(lessonServiceMock).moveMonthForward(isA(LocalDate.class));
    }

    @Test
    void getMonthLessons_ShouldRenderListTemplate() throws Exception {
        LocalDate localDate = lesson.getDatestamp();
        mockMvc.perform(MockMvcRequestBuilders.get("/lessons/month-lessons/{date}", 
                        localDate.toString()))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists(GROUPS_ATTRIBUTE))
               .andExpect(model().attributeExists(COURSES_ATTRIBUTE))
               .andExpect(model().attributeExists(MONTH_LESSONS_ATTRIBUTE))
               .andExpect(model().attributeExists(LESSON_ATTRIBUTE))
               .andExpect(MockMvcResultMatchers.view().name(MONTH_SHEDULE_TEMPLATE_PATH));
        
        verify(lessonServiceMock).getMonthLessons(isA(LocalDate.class));
        verify(courseServiceMock).getAll();
        verify(groupServiceMock).getAll();
    }
}
