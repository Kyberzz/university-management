package ua.com.foxminded.university.converter;

import java.time.Duration;

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
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TimetableEntity source = context.getSource();
        TimetableModel model = modelMapper.map(source, TimetableModel.class);
        toLessonOrder(model);
        return model;
    }
    
    private void toLessonOrder(TimetableModel model) {
        Duration lessonsPeriod = Duration.between(config.getFirstLessonStartTime(), 
                                                  model.getStartTime());
        float lessonOrder = (float) lessonsPeriod.toMinutes() / 
                config.getAverageLessonMinutesInterval() + 1f;
        model.setLessonOrder(LessonOrder.of(Math.round(lessonOrder)));
    }
}
