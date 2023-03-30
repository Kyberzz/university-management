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
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.service.StudentService;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService<StudentModel> {
    
    private static final Type LIST_TYPE = new TypeToken<List<StudentModel>>() {}.getType();
    
    private final ModelMapper modelMapper;
    private final StudentRepository studentRepository;
    
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
            StudentEntity studentEntity = modelMapper.map(studentModel, StudentEntity.class);
            studentRepository.save(studentEntity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("The student was not added to the database", e);
        }
    }
    
    @Override 
    public StudentModel getStudentById(int id) throws ServiceException {
        try {
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            StudentEntity studentEntity = studentRepository.findById(id);
            return modelMapper.map(studentEntity, StudentModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting a student by its id failed.", e);
        }
    }
    
    @Override
    public List<StudentModel> getAllStudentsIncludingEmails() throws ServiceException {
        try {
            List<StudentEntity> studentEntities = studentRepository.findAll();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(studentEntities, LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | 
                 MappingException e) {
            throw new ServiceException("Getting all students was failed", e);
        }
    }
    
    @Override
    public void addStudent(StudentModel model) throws ServiceException {
        try {
            StudentEntity entity = modelMapper.map(model, StudentEntity.class);
            studentRepository.saveAndFlush(entity);
        } catch (IllegalArgumentException | ConfigurationException | 
                 MappingException e) {
            throw new ServiceException("Udating the student data failed.", e);
        }
    }
}
