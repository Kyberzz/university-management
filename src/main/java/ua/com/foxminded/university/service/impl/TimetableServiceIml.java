package ua.com.foxminded.university.service.impl;

import static ua.com.foxminded.university.exception.ServiceErrorCode.*;

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
import org.springframework.dao.DataIntegrityViolationException;
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
    
    public static final Type TIMETABLES_LIST_TYPE = 
            new TypeToken<List<TimetableDTO>>() {}.getType();
    
    private final TimetableRepository timetableRepository;
    private final ModelMapper modelMapper;
    
    @Override
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
    public TimetableDTO getByIdWithTimings(int id) {
        try {
            Timetable timetable = timetableRepository.getByIdWithTimings(id);
            return modelMapper.map(timetable, TimetableDTO.class);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(TIMETABLE_FETCH_ERROR, e);
        }
    }

    @Override
    public List<TimetableDTO> getAllWithTimings() {
        try {
            List<Timetable> entities = timetableRepository.getAllWithTimings();
            return modelMapper.map(entities, TIMETABLES_LIST_TYPE);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(TIMETABLE_FETCH_ERROR, e);
        }
    }
    
    @Override
    public void deleteById(Integer id) {
        try {
            timetableRepository.deleteById(id);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new ServiceException(TIMETABLE_DELETE_ERROR, e);
        }
    }

    @Override
    public TimetableDTO create(TimetableDTO timetableDto) {
        try {
            Timetable timetable = modelMapper.map(timetableDto, Timetable.class);
            Timetable createdEntity = timetableRepository.saveAndFlush(timetable);
            return modelMapper.map(createdEntity, TimetableDTO.class);
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException(TIMETABLE_NAME_DUPLICATION, e);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(TIMETABLE_PERSISTENCE_ERROR, e);
        }
    }

    @Override
    public void update(TimetableDTO dto) {
        try {
            Timetable persistedEntity = timetableRepository.findById(
                    dto.getId().intValue());
            Timetable entity = modelMapper.map(dto, Timetable.class);
            persistedEntity.setName(dto.getName());
            timetableRepository.saveAndFlush(entity);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(TIMETABLE_UPDATE_ERROR, e);
        }
    }

    @Override
    public TimetableDTO getById(int id) {
        try {
            Timetable entity = timetableRepository.findById(id);
            return modelMapper.map(entity, TimetableDTO.class);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(TIMETABLE_FETCH_ERROR, e);
        }
    }

    @Override
    public List<TimetableDTO> getAll() {
        try {
            List<Timetable> entities = timetableRepository.findAll();
            return modelMapper.map(entities, TIMETABLES_LIST_TYPE);
        } catch (DataAccessException | IllegalArgumentException | ConfigurationException | 
                 MappingException e) {
            throw new ServiceException(TIMETABLES_FETCH_ERROR, e);
        }
    }
}
