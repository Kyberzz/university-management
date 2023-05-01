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
    
    private final TeacherRepository teacherRepository; 
    
    @Override
    public List<TeacherModel> getAll() throws ServiceException {
        try {
            List<TeacherEntity> teacherEntities = teacherRepository.findAll();
            Type listType = new TypeToken<List<TeacherModel>>() {}.getType();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(teacherEntities, listType);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all teachers was failed", e);
        }
    }
    
    @Override
    public TeacherModel getCourseListByTeacherId(int id) throws ServiceException {
        try {
            TeacherEntity teacherEntity = teacherRepository.findCoursesById(id);
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(teacherEntity, TeacherModel.class);
        } catch (IllegalArgumentException | ConfigurationException | 
                 MappingException e) {
            throw new ServiceException("Getting the courses list by the teacher id failed.", e);
        }
    }
}