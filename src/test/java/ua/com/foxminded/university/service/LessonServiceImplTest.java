package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.service.GroupServiceImplTest.GROUP_ID;
import static ua.com.foxminded.university.service.impl.LessonServiceImpl.LESSON_LIST_TYPE;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.annotation.DirtiesContext;

import ua.com.foxminded.university.dto.CourseDTO;
import ua.com.foxminded.university.dto.GroupDTO;
import ua.com.foxminded.university.dto.LessonDTO;
import ua.com.foxminded.university.dto.TeacherDTO;
import ua.com.foxminded.university.dto.TimetableDTO;
import ua.com.foxminded.university.dtomother.CourseDTOMother;
import ua.com.foxminded.university.dtomother.GroupDTOMother;
import ua.com.foxminded.university.dtomother.LessonDTOMother;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Timetable;
import ua.com.foxminded.university.entity.Timing;
import ua.com.foxminded.university.entitymother.GroupMother;
import ua.com.foxminded.university.entitymother.LessonMother;
import ua.com.foxminded.university.entitymother.TimetableMother;
import ua.com.foxminded.university.entitymother.TimingMother;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.TimetableRepository;
import ua.com.foxminded.university.repository.TimingRepository;
import ua.com.foxminded.university.service.impl.LessonServiceImpl;

@ExtendWith(MockitoExtension.class)
@DirtiesContext
class LessonServiceImplTest {
    
    public static final int COURSE_ID = 1;
    public static final int TEACHER_ID = 1;
    public static final int TIMETABLE_ID = 1;
    public static final int LESSON_ID = 1;
    
    @InjectMocks
    private LessonServiceImpl lessonSerivice;
    
    @Mock
    private LessonRepository lessonRepositoryMock;
    
    @Mock
    private TimingRepository timingRepositoryMock;
    
    @Mock 
    private GroupRepository groupRepositoryMock;
    
    @Mock
    private TimetableRepository timetableRepositoryMock;
    
    @Mock
    private ModelMapper modelMapperMock;
    
    private Lesson lesson;
    private LessonDTO lessonDto;
    private List<Lesson> lessons;
    private List<LessonDTO> lessonsDto;
    private GroupDTO groupDto;
    private Timing timing;
    private TimetableDTO timetableDto;
    private Timetable timetable;
    private TeacherDTO teacherDto;
    private CourseDTO courseDto;
    private Group group;
    
    @BeforeEach
    void setUp() throws IOException {
        timetableDto = TimetableDTO.builder().id(TIMETABLE_ID).build();
        timetable = TimetableMother.complete().build();
        timing = TimingMother.complete().build();
        lesson = LessonMother.complete().build();
        lessonDto = LessonDTOMother.complete().timetable(timetableDto).build();
        lessons = Arrays.asList(lesson);
        lessonsDto = Arrays.asList(lessonDto);
        groupDto = GroupDTOMother.complete().build();
        teacherDto = new TeacherDTO();
        courseDto = CourseDTOMother.complete().build();
        group = GroupMother.complete().build();
    }
    
    @Test
    void getLessonsByTeacherId_ShouldReturnLessonsOwnedByTeacher() {
        when(lessonRepositoryMock.findByTeacherId(anyInt())).thenReturn(lessons);
        lessonSerivice.getLessonsByTeacherId(TEACHER_ID);
        verify(modelMapperMock).map(lessons, LESSON_LIST_TYPE);
    }
    
    @Test
    void sortByDatestamp_ShouldSortLessons() {
        LessonDTO lessonOne = LessonDTO.builder().datestamp(LocalDate.of(2023, 1, 1)).build();
        LessonDTO lessonTwo = LessonDTO.builder().datestamp(LocalDate.of(2023, 1, 2)).build();
        Set<LessonDTO> lessons = new HashSet<>();
        lessons.add(lessonTwo);
        lessons.add(lessonOne);
        Set<LessonDTO> sortedLesson = lessonSerivice.sortByDatestamp(lessons);
        assertEquals(lessonOne, sortedLesson.iterator().next());
    }
    
    @Test
    void moveWeekBack_ShouldMoveDatestampForOneWeekBack() {
        LocalDate date = lessonSerivice.moveWeekBack(lesson.getDatestamp());
        assertEquals(lesson.getDatestamp().minusWeeks(1), date);
    }
    
    @Test
    void moveWeekForward_ShouldMoveDatestampForOneWeekForward() {
        LocalDate date = lessonSerivice.moveWeekForward(lesson.getDatestamp());
        assertEquals(lesson.getDatestamp().plusWeeks(1), date);
    }
    
    @Test
    void getWeekLessonsOwnedByTeacher_ShouldReturnLessonsAndEmptyLessons() {
        
        when(lessonRepositoryMock.findByDatestampAndTeacherId(
                isA(LocalDate.class), anyInt())).thenReturn(lessons);
        when(lessonRepositoryMock.findByDatestampAndTeacherIdAndLessonOrder(
                isA(LocalDate.class), anyInt(), anyInt())).thenReturn(lesson);
        when(modelMapperMock.map(lesson, LessonDTO.class)).thenReturn(lessonDto);     
        
        lessonSerivice.getWeekLessonsOwnedByTeacher(lesson.getDatestamp(), TEACHER_ID);
        verify(lessonRepositoryMock, times(DayOfWeek.values().length))
            .findByDatestampAndTeacherId(isA(LocalDate.class), anyInt()); 
    }
    
