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
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.repository.StudentRepository;

@Slf4j
@Transactional
@Service
public class StudentServiceImpl implements StudentService<StudentModel> {
    
    public static final int ZERO = 0;
    public static final String EMPTY_STRING = "";
    
    private StudentRepository studentRepository;
    
    @Autowired
    public StudentServiceImpl(StudentRepository studentDao) {
        this.studentRepository = studentDao;
    }
    
    @Override
    public void deleteStudentById(int id) throws ServiceException {
        try {
            studentRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Deleting student failed", e);
        }
    }
    
    @Override
    public void updateStudent(StudentModel studentModel) throws ServiceException {
        try {
            ModelMapper modelMapper = new ModelMapper();
            StudentEntity studentEntity = modelMapper.map(studentModel, StudentEntity.class);
            studentRepository.save(studentEntity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("The student was not added to the database", e);
        }
    }
    
    @Override 
    public StudentModel getStudentById(int id) throws ServiceException {
        ModelMapper modelMapper = new ModelMapper();
        
        try {
            StudentEntity studentEntity = studentRepository.findById(id);
            return modelMapper.map(studentEntity, StudentModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException | RepositoryException e) {
            throw new ServiceException("Getting student by its id failed.", e);
        }
    }
    
    @Override
    public List<StudentModel> getAllStudentsIncludingEmails() throws ServiceException {
        try {
            List<StudentEntity> studentEntities = studentRepository.findAll();
            ModelMapper modelMapper = new ModelMapper();
            Type listType = new TypeToken<List<StudentModel>>() {}.getType();
            return modelMapper.map(studentEntities, listType);
        } catch (IllegalArgumentException | ConfigurationException | 
                 MappingException e) {
            throw new ServiceException("Getting all students was failed", e);
        }
    }
    
    @Override
    public void addStudent(StudentModel model) throws ServiceException {
        try {
            ModelMapper modelMapper = new ModelMapper();
            StudentEntity entity = modelMapper.map(model, StudentEntity.class);
            studentRepository.saveAndFlush(entity);
        } catch (IllegalArgumentException | ConfigurationException | 
                 MappingException e) {
            throw new ServiceException("Udating the student data failed.", e);
        }
    }
}
