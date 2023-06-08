package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.service.GroupServiceImplTest.GROUP_ID;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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

import ua.com.foxminded.university.dto.GroupDTO;
import ua.com.foxminded.university.dto.LessonDTO;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entitymother.LessonMother;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.modelmother.GroupDtoMother;
import ua.com.foxminded.university.modelmother.LessonDtoMother;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.service.impl.LessonServiceImpl;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {
    
    public static final int LESSON_ID = 1;
    
    @InjectMocks
    private LessonServiceImpl lessonSerivice;
    
    @Mock
    private LessonRepository lessonRepositoryMock;
    
    @Mock
    private ModelMapper modelMapperMock;
    
    private Lesson lesson;
    private LessonDTO lessonDto;
    private List<Lesson> lessons;
    private List<LessonDTO> lessonsDto;
    private GroupDTO groupDto;
    
    @BeforeEach
    void setUp() throws IOException {
        lesson = LessonMother.complete().build();
        lessonDto = LessonDtoMother.complete().build();
        lessons = Arrays.asList(lesson);
        lessonsDto = Arrays.asList(lessonDto);
        groupDto = GroupDtoMother.complete().build();
    }
    
    @Test
    void moveBack_ShouldMoveBackDatestamp() throws ServiceException {
        LocalDate localDate = LocalDate.now();
        LocalDate expectedResult = localDate.minusWeeks(
                LessonServiceImpl.WEEKS_OFFSET);
        assertEquals(expectedResult, lessonSerivice.moveMonthBack(localDate));
    }
    
    @Test
    void moveForward_ShouldMoveForwardDatestamp() throws ServiceException {
        LocalDate localDate = LocalDate.now();
        LocalDate expectedResult = localDate.plusWeeks(
                LessonServiceImpl.WEEKS_OFFSET);
        assertEquals(expectedResult, lessonSerivice.moveMonthForward(localDate));
    }
    
    @Test
    void deleteById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        lessonSerivice.deleteById(anyInt());
        verify(lessonRepositoryMock).deleteById(isA(Integer.class));
    }
    
    @Test
    void update_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        LessonDTO model = LessonDTO.builder().id(LESSON_ID).build();
        when(modelMapperMock.map(model, Lesson.class)).thenReturn(lesson);
        when(lessonRepositoryMock.findById(anyInt())).thenReturn(lesson);
        lessonSerivice.update(model);
        verify(lessonRepositoryMock).saveAndFlush(isA(Lesson.class));
    }
    
    @Test
    void create_ShouldCallComponents_WhenIfBlockFalse() throws ServiceException {
        lesson.setId(LESSON_ID);
        lessonDto.setId(LESSON_ID);
        groupDto.setId(GROUP_ID);
        lessonDto.setGroup(groupDto);
        when(lessonRepositoryMock.findByDatestampAndLessonOrderAndGroupsId(
                isA(LocalDate.class), anyInt(), anyInt()))
            .thenReturn(lesson);
        when(modelMapperMock.map(lessonDto, Lesson.class)).thenReturn(
                lesson);
        when(lessonRepositoryMock.findById(anyInt())).thenReturn(lesson);
        lessonSerivice.create(lessonDto);
        verify(lessonRepositoryMock).saveAndFlush(isA(Lesson.class));
    }
    
    @Test
    void create_ShouldCallComponents_WhenIfBlockTure() throws ServiceException {
        groupDto.setId(GROUP_ID);
        lessonDto.setGroup(groupDto);
        when(lessonRepositoryMock.findByDatestampAndLessonOrderAndGroupsId(
                isA(LocalDate.class), anyInt(), anyInt())).thenReturn(null);
        when(modelMapperMock.map(lessonDto, Lesson.class))
            .thenReturn(lesson);
        lessonSerivice.create(lessonDto);
        verify(lessonRepositoryMock).saveAndFlush(isA(Lesson.class));
    }
    
    @Test
    void getById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(lessonRepositoryMock.findById(anyInt())).thenReturn(lesson);
        lessonSerivice.getById(LESSON_ID);
        verify(modelMapperMock).map(lesson, LessonDTO.class);
    }
    
    @Test
    void getMonthTimetable_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(lessonRepositoryMock.findByDatestamp(isA(LocalDate.class)))
        .thenReturn(lessons);
        when(modelMapperMock.map(lessons, LessonServiceImpl.LESSON_MODELS_LIST_TYPE))
        .thenReturn(lessonsDto);
        lessonSerivice.getMonthLessons(LocalDate.now());
        verify(modelMapperMock, atLeastOnce()).map(lessons, LessonServiceImpl.LESSON_MODELS_LIST_TYPE);
    }
    
    @Test
    void getDayTimetalbe_ShouldExecuteCorectCallsQuantity() throws ServiceException {
        when(lessonRepositoryMock.findByDatestamp(isA(LocalDate.class)))
            .thenReturn(lessons);
        when(modelMapperMock.map(lessons, LessonServiceImpl.LESSON_MODELS_LIST_TYPE))
            .thenReturn(lessonsDto);
        lessonSerivice.getDayLessons(LocalDate.now());
        verify(modelMapperMock).map(lessons, LessonServiceImpl.LESSON_MODELS_LIST_TYPE);
    }
    
    @Test
    void getAll_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
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
