package ua.com.foxminded.university.service.impl;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.TimetableRepository;
import ua.com.foxminded.university.service.ServiceException;
import ua.com.foxminded.university.service.TimetableService;

@Service
@Slf4j
@Transactional
public class TimetableServiceImpl implements TimetableService<TimetableModel> {
    
    private TimetableRepository timetableDao;
    
    @Autowired
    public TimetableServiceImpl(TimetableRepository timetableDao) {
        this.timetableDao = timetableDao;
    }
    
    @Override
    public void updateTimetable(TimetableModel timetableModel) throws ServiceException {
        ModelMapper modelMapper = new ModelMapper();
        
        try {
            TimetableEntity timetableEntity = modelMapper.map(timetableModel, TimetableEntity.class);
            timetableDao.update(timetableEntity);
        } catch (RepositoryException | IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Updating the timetable failed.", e);
        }
    }
}
