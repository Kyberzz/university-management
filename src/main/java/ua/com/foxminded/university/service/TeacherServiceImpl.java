package ua.com.foxminded.university.service;

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
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.repository.TeacherRepository;


@Slf4j
@Transactional
@Service
public class TeacherServiceImpl implements TeacherService<TeacherModel> {
    
    private TeacherRepository teacherRepository; 
    
    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherDao) {
        this.teacherRepository = teacherDao;
    }
    
    @Override
    public List<TeacherModel> getAllTeachers() throws ServiceException {
        try {
            List<TeacherEntity> teacherEntities = teacherRepository.findAll();
            Type listType = new TypeToken<List<TeacherModel>>() {}.getType();
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(teacherEntities, listType);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all teachers was failed", e);
        }
    }
    
    @Override
    public TeacherModel getCourseListByTeacherId(int id) throws ServiceException {
        try {
            TeacherEntity teacherEntity = teacherRepository.findCourseListById(id);
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(teacherEntity, TeacherModel.class);
        } catch (IllegalArgumentException | ConfigurationException | 
                 MappingException e) {
            throw new ServiceException("Getting the courses list by the teacher id failed.", e);
        }
    }
}