    @Test
    void applyTimetable_ShouldSetTimetableForEachLesson() {
        List<Lesson> lessons = Arrays.asList(lesson);
        
        when(timetableRepositoryMock.findById(anyInt())).thenReturn(timetable);
        when(lessonRepositoryMock.findByDatestamp(isA(LocalDate.class))).thenReturn(lessons);
        when(lessonRepositoryMock.saveAllAndFlush(ArgumentMatchers.<Lesson>anyList()))
            .thenReturn(lessons);
       
        lessonSerivice.applyTimetable(lesson.getDatestamp(), TIMETABLE_ID);
        verify(modelMapperMock).map(lessons, LESSON_LIST_TYPE);
    }
    
    @Test
    void sortByLessonOrder_ShouldSortLessonsByLessonOrder() {
        LessonDTO lessonOne = LessonDTO.builder().lessonOrder(1).build();
        LessonDTO lessonTwo = LessonDTO.builder().lessonOrder(2).build();
        List<LessonDTO> list = Arrays.asList(lessonTwo, lessonOne);
        lessonSerivice.sortByLessonOrder(list);
        
        assertEquals(lessonOne, list.iterator().next());
    }
    
    @Test
    void addLessonTiming_ShouldAddTimingToEachLessonOfListAccordingToLessonOrderAndTimetableId() {
        List<Timing> timings = Arrays.asList(timing);
        when(timingRepositoryMock.findByTimetableId(anyInt())).thenReturn(timings);
        timetableDto.setId(TIMETABLE_ID);
        lessonDto.setTimetable(timetableDto);
        List<LessonDTO> lessons = Arrays.asList(lessonDto);
        lessonSerivice.addLessonTiming(lessons);
        
        verify(timingRepositoryMock).findByTimetableId(anyInt());
    }
    
    @Test
    void addLessonTiming_ShouldAddTimingAccodingToLessonOrderAndTimetableId() {
        List<Timing> timings = Arrays.asList(timing);
        when(timingRepositoryMock.findByTimetableId(anyInt())).thenReturn(timings);
        timetableDto.setId(TIMETABLE_ID);
        lessonDto.setTimetable(timetableDto);
        lessonSerivice.addLessonTiming(lessonDto);
        
        verify(timingRepositoryMock).findByTimetableId(anyInt());
    }
    
    @Test
    void moveMonthForward_ShouldMoveForwardDatestamp() {
        LocalDate localDate = LocalDate.now();
        LocalDate expectedResult = localDate.plusWeeks(
                LessonServiceImpl.WEEKS_OFFSET);
        assertEquals(expectedResult, lessonSerivice.moveMonthForward(localDate));
    }

    @Test
    void moveMonthBack_ShouldMoveBackDatestamp() {
        LocalDate localDate = LocalDate.now();
        LocalDate expectedResult = localDate.minusWeeks(
                LessonServiceImpl.WEEKS_OFFSET);
        assertEquals(expectedResult, lessonSerivice.moveMonthBack(localDate));
    }
    
    @Test
    void deleteById_ShouldExecuteCorrectCallsQuantity() {
        lessonSerivice.deleteById(anyInt());
        verify(lessonRepositoryMock).deleteById(isA(Integer.class));
    }
    
    @Test
    void update_ShouldExecuteCorrectCallsQuantity() {
        LessonDTO model = LessonDTO.builder().id(LESSON_ID).build();
        when(modelMapperMock.map(model, Lesson.class)).thenReturn(lesson);
        when(lessonRepositoryMock.findById(anyInt())).thenReturn(lesson);
        lessonSerivice.update(model);
        verify(lessonRepositoryMock).saveAndFlush(isA(Lesson.class));
    }
    
    @Test
    void create_ShouldCallComponents_WhenIfBlockFalse() {
        teacherDto.setId(TEACHER_ID);
        courseDto.setId(COURSE_ID);
        groupDto.setId(GROUP_ID);
        
        lessonDto.setId(LESSON_ID);
        lessonDto.setGroups(new HashSet<>());
        lessonDto.getGroups().add(groupDto);
        lessonDto.setTeacher(teacherDto);
        lessonDto.setCourse(courseDto);
        
        lesson.setId(LESSON_ID);
        
        when(lessonRepositoryMock.findByDatestampAndLessonOrderAndGroupsId(
                isA(LocalDate.class), anyInt(), anyInt()))
            .thenReturn(lesson);
        when(lessonRepositoryMock.findByDatestampAndTeacherIdAndLessonOrderAndCourseId(
                isA(LocalDate.class), anyInt(), anyInt(), anyInt())).thenReturn(lesson);
        when(modelMapperMock.map(lessonDto, Lesson.class)).thenReturn(lesson);
        when(modelMapperMock.map(lesson, LessonDTO.class)).thenReturn(lessonDto);
        lessonSerivice.create(lessonDto);
        verify(modelMapperMock).map(lesson, LessonDTO.class);
    }
    
