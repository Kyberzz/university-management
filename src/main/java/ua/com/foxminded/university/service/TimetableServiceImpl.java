package ua.com.foxminded.university.service;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.repository.TimetableRepository;
import ua.com.foxminded.univesity.exception.RepositoryException;
import ua.com.foxminded.univesity.exception.ServiceException;


@Slf4j
@Transactional
@Service
public class TimetableServiceImpl implements TimetableService<TimetableModel> {
    
    private TimetableRepository timetableRepository;
    
    @Autowired
    public TimetableServiceImpl(TimetableRepository timetableDao) {
        this.timetableRepository = timetableDao;
    }
    
    @Override
    public List<TimetableModel> getAllTimetables() throws ServiceException {
        try {
            List<TimetableEntity> timetableEntities = timetableRepository.findAll();
            Type listType = new TypeToken<List<TimetableModel>>() {}.getType();
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(timetableEntities, listType);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all timetables was failed", e);
        }
    }
    
    @Override
    public void updateTimetable(TimetableModel timetableModel) throws ServiceException {
        ModelMapper modelMapper = new ModelMapper();
        
        try {
            TimetableEntity timetableEntity = modelMapper.map(timetableModel, TimetableEntity.class);
            timetableRepository.save(timetableEntity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Updating the timetable failed.", e);
        }
    }
}
