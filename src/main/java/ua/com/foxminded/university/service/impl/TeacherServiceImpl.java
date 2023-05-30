package ua.com.foxminded.university.service.impl;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

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
    public TeacherDTO getByUserId(int id) throws ServiceException {
        try {
            Teacher teacher = teacherRepository.findByUserId(id);
            return modelMapper.map(teacher, TeacherDTO.class);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException("Getting teacher by the user's id fails", e);
        }
    }
    
    @Override
    public List<TeacherDTO> getAll() throws ServiceException {
        try {
            List<Teacher> entities = teacherRepository.findAll();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(entities, TEACHER_MODEL_LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all teachers was failed", e);
        }
    }
    
    @Override
    public TeacherDTO getByIdWithCourses(int id) throws ServiceException {
        try {
            Teacher entity = teacherRepository.findCoursesById(id);
            return modelMapper.map(entity, TeacherDTO.class);
        } catch (IllegalArgumentException | ConfigurationException | 
                 MappingException e) {
            throw new ServiceException("Getting the courses list by the teacher id failed.", e);
        }
    }
}