package ua.com.foxminded.university.service.impl;

import static ua.com.foxminded.university.exception.ErrorCode.*;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.dao.DataAccessException;
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
    
    @Override
    public List<CourseDTO> getByTeacherId(int teacherId) {
        try {
            List<Course> courses = courseRepository.findByTeachersId(teacherId);
            return modelMapper.map(courses, COURSE_MODEL_LIST_TYPE);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(COURSE_FETCH_ERROR, e);
        }
    }
    
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
    public CourseDTO getByIdWithLessonsAndTeachers(int id) {
        try {
            Course entity = courseRepository.findTeachersAndLessonsById(id);
            return modelMapper.map(entity, CourseDTO.class);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(COURSE_FETCH_ERROR, e);
        }
    }
    
    @Override
    public CourseDTO getById(int id) {
        try {
            Course entity = courseRepository.findById(id);
            return modelMapper.map(entity, CourseDTO.class); 
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(COURSE_FETCH_ERROR, e);
        }
    }
    
    @Override
    public void deleteById(Integer id) {
        try {
            courseRepository.deleteById(id);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new ServiceException(COURSE_DELETE_ERROR, e);
        }
    }
    
    @Override
    public CourseDTO create(CourseDTO model) {
        try {
            Course entity = modelMapper.map(model, Course.class);
            Course createdEntity = courseRepository.saveAndFlush(entity);
            return modelMapper.map(createdEntity, CourseDTO.class);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(COURSE_CREATE_ERROR, e);
        }
    }
    
    @Override
    public List<CourseDTO> getAll() {
        try {
            List<Course> courseEntities = courseRepository.findAll();
            return modelMapper.map(courseEntities, COURSE_MODEL_LIST_TYPE);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(COURSES_FETCH_ERROR, e); 
        }
    }
    
    @Override
    public void update(CourseDTO model) {
        try {
            Course entity = modelMapper.map(model, Course.class);
            Course persistedEntity = courseRepository.findById(entity.getId()
                                                           .intValue());
            persistedEntity.setDescription(entity.getDescription());
            persistedEntity.setName(entity.getName());
            courseRepository.saveAndFlush(persistedEntity);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(COURSE_UPDATE_ERROR, e);
        }
    }
   
    @Override
    public CourseDTO getByIdWithLessons(int id) {
        try {
            Course courseEntity = courseRepository.findTimetablesById(id);
            return modelMapper.map(courseEntity, CourseDTO.class);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(COURSE_FETCH_ERROR, e);
        }
    }
}
