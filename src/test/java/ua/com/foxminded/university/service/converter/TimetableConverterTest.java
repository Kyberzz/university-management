package ua.com.foxminded.university.service.converter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.spi.MappingContext;

import ua.com.foxminded.university.entity.LessonOrder;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entitymother.TimetableEntityMother;
import ua.com.foxminded.university.model.TimetableModel;

@ExtendWith(MockitoExtension.class)
class TimetableConverterTest {
    
    @InjectMocks
    private TimetableConverter timetableConverter;
    
    @Mock
    private MappingContext<TimetableEntity, TimetableModel> contextMock;
    
    private TimetableEntity entity;
    
    @BeforeEach
    void setUp() {
        entity = TimetableEntityMother.complete().build();
    }
    
    @Test
    void toLessonPeriod_ShouldConvertPropertiesCorrectly() {
        LessonOrder lessonOrder = LessonOrder.FIRST_LESSON;
        when(contextMock.getSource()).thenReturn(entity);
        
        TimetableConverter converter = new TimetableConverter(); 
        TimetableModel model = converter.convert(contextMock);
        
        assertEquals(lessonOrder, model.getLessonOrder());
    }
}
