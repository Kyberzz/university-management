package ua.com.foxminded.university.service.impl;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.service.CourseService;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    
    private static final Type TYPE = new TypeToken<List<CourseModel>>() {}.getType();

    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    
    @Override
    public CourseModel getById(int id) throws ServiceException {
        try {
            CourseEntity entity = courseRepository.findById(id);
            return modelMapper.map(entity, CourseModel.class); 
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting a course by its id fails", e);
        }
    }
    
    @Override
    public void deleteById(Integer id) throws ServiceException {
        try {
            courseRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Deleting the course fails", e);
        }
    }
    
    @Override
    public void create(CourseModel model) throws ServiceException {
        try {
            CourseEntity entity = modelMapper.map(model, CourseEntity.class);
            courseRepository.saveAndFlush(entity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Creating the course fails", e);
        }
    }
    
    @Override
    public List<CourseModel> getAll() throws ServiceException {
        try {
            List<CourseEntity> courseEntities = courseRepository.findAll();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(courseEntities, TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all courses was failed.", e); 
        }
    }
    
    @Override
    public void update(CourseModel courseModel) throws ServiceException {
        try {
            CourseEntity courseEntity = modelMapper.map(courseModel, CourseEntity.class);
            courseRepository.save(courseEntity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Updating all courses was failed", e);
        }
    }
   
    @Override
    public CourseModel getTimetableListByCourseId(int id) throws ServiceException {
        try {
            CourseEntity courseEntity = courseRepository.findTimetablesById(id);
            return modelMapper.map(courseEntity, CourseModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timetable list of the course id was failed", e);
        }
    }
}
