package ua.com.foxminded.university.service.impl;

import static ua.com.foxminded.university.exception.ServiceErrorCode.*;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.dto.TeacherDTO;
import ua.com.foxminded.university.entity.Teacher;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.repository.TeacherRepository;
import ua.com.foxminded.university.service.TeacherService;

@Service
@Transactional
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    
    public static final Type TEACHER_MODEL_LIST_TYPE = 
            new TypeToken<List<TeacherDTO>>() {}.getType();
    
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;
    
    @Override
    public TeacherDTO getTeacherByEmail(String email) {
        try {
            Teacher teacher = teacherRepository.findByUserEmail(email);
            return modelMapper.map(teacher, TeacherDTO.class);
        } catch (DataAccessException e) {
            throw new ServiceException(TEACHER_FETCH_ERROR, e);
        }
    }
    
    @Override
    public TeacherDTO getByUserId(int id) {
        try {
            Teacher teacher = teacherRepository.findByUserId(id);
            return modelMapper.map(teacher, TeacherDTO.class);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(TEACHER_FETCH_ERROR, e);
        }
    }
    
    @Override
    public List<TeacherDTO> getAll() {
        try {
            List<Teacher> entities = teacherRepository.findAll();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(entities, TEACHER_MODEL_LIST_TYPE);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException(TEACHERS_FETCH_ERROR, e);
        }
    }
    
    @Override
    public TeacherDTO getByIdWithCourses(int id) {
        try {
            Teacher entity = teacherRepository.findCoursesById(id);
            return modelMapper.map(entity, TeacherDTO.class);
        } catch (DataAccessException | IllegalArgumentException | ConfigurationException | 
                 MappingException e) {
            throw new ServiceException(TEACHER_FETCH_ERROR, e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            teacherRepository.deleteById(id);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new ServiceException(TEACHER_DELETE_ERROR, e);
        }
    }

    @Override
    public TeacherDTO create(TeacherDTO model) throws ServiceException {
        try {
            Teacher teacher = modelMapper.map(model, Teacher.class);
            Teacher persistedTeacher = teacherRepository.saveAndFlush(teacher);
            return modelMapper.map(persistedTeacher, TeacherDTO.class);
        } catch (DataAccessException | IllegalArgumentException | ConfigurationException | 
                 MappingException e) {
            throw new ServiceException(TEACHER_CREATE_ERROR, e);
        }
    }

    @Override
    public void update(TeacherDTO model) {
        try {
            Teacher persistedTeacher = teacherRepository.findById(model.getId().intValue());
            Teacher teacher = modelMapper.map(model, Teacher.class);
            persistedTeacher.setUser(teacher.getUser());
        } catch (DataAccessException | IllegalArgumentException | ConfigurationException | 
                 MappingException e) {
            throw new ServiceException(TEACHER_UPDATE_ERORR, e);
        }
    }

    @Override
    public TeacherDTO getById(int id) {
        try {
            Teacher teacher = teacherRepository.findById(id);
            return modelMapper.map(teacher, TeacherDTO.class);
        } catch (DataAccessException | IllegalArgumentException | ConfigurationException | 
                 MappingException e) {
            throw new ServiceException(TEACHER_FETCH_ERROR, e);
        }
    }

    @Override
    public List<TeacherDTO> getByCoursesId(int id) throws ServiceException {
        try {
            List<Teacher> teachers = teacherRepository.findByCoursesId(id);
            return modelMapper.map(teachers, TEACHER_MODEL_LIST_TYPE);
        } catch (DataAccessException | IllegalArgumentException | ConfigurationException | 
                 MappingException e) {
            throw new ServiceException(TEACHER_FETCH_ERROR, e);
        }
    }
}
