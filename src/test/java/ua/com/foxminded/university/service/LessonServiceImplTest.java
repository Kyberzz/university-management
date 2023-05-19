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

import ua.com.foxminded.university.entity.LessonEntity;
import ua.com.foxminded.university.entitymother.LessonEntityMother;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.model.LessonModel;
import ua.com.foxminded.university.modelmother.GroupModelMother;
import ua.com.foxminded.university.modelmother.LessonModelMother;
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
    
    private LessonEntity lessonEntity;
    private LessonModel lessonModel;
    private List<LessonEntity> lessonEntities;
    private List<LessonModel> lessonModels;
    private GroupModel groupModel;
    
    @BeforeEach
    void setUp() throws IOException {
        lessonEntity = LessonEntityMother.complete().build();
        lessonModel = LessonModelMother.complete().build();
        lessonEntities = Arrays.asList(lessonEntity);
        lessonModels = Arrays.asList(lessonModel);
        groupModel = GroupModelMother.complete().build();
    }
    
    @Test
    void moveBack_ShouldMoveBackDatestamp() {
        LocalDate localDate = LocalDate.now();
        LocalDate expectedResult = localDate.minusWeeks(
                LessonServiceImpl.OFFSET_WEEKS_QUANTITY);
        assertEquals(expectedResult, lessonSerivice.moveBack(localDate));
    }
    
    @Test
    void moveForward_ShouldMoveForwardDatestamp() {
        LocalDate localDate = LocalDate.now();
        LocalDate expectedResult = localDate.plusWeeks(
                LessonServiceImpl.OFFSET_WEEKS_QUANTITY);
        assertEquals(expectedResult, lessonSerivice.moveForward(localDate));
    }
    
    @Test
    void deleteById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        lessonSerivice.deleteById(anyInt());
        verify(lessonRepositoryMock).deleteById(isA(Integer.class));
    }
    
    @Test
    void update_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        LessonModel model = LessonModel.builder().id(LESSON_ID).build();
        when(modelMapperMock.map(model, LessonEntity.class)).thenReturn(lessonEntity);
        when(lessonRepositoryMock.findById(anyInt())).thenReturn(lessonEntity);
        lessonSerivice.update(model);
        verify(lessonRepositoryMock).saveAndFlush(isA(LessonEntity.class));
    }
    
    @Test
    void create_ShouldCallComponents_WhenIfBlockFalse() throws ServiceException {
        lessonEntity.setId(LESSON_ID);
        lessonModel.setId(LESSON_ID);
        groupModel.setId(GROUP_ID);
        lessonModel.setGroup(groupModel);
        when(lessonRepositoryMock.findByDatestampAndGroupIdAndTimingId(
                isA(LocalDate.class), anyInt(), anyInt()))
            .thenReturn(lessonEntity);
        when(modelMapperMock.map(lessonModel, LessonEntity.class)).thenReturn(
                lessonEntity);
        when(lessonRepositoryMock.findById(anyInt())).thenReturn(lessonEntity);
        lessonSerivice.create(lessonModel);
        verify(lessonRepositoryMock).saveAndFlush(isA(LessonEntity.class));
    }
    
    @Test
    void create_ShouldCallComponents_WhenIfBlockTure() throws ServiceException {
        groupModel.setId(GROUP_ID);
        lessonModel.setGroup(groupModel);
        when(lessonRepositoryMock.findByDatestampAndGroupIdAndTimingId(
                isA(LocalDate.class), anyInt(), anyInt())).thenReturn(null);
        when(modelMapperMock.map(lessonModel, LessonEntity.class))
            .thenReturn(lessonEntity);
        lessonSerivice.create(lessonModel);
        verify(lessonRepositoryMock).saveAndFlush(isA(LessonEntity.class));
    }
    
    @Test
    void getById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(lessonRepositoryMock.findById(anyInt())).thenReturn(lessonEntity);
        lessonSerivice.getById(LESSON_ID);
        verify(modelMapperMock).map(lessonEntity, LessonModel.class);
    }
    
    @Test
    void getMonthTimetable_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(lessonRepositoryMock.findByDatestamp(isA(LocalDate.class)))
        .thenReturn(lessonEntities);
        when(modelMapperMock.map(lessonEntities, LessonServiceImpl.LESSON_MODELS_LIST_TYPE))
        .thenReturn(lessonModels);
        lessonSerivice.getMonthLessons(LocalDate.now());
        verify(modelMapperMock, atLeastOnce()).map(lessonEntities, LessonServiceImpl.LESSON_MODELS_LIST_TYPE);
    }
    
    @Test
    void getDayTimetalbe_ShouldExecuteCorectCallsQuantity() throws ServiceException {
        when(lessonRepositoryMock.findByDatestamp(isA(LocalDate.class)))
            .thenReturn(lessonEntities);
        when(modelMapperMock.map(lessonEntities, LessonServiceImpl.LESSON_MODELS_LIST_TYPE))
            .thenReturn(lessonModels);
        lessonSerivice.getDayLessons(LocalDate.now());
        verify(modelMapperMock).map(lessonEntities, LessonServiceImpl.LESSON_MODELS_LIST_TYPE);
    }
    
    @Test
    void getAll_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        List<LessonEntity> entities = Arrays.asList(lessonEntity);
        when(lessonRepositoryMock.findAll()).thenReturn(entities);
        lessonSerivice.getAll();
        
        InOrder inOrder = Mockito.inOrder(lessonRepositoryMock, modelMapperMock);
        inOrder.verify(lessonRepositoryMock).findAll();
        inOrder.verify(modelMapperMock).map(
                ArgumentMatchers.<LessonEntity>anyList(), 
                any(Type.class));
    }
}
