package ua.com.foxminded.university.service;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.repository.CourseRepository;


@Slf4j
@Transactional
@Service
public class CourseServiceImpl implements CourseService<CourseModel> {
    
    private CourseRepository courseRepository;
    
    @Autowired
    public CourseServiceImpl(CourseRepository courseDao) {
        this.courseRepository = courseDao;
    }
    
    @Override
    public List<CourseModel> getAllCourses() throws ServiceException {
        try {
            List<CourseEntity> courseEntities = courseRepository.findAll();
            Type listType = new TypeToken<List<CourseModel>>() {}.getType();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(courseEntities, listType);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all courses was failed.", e); 
        }
    }
    
    @Override
    public void updateCourse(CourseModel courseModel) throws ServiceException {
        ModelMapper modelMapper = new ModelMapper();
        
        try {
            CourseEntity courseEntity = modelMapper.map(courseModel, CourseEntity.class);
            courseRepository.save(courseEntity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Updating all courses was failed", e);
        }
    }
   
    @Override
    public CourseModel getTimetableListByCourseId(int id) throws ServiceException {
        CourseModel course = null;
        try {
            CourseEntity courseEntity = courseRepository.findTimetableListById(id);
            ModelMapper modelMapper = new ModelMapper();
            course = modelMapper.map(courseEntity, CourseModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timetable list of the course id was failed", e);
        }
        return course;
    }
}
