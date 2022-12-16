package ua.com.foxminded.university.buisness.model.service;

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
import ua.com.foxminded.university.buisness.entity.StudentEntity;
import ua.com.foxminded.university.buisness.entity.repository.RepositoryException;
import ua.com.foxminded.university.buisness.entity.repository.StudentRepository;
import ua.com.foxminded.university.buisness.model.StudentModel;


@Slf4j
@Transactional
@Service
public class StudentServiceImpl implements StudentService<StudentModel> {
    
    private StudentRepository studentRepository;
    
    @Autowired
    public StudentServiceImpl(StudentRepository studentDao) {
        this.studentRepository = studentDao;
    }
    
    @Override
    public void addStudent(StudentModel studentModel) throws ServiceException {
        ModelMapper modelMapper = new ModelMapper();
        
        try {
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
            StudentModel studentModel = modelMapper.map(studentEntity, StudentModel.class);
            return studentModel;
        } catch (RepositoryException e) {
            throw new ServiceException("Getting student by its id failed.", e);
        }
    }
    
    @Override
    public List<StudentModel> getAllStudents() {
        ModelMapper modelMapper = new ModelMapper();

        List<StudentEntity> studentEntities = studentRepository.findAll();
        Type listType = new TypeToken<List<StudentModel>>() {}.getType();
        List<StudentModel> studentModels = modelMapper.map(studentEntities, listType);
        return studentModels;
    }
    
    @Override
    public void updateStudent(StudentModel studentModel) throws ServiceException {
        ModelMapper modelMapper = new ModelMapper();
       
        try {
            StudentEntity studentEntity = modelMapper.map(studentModel, StudentEntity.class);
            studentRepository.save(studentEntity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Udating the student data failed.", e);
        }
    }
}
