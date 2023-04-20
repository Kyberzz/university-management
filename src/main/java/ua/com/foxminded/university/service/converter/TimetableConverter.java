package ua.com.foxminded.university.service.converter;

import java.time.Duration;
import java.time.LocalTime;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.model.LessonOrder;
import ua.com.foxminded.university.model.TimetableModel;

public class TimetableConverter implements Converter<TimetableEntity, TimetableModel> {
    
    @Value("${firstLessonStartTime}")
    private String stringFirstLessonStartTime;
    
    @Value("${averageLessonMinutesInterval}")
    private int averageLessonMinutesInterval;
    
    private LocalTime firstLessonStartTime = LocalTime.parse(stringFirstLessonStartTime);
    
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
        Duration lessonsPeriod = Duration.between(firstLessonStartTime, model.getStartTime());
        int lessonOrder = (int) lessonsPeriod.toMinutes() / averageLessonMinutesInterval + 1;
        model.setLessonOrder(LessonOrder.of(lessonOrder));
    }
}