    @Test
    void create_ShouldCallComponentsIntoIfBlock_WhenOnlyPersistedLessonIsNull() {
        teacherDto.setId(TEACHER_ID);
        courseDto.setId(COURSE_ID);
        groupDto.setId(GROUP_ID);
        
        lessonDto.setId(LESSON_ID);
        lessonDto.setGroups(new HashSet<>());
        lessonDto.getGroups().add(groupDto);
        lessonDto.setTeacher(teacherDto);
        lessonDto.setCourse(courseDto);
        
        group.setId(GROUP_ID);
        group.setLessons(new HashSet<>());
        
        lesson.setId(LESSON_ID);
        lesson.setGroups(new HashSet<>());
        lesson.getGroups().add(group);
        
        when(lessonRepositoryMock.findByDatestampAndLessonOrderAndGroupsId(
                isA(LocalDate.class), anyInt(), anyInt())).thenReturn(null);
        when(lessonRepositoryMock.findByDatestampAndTeacherIdAndLessonOrderAndCourseId(
                isA(LocalDate.class), anyInt(), anyInt(), anyInt())).thenReturn(lesson);
        
        when(modelMapperMock.map(lessonDto, Lesson.class)).thenReturn(lesson);
        when(groupRepositoryMock.findById(anyInt())).thenReturn(group);
        when(lessonRepositoryMock.saveAndFlush(lesson)).thenReturn(lesson);
        
        lessonSerivice.create(lessonDto);
        verify(modelMapperMock).map(lesson, LessonDTO.class);
    }
    
    @Test
    void create_ShouldCallComponentsIntoIfBlock_WhenPersistedAndCouterpartLessonsAreNull() {
        teacherDto.setId(TEACHER_ID);
        courseDto.setId(COURSE_ID);
        groupDto.setId(GROUP_ID);
        
        lessonDto.setId(LESSON_ID);
        lessonDto.setGroups(new HashSet<>());
        lessonDto.getGroups().add(groupDto);
        lessonDto.setTeacher(teacherDto);
        lessonDto.setCourse(courseDto);
        
        lesson.setId(LESSON_ID);
       
        when(lessonRepositoryMock.findByDatestampAndLessonOrderAndGroupsId(
                isA(LocalDate.class), anyInt(), anyInt())).thenReturn(null);
        when(lessonRepositoryMock.findByDatestampAndTeacherIdAndLessonOrderAndCourseId(
                isA(LocalDate.class), anyInt(), anyInt(), anyInt())).thenReturn(null);
        
        when(modelMapperMock.map(lessonDto, Lesson.class)).thenReturn(lesson);
        when(lessonRepositoryMock.saveAndFlush(isA(Lesson.class))).thenReturn(lesson);
        
        lessonSerivice.create(lessonDto);
        verify(modelMapperMock).map(lesson, LessonDTO.class);
    }
    
    @Test
    void getById_ShouldExecuteCorrectCallsQuantity() {
        when(lessonRepositoryMock.findById(anyInt())).thenReturn(lesson);
        lessonSerivice.getById(LESSON_ID);
        verify(modelMapperMock).map(lesson, LessonDTO.class);
    }
    
    @Test
    void getMonthLessons_ShouldExecuteCorrectCallsQuantity() {
        when(lessonRepositoryMock.findByDatestamp(isA(LocalDate.class)))
        .thenReturn(lessons);
        when(modelMapperMock.map(lessons, LESSON_LIST_TYPE))
        .thenReturn(lessonsDto);
        lessonSerivice.getMonthLessons(LocalDate.now());
        verify(modelMapperMock, atLeastOnce()).map(lessons, LESSON_LIST_TYPE);
    }
    
    @Test
    void getDayLessons_ShouldExecuteCorectCallsQuantity() {
        when(lessonRepositoryMock.findByDatestamp(isA(LocalDate.class)))
            .thenReturn(lessons);
        when(modelMapperMock.map(lessons, LessonServiceImpl.LESSON_LIST_TYPE))
            .thenReturn(lessonsDto);
        lessonSerivice.getDayLessons(LocalDate.now());
        verify(modelMapperMock).map(lessons, LessonServiceImpl.LESSON_LIST_TYPE);
    }
    
    @Test
    void getAll_ShouldExecuteCorrectCallsQuantity() {
        List<Lesson> entities = Arrays.asList(lesson);
        when(lessonRepositoryMock.findAll()).thenReturn(entities);
        lessonSerivice.getAll();
        
        InOrder inOrder = Mockito.inOrder(lessonRepositoryMock, modelMapperMock);
        inOrder.verify(lessonRepositoryMock).findAll();
        inOrder.verify(modelMapperMock).map(
                ArgumentMatchers.<Lesson>anyList(), 
                any(Type.class));
    }
}
