package ua.com.foxminded.university.converter;

import static java.util.Comparator.comparing;

import java.util.Arrays;
import java.util.List;

import org.modelmapper.PropertyMap;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.entity.LessonEntity;
import ua.com.foxminded.university.entity.TimingEntity;
import ua.com.foxminded.university.model.LessonModel;
import ua.com.foxminded.university.repository.TimingRepository;

@RequiredArgsConstructor
public class LessonEntityMap extends PropertyMap<LessonEntity, LessonModel> {
    
    public static final int OFFSET = 1;
    
    private final TimingRepository timingRepository;

    @Override
    protected void configure() {
        map().setStartTime(getTiming().getStartTime());
        map().setEndTime(getTiming().getStartTime().plus(getTiming().getLessonDuration()));
    }
    
    private TimingEntity getTiming() {
        List<TimingEntity> timings = timingRepository.findAllById(
                Arrays.asList(source.getTimetable().getId()));
        return timings.stream().sorted(comparing(TimingEntity::getStartTime))
                               .skip(source.getLessonOrder() - OFFSET)
                               .findFirst().get();
    }
}
