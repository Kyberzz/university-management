package ua.com.foxminded.university.converter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.spi.MappingContext;

import ua.com.foxminded.university.config.TimetableConverterConfig;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entitymother.TimetableEntityMother;
import ua.com.foxminded.university.model.LessonOrder;
import ua.com.foxminded.university.model.TimetableModel;

@ExtendWith(MockitoExtension.class)
class TimetableConverterTest {
    
    public static final int AVERAGE_LESSON_MINUTES_INTERVAL = 20;
    public static final String FIRST_LESSON_START_TIME = "08:00";
    
    @InjectMocks
    private TimetableConverter timetableConverter;
    
    @Mock
    private MappingContext<TimetableEntity, TimetableModel> contextMock;
    
    @Mock
    private TimetableConverterConfig config;
    
    private TimetableEntity entity;
    
    @BeforeEach
    void setUp() {
        entity = TimetableEntityMother.complete().build();
    }
    
    @Test
    void toLessonPeriod_ShouldConvertPropertiesCorrectly() {
        when(config.getAverageLessonMinutesInterval())
            .thenReturn(AVERAGE_LESSON_MINUTES_INTERVAL);
        when(config.getFirstLessonStartTime())
            .thenReturn(LocalTime.parse(FIRST_LESSON_START_TIME));
        LessonOrder lessonOrder = LessonOrder.FIRST_LESSON;
        when(contextMock.getSource()).thenReturn(entity);
        
        TimetableConverter converter = new TimetableConverter(config); 
        TimetableModel model = converter.convert(contextMock);
        
        assertEquals(lessonOrder, model.getLessonOrder());
    }
}
