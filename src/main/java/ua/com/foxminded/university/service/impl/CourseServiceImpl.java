package ua.com.foxminded.university.service.impl;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.TeacherRepository;
import ua.com.foxminded.university.service.CourseService;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    
    private static final Type TYPE = new TypeToken<List<CourseModel>>() {}.getType();

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;
    
    @Override
    public void addTeacherToCourse(CourseModel courseModel) throws ServiceException {
        try {
            CourseEntity courseEntity = courseRepository.findById(
                    courseModel.getId().intValue());
            
            Set<TeacherEntity> teachers = courseModel.getTeachers().stream()
                    .map(teacher -> {
                        TeacherEntity teacherEntity = teacherRepository.findById(
                                teacher.getId().intValue());
                        teacherEntity.getCourses().add(courseEntity);
                        return teacherEntity;
                    })
                    .collect(Collectors.toSet());
            teacherRepository.saveAllAndFlush(teachers);
            
            courseEntity.getTeachers().addAll(teachers);
            courseRepository.saveAndFlush(courseEntity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Adding teacher to a course fails", e);
        }
    }
    
    @Override
    public CourseModel getTimetableAndTeachersByCourseId(int id) throws ServiceException {
        try {
            CourseEntity entity = courseRepository.getCourseWithDependencies(id);
            return modelMapper.map(entity, CourseModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Fetching the course with related "
                    + "timetables and teachers fails", e);
        }
    }
    
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
            return modelMapper.map(courseEntities, TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all courses was failed.", e); 
        }
    }
    
    @Override
    public void update(CourseModel model) throws ServiceException {
        try {
            CourseEntity entity = modelMapper.map(model, CourseEntity.class);
            CourseEntity persistedEntity = courseRepository.findById(entity.getId()
                                                           .intValue());
            persistedEntity.setDescription(entity.getDescription());
            persistedEntity.setName(entity.getName());
            courseRepository.saveAndFlush(persistedEntity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Updating the course was failed", e);
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
