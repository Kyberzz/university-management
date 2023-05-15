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
    
    public static final int AVERAGE_LESSON_MINUTES_INTERVAL = 126;
    public static final int LESSON_MINUTES_DUREATION = 20;
    public static final String FIRST_LESSON_START_TIME = "08:00";
    
    @InjectMocks
    private TimetableConverter converterMock;
    
    @Mock
    private MappingContext<TimetableEntity, TimetableModel> contextMock;
    
    @Mock
    private TimetableConverterConfig configMock;
    
    private TimetableEntity entity;
    
    @BeforeEach
    void setUp() {
        entity = TimetableEntityMother.complete().build();
    }
    
    @Test
    void toLessonPeriod_ShouldConvertPropertiesCorrectly() {
        when(configMock.getLessonMinutesDuration())
            .thenReturn(LESSON_MINUTES_DUREATION);
        when(configMock.getFirstLessonStartTime())
            .thenReturn(LocalTime.parse(FIRST_LESSON_START_TIME));
        when(configMock.getAverageLessonsMinutesInterval()).thenReturn(
                AVERAGE_LESSON_MINUTES_INTERVAL);
        when(contextMock.getSource()).thenReturn(entity);
        
        TimetableModel model = converterMock.convert(contextMock);
        
        assertEquals(LessonOrder.FIRST_LESSON, model.getLessonOrder());
    }
}
