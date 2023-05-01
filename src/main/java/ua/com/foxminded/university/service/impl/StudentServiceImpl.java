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
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.repository.UserRepository;
import ua.com.foxminded.university.service.StudentService;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    
    private static final Type LIST_TYPE = new TypeToken<List<StudentModel>>() {}.getType();
    
    private final ModelMapper modelMapper;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    
    @Override
    public void deleteById(Integer id) throws ServiceException {
        try {
            studentRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Deleting a student fails", e);
        }
    }
    
    @Override
    public void update(StudentModel studentModel) throws ServiceException {
        try {
            StudentEntity studentEntity = modelMapper.map(studentModel, StudentEntity.class);
            StudentEntity persistedStudent = studentRepository.findById(studentEntity.getId());
            
            if (studentModel.hasGroup()) {
                GroupEntity persistedGroup = groupRepository.findById(
                        studentModel.getGroup().getId().intValue());
                persistedStudent.setGroup(persistedGroup);
            } else {
                persistedStudent.setGroup(null);
            }
            
            UserEntity persistedUser = userRepository.findById(
                    persistedStudent.getUser().getId().intValue());
            persistedUser.getPerson().setFirstName(
                    studentModel.getUser().getPerson().getFirstName());
            persistedUser.getPerson().setLastName(
                    studentModel.getUser().getPerson().getLastName());
            persistedUser.setEmail(studentModel.getUser().getEmail());
            persistedStudent.setUser(persistedUser);
            studentRepository.saveAndFlush(persistedStudent);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("The student data was not updated", e);
        }
    }
    
    @Override 
    public StudentModel getById(int id) throws ServiceException {
        try {
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            StudentEntity studentEntity = studentRepository.findById(id);
            return modelMapper.map(studentEntity, StudentModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting a student by its id fails", e);
        }
    }
    
    @Override
    public List<StudentModel> getAll() throws ServiceException {
        try {
            List<StudentEntity> studentEntities = studentRepository.findAll();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(studentEntities, LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all students fails", e);
        }
    }
    
    @Override
    public void create(StudentModel model) throws ServiceException {
        try {
            StudentEntity entity = modelMapper.map(model, StudentEntity.class);
            studentRepository.saveAndFlush(entity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Creating a student fails", e);
        }
    }
}