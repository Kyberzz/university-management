package ua.com.foxminded.university.service.impl;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.service.ServiceException;
import ua.com.foxminded.university.service.StudentService;

@Service
@Slf4j
@Transactional
public class StudentServiceImpl implements StudentService<StudentModel> {
    
    private StudentRepository studentDao;
    
    @Autowired
    public StudentServiceImpl(StudentRepository studentDao) {
        this.studentDao = studentDao;
    }
    
    @Override
    public void updateStudent(StudentModel studentModel) throws ServiceException {
        ModelMapper modelMapper = new ModelMapper();
       
        try {
            StudentEntity studentEntity = modelMapper.map(studentModel, StudentEntity.class);
            studentDao.update(studentEntity);
        } catch (RepositoryException | IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Udating the student data failed.", e);
        }
    }
}
