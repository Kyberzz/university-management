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
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.repository.TimetableRepository;
import ua.com.foxminded.university.service.TimetableService;

@Service
@Transactional
@RequiredArgsConstructor
public class TimetableServiceIml implements TimetableService {
    
    public static final Type TIMETABLE_MODELS_LIST_TYPE = 
            new TypeToken<List<TimetableModel>>() {}.getType();
    
    private final TimetableRepository timetableRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<TimetableModel> getAllWithTimings() throws ServiceException {
        try {
            List<TimetableEntity> entities = timetableRepository.getAllWithTimings();
            return modelMapper.map(entities, TIMETABLE_MODELS_LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timetables with timings relationships fails", e);
        }
    }
    
    @Override
    public void deleteById(Integer id) throws ServiceException {
        try {
            timetableRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Deleting the timetable by its id fails", e);
        }
    }

    @Override
    public void create(TimetableModel model) throws ServiceException {
        try {
            TimetableEntity entity = modelMapper.map(model, TimetableEntity.class);
            timetableRepository.saveAndFlush(entity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Creating timetable fails", e);
        }
    }

    @Override
    public void update(TimetableModel model) throws ServiceException {
        try {
            TimetableEntity persistedEntity = timetableRepository.findById(
                    model.getId().intValue());
            TimetableEntity entity = modelMapper.map(model, TimetableEntity.class);
            persistedEntity.setName(model.getName());
            timetableRepository.saveAndFlush(entity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Updating timetable fils", e);
        }
    }

    @Override
    public TimetableModel getById(int id) throws ServiceException {
        try {
            TimetableEntity entity = timetableRepository.findById(id);
            return modelMapper.map(entity, TimetableModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting the timetable by its id fails", e);
        }
    }

    @Override
    public List<TimetableModel> getAll() throws ServiceException {
        try {
            List<TimetableEntity> entities = timetableRepository.findAll();
            return modelMapper.map(entities, TIMETABLE_MODELS_LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all timetables fails", e);
        }
    }
}
