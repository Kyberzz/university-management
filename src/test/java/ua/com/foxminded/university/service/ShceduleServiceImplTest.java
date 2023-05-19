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

import ua.com.foxminded.university.entity.LessonOrder;
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
class ShceduleServiceImplTest {
    
    public static final int TIMETABLE_ID = 1;
    
    @InjectMocks
    private LessonServiceImpl timetableSerivice;
    
    @Mock
    private LessonRepository timetableRepositoryMock;
    
    @Mock
    private ModelMapper modelMapperMock;
    
    private LessonEntity timetableEntity;
    private LessonModel timetableModel;
    private List<LessonEntity> timetableEntities;
    private List<LessonModel> timetableModels;
    private GroupModel groupModel;
    
    @BeforeEach
    void setUp() throws IOException {
        timetableEntity = LessonEntityMother.complete().build();
        timetableModel = LessonModelMother.complete().build();
        timetableEntities = Arrays.asList(timetableEntity);
        timetableModels = Arrays.asList(timetableModel);
        groupModel = GroupModelMother.complete().build();
    }
    
    @Test
    void moveBack_ShouldMoveBackDatestamp() {
        LocalDate localDate = LocalDate.now();
        LocalDate expectedResult = localDate.minusWeeks(
                LessonServiceImpl.OFFSET_WEEKS_QUANTITY);
        assertEquals(expectedResult, timetableSerivice.moveBack(localDate));
    }
    
    @Test
    void moveForward_ShouldMoveForwardDatestamp() {
        LocalDate localDate = LocalDate.now();
        LocalDate expectedResult = localDate.plusWeeks(
                LessonServiceImpl.OFFSET_WEEKS_QUANTITY);
        assertEquals(expectedResult, timetableSerivice.moveForward(localDate));
    }
    
    @Test
    void deleteById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        timetableSerivice.deleteById(anyInt());
        verify(timetableRepositoryMock).deleteById(isA(Integer.class));
    }
    
    @Test
    void update_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        LessonModel model = LessonModel.builder().id(TIMETABLE_ID).build();
        when(modelMapperMock.map(model, LessonEntity.class)).thenReturn(timetableEntity);
        when(timetableRepositoryMock.findById(anyInt())).thenReturn(timetableEntity);
        timetableSerivice.update(model);
        verify(timetableRepositoryMock).saveAndFlush(isA(LessonEntity.class));
    }
    
    @Test
    void create_ShouldCallComponents_WhenIfBlockFalse() throws ServiceException {
        timetableEntity.setId(TIMETABLE_ID);
        timetableModel.setId(TIMETABLE_ID);
        groupModel.setId(GROUP_ID);
        timetableModel.setGroup(groupModel);
//        when(timetableRepositoryMock.findByDatestampAndLessonOrderAndGroupId(
//                isA(LocalDate.class), isA(LessonOrder.class), anyInt()))
//            .thenReturn(timetableEntity);
        when(modelMapperMock.map(timetableModel, LessonEntity.class)).thenReturn(
                timetableEntity);
        when(timetableRepositoryMock.findById(anyInt())).thenReturn(timetableEntity);
        timetableSerivice.create(timetableModel);
        verify(timetableRepositoryMock).saveAndFlush(isA(LessonEntity.class));
    }
    
    @Test
    void create_ShouldCallComponents_WhenIfBlockTure() throws ServiceException {
        groupModel.setId(GROUP_ID);
        timetableModel.setGroup(groupModel);
//        when(timetableRepositoryMock.findByDatestampAndLessonOrderAndGroupId(
//                isA(LocalDate.class), isA(LessonOrder.class), anyInt())).thenReturn(null);
        when(modelMapperMock.map(timetableModel, LessonEntity.class))
            .thenReturn(timetableEntity);
        timetableSerivice.create(timetableModel);
        verify(timetableRepositoryMock).saveAndFlush(isA(LessonEntity.class));
    }
    
    @Test
    void getById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(timetableRepositoryMock.findById(anyInt())).thenReturn(timetableEntity);
        timetableSerivice.getById(TIMETABLE_ID);
        verify(modelMapperMock).map(timetableEntity, LessonModel.class);
    }
    
    @Test
    void getMonthTimetable_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(timetableRepositoryMock.findByDatestamp(isA(LocalDate.class)))
        .thenReturn(timetableEntities);
        when(modelMapperMock.map(timetableEntities, LessonServiceImpl.SCHEDULE_MODEL_LIST_TYPE))
        .thenReturn(timetableModels);
        timetableSerivice.getMonthLessons(LocalDate.now());
        verify(modelMapperMock, atLeastOnce()).map(timetableEntities, LessonServiceImpl.SCHEDULE_MODEL_LIST_TYPE);
    }
    
    @Test
    void getDayTimetalbe_ShouldExecuteCorectCallsQuantity() throws ServiceException {
        when(timetableRepositoryMock.findByDatestamp(isA(LocalDate.class)))
            .thenReturn(timetableEntities);
        when(modelMapperMock.map(timetableEntities, LessonServiceImpl.SCHEDULE_MODEL_LIST_TYPE))
            .thenReturn(timetableModels);
        timetableSerivice.getDayLessons(LocalDate.now());
        verify(modelMapperMock).map(timetableEntities, LessonServiceImpl.SCHEDULE_MODEL_LIST_TYPE);
    }
    
    @Test
    void getAll_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        List<LessonEntity> entities = Arrays.asList(timetableEntity);
        when(timetableRepositoryMock.findAll()).thenReturn(entities);
        timetableSerivice.getAll();
        
        InOrder inOrder = Mockito.inOrder(timetableRepositoryMock, modelMapperMock);
        inOrder.verify(timetableRepositoryMock).findAll();
        inOrder.verify(modelMapperMock).map(
                ArgumentMatchers.<LessonEntity>anyList(), 
                any(Type.class));
    }
}
