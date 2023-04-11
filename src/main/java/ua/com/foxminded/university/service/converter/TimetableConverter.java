package ua.com.foxminded.university.service.converter;

import java.time.Duration;
import java.time.LocalTime;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;

import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.model.LessonOrder;
import ua.com.foxminded.university.model.TimetableModel;

public class TimetableConverter implements Converter<TimetableEntity, TimetableModel> {
    
    private static final LocalTime FIRST_LESSON_START_TIME = LocalTime.of(8, 0);
    private static final int AVERAGE_LESSON_MINUTES_INTERVAL = 120;
    private static final int ONE = 1;
    
    @Override
    public TimetableModel convert(MappingContext<TimetableEntity, TimetableModel> context) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TimetableEntity source = context.getSource();
        TimetableModel model = modelMapper.map(source, TimetableModel.class);
        toLessonOrder(model);
        return model;
    }
    
    private void toLessonOrder(TimetableModel model) {
        Duration lessonsPeriod = Duration.between(FIRST_LESSON_START_TIME, model.getStartTime());
        int lessonOrder = (int) lessonsPeriod.toMinutes() / AVERAGE_LESSON_MINUTES_INTERVAL + ONE;
        model.setLessonOrder(LessonOrder.of(lessonOrder));
    }
}
