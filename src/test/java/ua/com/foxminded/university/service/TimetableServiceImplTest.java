package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entitymother.TimetableEntityMother;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.modelmother.TimetableModelMother;
import ua.com.foxminded.university.repository.TimetableRepository;
import ua.com.foxminded.university.service.impl.TimetableServiceImpl;

@ExtendWith(MockitoExtension.class)
class TimetableServiceImplTest {
    
    public static final int TIMETABLE_ID = 1;
    
    @InjectMocks
    private TimetableServiceImpl timetableSerivice;
    
    @Mock
    private TimetableRepository timetableRepositoryMock;
    
    @Mock
    private ModelMapper modelMapperMock;
    
    private TimetableEntity entity;
    private TimetableModel model;
    List<TimetableEntity> entities;
    List<TimetableModel> models;
    
    @BeforeEach
    void setUp() throws IOException {
        entity = TimetableEntityMother.complete().build();
        model = TimetableModelMother.complete().build();
        entities = Arrays.asList(entity);
        models = Arrays.asList(model);
    }
    
    @Test
    void moveBack_ShouldMoveBackDatestamp() {
        LocalDate localDate = LocalDate.now();
        LocalDate expectedResult = localDate.minusWeeks(
                TimetableServiceImpl.OFFSET_WEEKS_QUANTITY);
        assertEquals(expectedResult, timetableSerivice.moveBack(localDate));
    }
    
    @Test
    void moveForward_ShouldMoveForwardDatestamp() {
        LocalDate localDate = LocalDate.now();
        LocalDate expectedResult = localDate.plusWeeks(
                TimetableServiceImpl.OFFSET_WEEKS_QUANTITY);
        assertEquals(expectedResult, timetableSerivice.moveForward(localDate));
    }
    
    @Test
    void deleteById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        timetableSerivice.deleteById(anyInt());
        verify(timetableRepositoryMock).deleteById(isA(Integer.class));
    }
    
    @Test
    void update_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        TimetableModel model = TimetableModel.builder().id(TIMETABLE_ID).build();
        when(modelMapperMock.map(model, TimetableEntity.class)).thenReturn(entity);
        when(timetableRepositoryMock.findById(anyInt())).thenReturn(entity);
        timetableSerivice.update(model);
        verify(timetableRepositoryMock).saveAndFlush(isA(TimetableEntity.class));
    }
    
    @Test
    void create_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(modelMapperMock.map(model, TimetableEntity.class)).thenReturn(entity);
        timetableSerivice.create(model);
        verify(timetableRepositoryMock).saveAndFlush(isA(TimetableEntity.class));
    }
    
    @Test
    void getById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(timetableRepositoryMock.findById(anyInt())).thenReturn(entity);
        timetableSerivice.getById(TIMETABLE_ID);
        verify(modelMapperMock).map(entity, TimetableModel.class);
    }
    
    @Test
    void getMonthTimetable_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(timetableRepositoryMock.findByDatestamp(isA(LocalDate.class)))
        .thenReturn(entities);
        when(modelMapperMock.map(entities, TimetableServiceImpl.TIMETABEL_MODEL_LIST_TYPE))
        .thenReturn(models);
        timetableSerivice.getMonthTimetable(LocalDate.now());
        verify(modelMapperMock, atLeastOnce()).map(entities, TimetableServiceImpl.TIMETABEL_MODEL_LIST_TYPE);
    }
    
    @Test
    void getDayTimetalbe_ShouldExecuteCorectCallsQuantity() throws ServiceException {
        when(timetableRepositoryMock.findByDatestamp(isA(LocalDate.class)))
            .thenReturn(entities);
        when(modelMapperMock.map(entities, TimetableServiceImpl.TIMETABEL_MODEL_LIST_TYPE))
            .thenReturn(models);
        timetableSerivice.getDayTimetalbe(LocalDate.now());
        verify(modelMapperMock).map(entities, TimetableServiceImpl.TIMETABEL_MODEL_LIST_TYPE);
    }
    
    @Test
    void getAll_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        List<TimetableEntity> entities = Arrays.asList(entity);
        when(timetableRepositoryMock.findAll()).thenReturn(entities);
        timetableSerivice.getAll();
        
        InOrder inOrder = Mockito.inOrder(timetableRepositoryMock, modelMapperMock);
        inOrder.verify(timetableRepositoryMock, times(1)).findAll();
        inOrder.verify(modelMapperMock, times(1)).map(
                ArgumentMatchers.<TimetableEntity>anyList(), 
                any(Type.class));
    }
}