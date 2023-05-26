package ua.com.foxminded.university.service.impl;

import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.dto.CourseDTO;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Teacher;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.TeacherRepository;
import ua.com.foxminded.university.service.CourseService;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    
    public static final Type COURSE_MODEL_LIST_TYPE = 
            new TypeToken<List<CourseDTO>>() {}.getType();

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;
    
    @PersistenceUnit
    private  EntityManagerFactory entityManagerFactory;
    
    @Override
    public void deassignTeacherToCourse(int teacherId, int courseId) {
        
            Course course = courseRepository.findById(courseId);
            Teacher teacher = teacherRepository.findById(teacherId);
            course.removeTeacher(teacher);
            courseRepository.saveAndFlush(course);
    }
    
    @Override
    public void assignTeacherToCourse(int teacherId, int courseId) {
        Teacher teacher = teacherRepository.findById(teacherId);
        Course course = courseRepository.findById(courseId);
        course.addTeacher(teacher);
        courseRepository.saveAndFlush(course);
    }

    @Override
    public CourseDTO getByIdWithLessonsAndTeachers(int id) throws ServiceException {
        try {
            Course entity = courseRepository.getCourseRelationsById(id);
            return modelMapper.map(entity, CourseDTO.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Fetching the course with related "
                    + "timetables and teachers fails", e);
        }
    }
    
    @Override
    public CourseDTO getById(int id) throws ServiceException {
        try {
            Course entity = courseRepository.findById(id);
            return modelMapper.map(entity, CourseDTO.class); 
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
    public CourseDTO create(CourseDTO model) throws ServiceException {
        try {
            Course entity = modelMapper.map(model, Course.class);
            Course createdEntity = courseRepository.saveAndFlush(entity);
            return modelMapper.map(createdEntity, CourseDTO.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Creating the course fails", e);
        }
    }
    
    @Override
    public List<CourseDTO> getAll() throws ServiceException {
        try {
            List<Course> courseEntities = courseRepository.findAll();
            return modelMapper.map(courseEntities, COURSE_MODEL_LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all courses was failed.", e); 
        }
    }
    
    @Override
    public void update(CourseDTO model) throws ServiceException {
        try {
            Course entity = modelMapper.map(model, Course.class);
            Course persistedEntity = courseRepository.findById(entity.getId()
                                                           .intValue());
            persistedEntity.setDescription(entity.getDescription());
            persistedEntity.setName(entity.getName());
            courseRepository.saveAndFlush(persistedEntity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Updating the course was failed", e);
        }
    }
   
    @Override
    public CourseDTO getByIdWithLessons(int id) throws ServiceException {
        try {
            Course courseEntity = courseRepository.findTimetablesById(id);
            return modelMapper.map(courseEntity, CourseDTO.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timetable list of the course id was failed", e);
        }
    }
}