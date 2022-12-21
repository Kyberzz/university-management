package ua.com.foxminded.university.buisness.model.service;

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
import ua.com.foxminded.university.buisness.entity.CourseEntity;
import ua.com.foxminded.university.buisness.entity.repository.CourseRepository;
import ua.com.foxminded.university.buisness.entity.repository.RepositoryException;
import ua.com.foxminded.university.buisness.model.CourseModel;


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
            return modelMapper.map(courseEntities, listType);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all courses was failed", e);
        }
    }
    
    @Override
    public void updateCourse(CourseModel courseModel) throws ServiceException {
        ModelMapper modelMapper = new ModelMapper();
        
        try {
            CourseEntity courseEntity = modelMapper.map(courseModel, CourseEntity.class);
            courseRepository.save(courseEntity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Updating the course failed.", e);
        }
    }
   
    @Override
    public CourseModel getTimetableListByCourseId(int id) throws ServiceException {
        try {
            CourseEntity courseEntity = courseRepository.findTimetableListById(id);
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(courseEntity, CourseModel.class);
        } catch (RepositoryException | IllegalArgumentException | ConfigurationException | 
                 MappingException e) {
            throw new ServiceException("Getting timetable list of course id faled.", e);
        }
    }
}
