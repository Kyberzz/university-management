package ua.com.foxminded.university.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;
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
import ua.com.foxminded.university.repository.TimetableRepository;
import ua.com.foxminded.university.service.impl.TimetableServiceImpl;

@ExtendWith(MockitoExtension.class)
class TimetableServiceImplTest {
    
    @InjectMocks
    private TimetableServiceImpl timetableSerivice;
    
    @Mock
    private TimetableRepository timetableRepositoryMock;
    
    @Mock
    private ModelMapper modelMapperMock;
    
    private TimetableEntity entity;
    
    @BeforeEach
    void setUp() throws IOException {
        entity = TimetableEntityMother.complete().build();
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
    
    @Test
    void update_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        TimetableModel model = new TimetableModel();
        model.setDescription("asdf");
        when(modelMapperMock.map(model, TimetableEntity.class)).thenReturn(entity);
        timetableSerivice.update(model);
        
        InOrder inOrder = Mockito.inOrder(modelMapperMock, timetableRepositoryMock);
        
        inOrder.verify(modelMapperMock, times(1)).map(model,  TimetableEntity.class);
        inOrder.verify(timetableRepositoryMock, times(1)).save(isA(TimetableEntity.class));
    }
}