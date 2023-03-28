package ua.com.foxminded.university.service.converter;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.cache.PropertiesCache;
import ua.com.foxminded.university.entity.LessonOrder;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.model.TimetableModel;

@RequiredArgsConstructor
public class TimetableConverter implements Converter<TimetableEntity, TimetableModel> {
    
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
    
    private final PropertiesCache fridayLessonsPeriodCache; 
    private final PropertiesCache defaultLessonsPeriodCache;
    
    @Override
    public TimetableModel convert(MappingContext<TimetableEntity, TimetableModel> context) {
        
        TimetableEntity source = context.getSource();
        LessonPeriod lessonPeriod = toLessonPeriod(source.getLessonOrder(), 
                                                   source.getDayOfWeek());
        TimetableModel model = new TimetableModel();
        model.setLessonPeriod(lessonPeriod);
        return model;
    }
    
    private LessonPeriod toLessonPeriod(LessonOrder order, DayOfWeek dayOfWeek){
        switch (order) {
        case FIRST_LESSON:
            return selectLessonsPeriodRange(dayOfWeek).getFirstLessonPeriod();
        case SECOND_LESSON:
            return selectLessonsPeriodRange(dayOfWeek).getSecondLessonPeriod();
        case THIRD_LESSON:
            return selectLessonsPeriodRange(dayOfWeek).getThirdLessonPeriod();
        case FOURTH_LESSON:
            return selectLessonsPeriodRange(dayOfWeek).getFourthLessonPeriod();
        case FIFTH_LESSON:
            return selectLessonsPeriodRange(dayOfWeek).getFifthLessonPeriod();
        default:
            return null;
        }
    }
    
    private LessonPeriodRange selectLessonsPeriodRange(DayOfWeek dayOfWeek) {
        
        if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
            return getLessonsPeriodRange(fridayLessonsPeriodCache);
        } else {
            return getLessonsPeriodRange(defaultLessonsPeriodCache);
        }
    }
    
    private LessonPeriodRange getLessonsPeriodRange(PropertiesCache lessonsPeriodCache) {
        LessonPeriodRange range = new LessonPeriodRange();
        LocalTime firstStart = LocalTime.parse(
                lessonsPeriodCache.getProperty(FIRST_LESSON_START_TIME));
        LocalTime firstEnd = LocalTime.parse(
                lessonsPeriodCache.getProperty(FIRST_LESSON_END_TIME));
        LocalTime secondStart = LocalTime.parse(
                lessonsPeriodCache.getProperty(SECOND_LESSON_START_TIME));
        LocalTime secondEnd = LocalTime.parse(
                lessonsPeriodCache.getProperty(SECOND_LESSON_END_TIME));
        LocalTime thirdStart = LocalTime.parse(
                lessonsPeriodCache.getProperty(THIRD_LESSON_START_TIME));
        LocalTime thirdEnd = LocalTime.parse(
                lessonsPeriodCache.getProperty(THIRD_LESSON_END_TIME));
        LocalTime fourthStart = LocalTime.parse(
                lessonsPeriodCache.getProperty(FOURTH_LESSON_START_TIME));
        LocalTime fourthEnd = LocalTime.parse(
                lessonsPeriodCache.getProperty(FOURTH_LESSON_END_TIME));
        LocalTime fifthStart = LocalTime.parse(
                lessonsPeriodCache.getProperty(FIFTH_LESSON_START_TIME));
        LocalTime fifthEnd = LocalTime.parse(
                lessonsPeriodCache.getProperty(FIFTH_LESSON_END_TIME));
        
        range.setFirstLessonPeriod(new LessonPeriod());
        range.setSecondLessonPeriod(new LessonPeriod());
        range.setThirdLessonPeriod(new LessonPeriod());
        range.setFourthLessonPeriod(new LessonPeriod());
        range.setFifthLessonPeriod(new LessonPeriod());
        
        range.getFirstLessonPeriod().setStartTime(firstStart);
        range.getFirstLessonPeriod().setEndTime(firstEnd);
        range.getSecondLessonPeriod().setStartTime(secondStart);
        range.getSecondLessonPeriod().setEndTime(secondEnd);
        range.getSecondLessonPeriod().setStartTime(thirdStart);
        range.getThirdLessonPeriod().setEndTime(thirdEnd);
        range.getFourthLessonPeriod().setStartTime(fourthStart);
        range.getFourthLessonPeriod().setEndTime(fourthEnd);
        range.getFifthLessonPeriod().setStartTime(fifthStart);
        range.getFifthLessonPeriod().setEndTime(fifthEnd);
        return range;
    }
}
