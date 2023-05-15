package ua.com.foxminded.university.converter;

import java.time.Duration;
import java.time.LocalTime;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.config.TimetableConverterConfig;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.model.LessonOrder;
import ua.com.foxminded.university.model.TimetableModel;

@RequiredArgsConstructor
public class TimetableConverter implements Converter<TimetableEntity, TimetableModel> {
    
    private final TimetableConverterConfig config;
    
    @Override
    public TimetableModel convert(MappingContext<TimetableEntity, TimetableModel> context) {
        TimetableEntity source = context.getSource();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TimetableModel model = modelMapper.map(source, TimetableModel.class);
        addLessonOrder(model);
        addEndTime(model);
        return model;
    }
    
    private void addEndTime(TimetableModel model) {
        Duration lessonMinutes = Duration.ofMinutes(config.getLessonMinutesDuration());
        LocalTime endTime = model.getStartTime().plus(lessonMinutes);
        model.setEndTime(endTime);
    }
    
    private void addLessonOrder(TimetableModel model) {
        Duration lessonsPeriod = Duration.between(config.getFirstLessonStartTime(), 
                                                  model.getStartTime());
        float lessonOrder = (float) lessonsPeriod.toMinutes() / 
                config.getAverageLessonsMinutesInterval() + 1f;
        model.setLessonOrder(LessonOrder.of(Math.round(lessonOrder)));
    }
}
