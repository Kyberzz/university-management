package ua.com.foxminded.university.service.impl;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.TypeToken;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.entity.TimingEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.TimingModel;
import ua.com.foxminded.university.repository.TimingRepository;
import ua.com.foxminded.university.service.TimingService;

@Service
@Transactional
@RequiredArgsConstructor
public class TimingServiceImpl implements TimingService {
    
    public static final Type LESSON_TIMING_LIST_TYPE = 
            new TypeToken<List<TimingModel>>() {}.getType();
    
    private final ModelMapper modelMapper;
    private final TimingRepository timingRepository;

    @Override
    public void deleteById(Integer id) throws ServiceException {
        try {
            timingRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Deleting the LessonTiming by its id fails", e);
        }
    }

    @Override
    public void create(TimingModel model) throws ServiceException {
        try {
            TimingEntity entity = modelMapper.map(model, TimingEntity.class);
            timingRepository.saveAndFlush(entity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Creating timing fails", e);
        }
    }

    @Override
    public void update(TimingModel model) throws ServiceException {
        try {
            TimingEntity persistedEntity = timingRepository.findById(
                    model.getId().intValue());
            TimingEntity entity = modelMapper.map(model, TimingEntity.class);
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
    public TimingModel getById(int id) throws ServiceException {
        try {
            TimingEntity entity = timingRepository.findById(id);
            return modelMapper.map(entity, TimingModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting the LessonTiming by its fails", e);
        }
    }

    @Override
    public List<TimingModel> getAll() throws ServiceException {
        try {
            List<TimingEntity> lessonsTiming = timingRepository.findAll();
            return modelMapper.map(lessonsTiming, LESSON_TIMING_LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting lessons timing fails", e);
        }
    }
}
