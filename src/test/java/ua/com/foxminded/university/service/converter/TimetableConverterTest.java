package ua.com.foxminded.university.service.converter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import ua.com.foxminded.university.cache.PropertiesCache;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entitymother.TimetableEntityMother;
import ua.com.foxminded.university.model.TimetableModel;

@ExtendWith(MockitoExtension.class)
class TimetableConverterTest {

    public static final String FIFTH_LESSON_END_TIME = "fifthLessonEndTime";
    public static final String FIFTH_LESSON_START_TIME = "fifthLessonStartTime";
    public static final String FOURTH_LESSON_START_TIME = "fourthLessonStartTime";
    public static final String FOURTH_LESSON_END_TIME = "fourthLessonEndTime";
    public static final String THIRD_LESSON_END_TIME = "thirdLessonEndTime";
    public static final String THIRD_LESSON_START_TIME = "thirdLessonStartTime";
    public static final String SECOND_LESSON_START_TIME = "secondLessonStartTime";
    public static final String SECOND_LESSON_END_TIME = "secondLessonEndTime";
    public static final String FIRST_LESSON_END_TIME = "firstLessonEndTime";
    public static final String FIRST_LESSON_START_TIME = "firstLessonStartTime";
    
    public static final String END_TIME = "09:45";
    public static final String START_TIME = "08:00";
    
    
    @InjectMocks
    private TimetableConverter timetableConverter;
    
    @Mock
    private PropertiesCache lessonsPeriodCacheMock;
    
    private TimetableEntity entity;
    
    @BeforeEach
    void setUp() {
        when(lessonsPeriodCacheMock.getProperty(FIRST_LESSON_START_TIME)).thenReturn(START_TIME);
        when(lessonsPeriodCacheMock.getProperty(FIRST_LESSON_END_TIME)).thenReturn(END_TIME);
        when(lessonsPeriodCacheMock.getProperty(SECOND_LESSON_START_TIME)).thenReturn(START_TIME);
        when(lessonsPeriodCacheMock.getProperty(SECOND_LESSON_END_TIME)).thenReturn(END_TIME);
        when(lessonsPeriodCacheMock.getProperty(THIRD_LESSON_START_TIME)).thenReturn(START_TIME);
        when(lessonsPeriodCacheMock.getProperty(THIRD_LESSON_END_TIME)).thenReturn(END_TIME);
        when(lessonsPeriodCacheMock.getProperty(FOURTH_LESSON_START_TIME)).thenReturn(START_TIME);
        when(lessonsPeriodCacheMock.getProperty(FOURTH_LESSON_END_TIME)).thenReturn(END_TIME);
        when(lessonsPeriodCacheMock.getProperty(FIFTH_LESSON_START_TIME)).thenReturn(START_TIME);
        when(lessonsPeriodCacheMock.getProperty(FIFTH_LESSON_END_TIME)).thenReturn(END_TIME);
        
        entity = TimetableEntityMother.complete().build();
    }

    @Test
    void toLessonPeriod_shouldConvertLessonOrderToLessonPeriod() {
        TimetableConverter converter = new TimetableConverter(lessonsPeriodCacheMock, 
                                                              lessonsPeriodCacheMock);
        ModelMapper modelMapper = new ModelMapper();
        
        TypeMap<TimetableEntity, TimetableModel> typeMap = modelMapper.createTypeMap(
                TimetableEntity.class, TimetableModel.class);
        typeMap.setPreConverter(converter);
        TimetableModel model = modelMapper.map(entity, TimetableModel.class);
        
        LocalTime expectedStartTime = LocalTime.parse(START_TIME);
        LocalTime expectedEndTime = LocalTime.parse(END_TIME);

        assertEquals(expectedStartTime, model.getLessonPeriod().getStartTime());
        assertEquals(expectedEndTime, model.getLessonPeriod().getEndTime());
        assertEquals(entity.getLessonOrder(), model.getLessonOrder());
    }
}
