package ua.com.foxminded.university.service.converter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.university.entity.LessonOrder;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entitymother.TimetableEntityMother;
import ua.com.foxminded.university.model.TimetableModel;

@SpringBootTest
@ActiveProfiles("test")
class TimetableConverterIntegrationTest {
    
    @Autowired
    private ModelMapper modelMapper;
    
    private TimetableEntity entity;
    
    @BeforeEach
    void setUp() {
        entity = TimetableEntityMother.complete().build();
    }
    
    @Test
    void toLessonPeriod_ShouldConvertEntityCorrectly() {
        TimetableModel model = modelMapper.map(entity, TimetableModel.class);
        LessonOrder expectedLessonOrder = LessonOrder.FIRST_LESSON;
        assertEquals(entity.getDayOfWeek(), model.getDayOfWeek());
        assertEquals(expectedLessonOrder, model.getLessonOrder());
    }
}
