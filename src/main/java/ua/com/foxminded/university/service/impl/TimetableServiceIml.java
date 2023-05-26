package ua.com.foxminded.university.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.TypeToken;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.dto.TimetableDTO;
import ua.com.foxminded.university.dto.TimingDTO;
import ua.com.foxminded.university.entity.Timetable;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.repository.TimetableRepository;
import ua.com.foxminded.university.service.TimetableService;

@Service
@Transactional
@RequiredArgsConstructor
public class TimetableServiceIml implements TimetableService {
    
    public static final Type TIMETABLE_MODELS_LIST_TYPE = 
            new TypeToken<List<TimetableDTO>>() {}.getType();
    
    private final TimetableRepository timetableRepository;
    private final ModelMapper modelMapper;
    
    public void sortByName(List<TimetableDTO> timetables) {
        Collections.sort(timetables, Comparator.comparing(TimetableDTO::getName));
    }
    
    @Override
    public void sortTimingsByStartTime(TimetableDTO timetable) {
        List<TimingDTO> list = new ArrayList<>(timetable.getTimings());
        Collections.sort(list, Comparator.comparing(TimingDTO::getStartTime));
        Set<TimingDTO> set = new LinkedHashSet<>(list);
        timetable.setTimings(set);
    }
    
    @Override
    public TimetableDTO getByIdWithTimings(int id) throws ServiceException {
        try {
            Timetable timetable = timetableRepository.getByIdWithTimings(id);
            return modelMapper.map(timetable, TimetableDTO.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Fetching the timetable with its timings list fails", e);
        }
    }

    @Override
    public List<TimetableDTO> getAllWithTimings() throws ServiceException {
        try {
            List<Timetable> entities = timetableRepository.getAllWithTimings();
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
    public TimetableDTO create(TimetableDTO timetable) throws ServiceException {
        try {
            Timetable entity = modelMapper.map(timetable, Timetable.class);
            Timetable createdEntity = timetableRepository.saveAndFlush(entity);
            return modelMapper.map(createdEntity, TimetableDTO.class);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException("Creating timetable fails", e);
        }
    }

    @Override
    public void update(TimetableDTO model) throws ServiceException {
        try {
            Timetable persistedEntity = timetableRepository.findById(
                    model.getId().intValue());
            Timetable entity = modelMapper.map(model, Timetable.class);
            persistedEntity.setName(model.getName());
            timetableRepository.saveAndFlush(entity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Updating timetable fils", e);
        }
    }

    @Override
    public TimetableDTO getById(int id) throws ServiceException {
        try {
            Timetable entity = timetableRepository.findById(id);
            return modelMapper.map(entity, TimetableDTO.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting the timetable by its id fails", e);
        }
    }

    @Override
    public List<TimetableDTO> getAll() throws ServiceException {
        try {
            List<Timetable> entities = timetableRepository.findAll();
            return modelMapper.map(entities, TIMETABLE_MODELS_LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all timetables fails", e);
        }
    }
}
