package ua.com.foxminded.university.service.impl;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.TypeToken;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
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
    public void create(TimingDTO model) throws ServiceException {
        try {
            Timing entity = modelMapper.map(model, Timing.class);
            timingRepository.saveAndFlush(entity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Creating timing fails", e);
        }
    }

    @Override
    public void update(TimingDTO model) throws ServiceException {
        try {
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
            Timing entity = timingRepository.findById(id);
            Provider<Timing> timingProvider = request -> new Timing();
            modelMapper.typeMap(Timing.class, TimingDTO.class).addMappings(mapper -> mapper.with(timingProvider)
                       .map(src -> src.getStartTime().plus(src.getLessonDuration()), TimingDTO::setEndTime));
            return modelMapper.map(entity, TimingDTO.class);
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
}
