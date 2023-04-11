package ua.com.foxminded.university.service.converter;

import java.time.LocalTime;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.LessonOrder;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.model.LocalTimePeriod;
import ua.com.foxminded.university.model.TimetableModel;

@Getter
@Setter
@Slf4j
public class TimetableConverter implements Converter<TimetableEntity, TimetableModel> {
    
    public static final LocalTime DEF_FIRST_LESSON_START_TIME = LocalTime.of(8, 00);
    public static final LocalTime DEF_FIRST_LESSON_END_TIME = LocalTime.of(9, 45);
    public static final LocalTime DEF_SECOND_LESSON_START_TIME = LocalTime.of(10, 00);
    public static final LocalTime DEF_SECOND_LESSON_END_TIME = LocalTime.of(11, 45);
    public static final LocalTime DEF_THIRD_LESSON_START_TIME = LocalTime.of(12, 30);
    public static final LocalTime DEF_THIRD_LESSON_END_TIME = LocalTime.of(14, 15);
    public static final LocalTime DEF_FOURTH_LESSON_START_TIME = LocalTime.of(14, 30);
    public static final LocalTime DEF_FOURTH_LESSON_END_TIME = LocalTime.of(16, 15);
    public static final LocalTime DEF_FIFTH_LESSON_START_TIME = LocalTime.of(16, 30);
    public static final LocalTime DEF_FIFTH_LESSON_END_TIME = LocalTime.of(18, 15);
    
    private LocalTime fifthLessonEndTime;
    private LocalTime fifthLessonStartTime;
    private LocalTime fourthLessonEndTime;
    private LocalTime fourthLessonStartTime;
    private LocalTime thirdLessonEndTime;
    private LocalTime thirdLessonStartTime;
    private LocalTime secondLessonEndTime;
    private LocalTime secondLessonStartTime;
    private LocalTime firstLessonEndTime;
    private LocalTime firstLessonStartTime;
    
    public TimetableConverter() {
        fifthLessonEndTime = DEF_FIFTH_LESSON_END_TIME;
        fifthLessonStartTime = DEF_FIFTH_LESSON_START_TIME;
        fourthLessonEndTime = DEF_FOURTH_LESSON_END_TIME;
        fourthLessonStartTime = DEF_FOURTH_LESSON_START_TIME;
        thirdLessonEndTime = DEF_THIRD_LESSON_END_TIME;
        thirdLessonStartTime = DEF_THIRD_LESSON_START_TIME;
        secondLessonEndTime = DEF_SECOND_LESSON_END_TIME;
        secondLessonStartTime = DEF_SECOND_LESSON_START_TIME;
        firstLessonEndTime = DEF_FIRST_LESSON_END_TIME;
        firstLessonStartTime = DEF_FIRST_LESSON_START_TIME;
    }

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
        LocalTime startTime = model.getStartTime();
        long deviation = 1;
        LocalTimePeriod firstLessonStartTimePeriod = LocalTimePeriod.between(
                DEF_FIRST_LESSON_START_TIME.minusHours(deviation), 
                DEF_FIRST_LESSON_START_TIME.plusHours(deviation));
        LocalTimePeriod secondLessonStartTimePeriod = LocalTimePeriod.between(
                DEF_SECOND_LESSON_START_TIME.minusHours(deviation), 
                DEF_SECOND_LESSON_START_TIME.plusHours(deviation));
        LocalTimePeriod thirdLessonStartTimePeriod = LocalTimePeriod.between(
                DEF_THIRD_LESSON_START_TIME.minusHours(deviation), 
                DEF_THIRD_LESSON_START_TIME.plusHours(deviation));
        LocalTimePeriod fourthLessonStartTimePeriod = LocalTimePeriod.between(
                DEF_FOURTH_LESSON_START_TIME.minusHours(deviation), 
                DEF_FOURTH_LESSON_START_TIME.plusHours(deviation));
        LocalTimePeriod fifthLessonStartTimePeriod = LocalTimePeriod.between(
                DEF_FIFTH_LESSON_START_TIME.minusHours(deviation), 
                DEF_FIFTH_LESSON_START_TIME.plusHours(deviation));
        
        if (firstLessonStartTimePeriod.include(startTime)) {
            model.setLessonOrder(LessonOrder.FIRST_LESSON);
        } else if (secondLessonStartTimePeriod.include(startTime)) {
            model.setLessonOrder(LessonOrder.SECOND_LESSON);
        } else if (thirdLessonStartTimePeriod.include(startTime)) {
            model.setLessonOrder(LessonOrder.THIRD_LESSON);
        } else if (fourthLessonStartTimePeriod.include(startTime)) {
            model.setLessonOrder(LessonOrder.FOURTH_LESSON);
        } else if (fifthLessonStartTimePeriod.include(startTime)) {
            model.setLessonOrder(LessonOrder.FIFTH_LESSON);
        } else {
            log.warn("The lesson start time in Timetable with {} doesn't correspond any "
                    + "specified period of time in order to have a lesson order", model.getId());
        }
    }
}
