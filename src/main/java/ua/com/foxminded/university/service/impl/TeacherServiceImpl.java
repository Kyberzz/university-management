package ua.com.foxminded.university.service.impl;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.TeacherRepository;
import ua.com.foxminded.university.service.ServiceException;
import ua.com.foxminded.university.service.TeacherService;


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
    public TeacherModel getCourseListByTeacherId(int id) throws ServiceException {
        try {
            TeacherEntity teacherEntity = teacherRepository.findCourseListById(id);
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(teacherEntity, TeacherModel.class);
        } catch (RepositoryException | IllegalArgumentException | ConfigurationException | 
                 MappingException e) {
            throw new ServiceException("Getting the courses list by the teacher id failed.", e);
        }
    }
}
