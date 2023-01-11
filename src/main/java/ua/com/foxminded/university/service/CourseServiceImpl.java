package ua.com.foxminded.university.service;

import static ua.com.foxminded.univesity.exception.ExceptionMessage.*;

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
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.univesity.exception.RepositoryException;
import ua.com.foxminded.univesity.exception.ServiceException;


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
        List<CourseModel> courses = null;
       
        try {
            List<CourseEntity> courseEntities = courseRepository.findAll();
            Type listType = new TypeToken<List<CourseModel>>() {}.getType();
            ModelMapper modelMapper = new ModelMapper();
            courses = modelMapper.map(courseEntities, listType);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            selectErrorMessage(e);
        }
        return courses;
    }
    
    @Override
    public void updateCourse(CourseModel courseModel) throws ServiceException {
        ModelMapper modelMapper = new ModelMapper();
        
        try {
            CourseEntity courseEntity = modelMapper.map(courseModel, CourseEntity.class);
            courseRepository.save(courseEntity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            selectErrorMessage(e);
        }
    }
   
    @Override
    public CourseModel getTimetableListByCourseId(int id) throws ServiceException {
        CourseModel course = null;
        try {
            CourseEntity courseEntity = courseRepository.findTimetableListById(id);
            ModelMapper modelMapper = new ModelMapper();
            course = modelMapper.map(courseEntity, CourseModel.class);
        } catch (RepositoryException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            selectErrorMessage(e);
        }
        return course;
    }
    
    private void selectErrorMessage(Exception e) throws ServiceException {
        if (e.getClass() == IllegalArgumentException.class) {
            throw new ServiceException(ILLEGAL_MODELMAPPER_ARGUMENT, e);
        } else if (e.getClass() == ConfigurationException.class) {
            throw new ServiceException(CONFIGURATION_MODELMAPPER_EXCEPTION, e);
        } else if (e.getClass() == MappingException.class) {
            throw new ServiceException(MAPPING_FAILURE_OPERATION, e);
        } else if (e.getClass() == RepositoryException.class) {
            throw new ServiceException(REPOSITORY_FAILURE, e);
        }
    }
}
