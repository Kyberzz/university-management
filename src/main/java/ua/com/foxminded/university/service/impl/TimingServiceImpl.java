package ua.com.foxminded.university.service.impl;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.modelmapper.TypeToken;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.dto.TimingDTO;
import ua.com.foxminded.university.entity.Timing;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.repository.TimingRepository;
import ua.com.foxminded.university.service.TimingService;

@Service
@Transactional
@RequiredArgsConstructor
public class TimingServiceImpl implements TimingService {
   
    public static final int FIRST_ELEMENT = 0;
    public static final int SECOND_ELEMENT = 1;
    public static final Type LESSON_TIMING_LIST_TYPE = 
            new TypeToken<List<TimingDTO>>() {}.getType();
    
    private final ModelMapper modelMapper;
    private final TimingRepository timingRepository;
    
    @Override
    public List<TimingDTO> getByTimetableId(int id) throws ServiceException {
        try {
            List<Timing> timings = timingRepository.findByTimetableId(id);
            return modelMapper.map(timings, LESSON_TIMING_LIST_TYPE );
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timing of lessons fails", e);
        }
    }

    @Override
    public void deleteById(Integer id) throws ServiceException {
        try {
            timingRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Deleting the LessonTiming by its id fails", e);
        }
    }

    @Override
    public TimingDTO create(TimingDTO model) throws ServiceException {
        try {
            checkTimingConsistency(model);
            Timing entity = modelMapper.map(model, Timing.class);
            Timing createdEntity = timingRepository.saveAndFlush(entity);
            return modelMapper.map(createdEntity, TimingDTO.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Creating timing fails", e);
        }
    }

    @Override
    public void update(TimingDTO model) throws ServiceException {
        try {
            checkTimingConsistency(model);
            Timing persistedEntity = timingRepository.findById(
                    model.getId().intValue());
            Timing entity = modelMapper.map(model, Timing.class);
            persistedEntity.setBreakDuration(entity.getBreakDuration());
            persistedEntity.setLessonDuration(entity.getLessonDuration());
            persistedEntity.setStartTime(entity.getStartTime());
            persistedEntity.setTimetable(entity.getTimetable());
            timingRepository.saveAndFlush(entity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Updating timing fails", e);
        }
    }

    @Override
    public TimingDTO getById(int id) throws ServiceException {
        try {
            Timing timing = timingRepository.findById(id);
            return modelMapper.map(timing, TimingDTO.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting the LessonTiming by its fails", e);
        }
    }

    @Override
    public List<TimingDTO> getAll() throws ServiceException {
        try {
            List<Timing> lessonsTiming = timingRepository.findAll();
            return modelMapper.map(lessonsTiming, LESSON_TIMING_LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting lessons timing fails", e);
        }
    }
    
    private void checkConsistencyWithLessonBefore(Optional<Timing> lessonBefore, 
                                                  TimingDTO timingDto) {
        if (lessonBefore.isPresent()) {
            LocalTime lessonBeforeEndTime = lessonBefore.get().getStartTime()
                    .plus(lessonBefore.get().getLessonDuration())
                    .plus(lessonBefore.get().getBreakDuration());
            
            if (lessonBeforeEndTime.isAfter(timingDto.getStartTime())) {
                throw new IllegalArgumentException(
                        "The start lesson time is not compatible with existence ones");
            }
        }
    }
    
    private void checkConsistencyWithLessonAfter(Optional<Timing> lessonAfter, 
                                                 TimingDTO timingDto) {
        if (lessonAfter.isPresent()) {
            LocalTime checkingPeriodEndTime = timingDto.getStartTime()
                    .plus(timingDto.getLessonDuration())
                    .plus(timingDto.getBreakDuration());
            
            if(checkingPeriodEndTime.isAfter(lessonAfter.get().getStartTime())) {
                throw new IllegalAccessError(
                        "The end lesson time is not compatible with existence ones");
            }
        }
    }
    
    private void checkTimingConsistency(TimingDTO timingDto) {
        List<Timing> timings = timingRepository.findByTimetableId(timingDto.getTimetable()
                                                                           .getId());
        Optional<Timing> counterpartLesson = timings.stream()
                .filter(timing -> timing.getStartTime().equals(timingDto.getStartTime()))
                .findFirst();
        if (counterpartLesson.isPresent()) {
            throw new IllegalArgumentException("A lesson with the same start time is present");
        }
        
        Optional<Timing> lessonBefore = timings.stream()
                .filter(timing -> timing.getStartTime().isBefore(timingDto.getStartTime()))
                .sorted(Comparator.comparing(Timing::getStartTime))
                .reduce((first, second) -> second);
                
        Optional<Timing> lessonAfter = timings.stream()
                .filter(timing -> timing.getStartTime().isAfter(timingDto.getStartTime()))
                .sorted(Comparator.comparing(Timing::getStartTime))
                .findFirst();
        
        checkConsistencyWithLessonBefore(lessonBefore, timingDto);
        checkConsistencyWithLessonAfter(lessonAfter, timingDto);
    }
}
