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
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.repository.TeacherRepository;
import ua.com.foxminded.university.service.TeacherService;

@Service
@Transactional
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    
    public static final Type TEACHER_MODEL_LIST_TYPE = 
            new TypeToken<List<TeacherModel>>() {}.getType();
    private final TeacherRepository teacherRepository; 
    
    @Override
    public List<TeacherModel> getAll() throws ServiceException {
        try {
            List<TeacherEntity> entities = teacherRepository.findAll();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(entities, TEACHER_MODEL_LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all teachers was failed", e);
        }
    }
    
    @Override
    public TeacherModel getByIdWithCourses(int id) throws ServiceException {
        try {
            TeacherEntity entity = teacherRepository.findCoursesById(id);
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(entity, TeacherModel.class);
        } catch (IllegalArgumentException | ConfigurationException | 
                 MappingException e) {
            throw new ServiceException("Getting the courses list by the teacher id failed.", e);
        }
    }
}