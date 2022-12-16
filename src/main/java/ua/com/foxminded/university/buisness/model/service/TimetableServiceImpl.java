package ua.com.foxminded.university.buisness.model.service;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.buisness.entity.TimetableEntity;
import ua.com.foxminded.university.buisness.entity.repository.RepositoryException;
import ua.com.foxminded.university.buisness.entity.repository.TimetableRepository;
import ua.com.foxminded.university.buisness.model.TimetableModel;


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
