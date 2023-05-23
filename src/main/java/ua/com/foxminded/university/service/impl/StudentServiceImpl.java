package ua.com.foxminded.university.service.impl;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.dto.StudentDTO;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.repository.UserRepository;
import ua.com.foxminded.university.service.StudentService;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    
    public static final Type STUDENT_MODEL_LIST_TYPE = 
            new TypeToken<List<StudentDTO>>() {}.getType();
    
    private final ModelMapper modelMapper;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    
    @Override 
    public void sortByLastName(List<StudentDTO> students) {
        Collections.sort(students, Comparator.comparing(student -> student.getUser()
                                                                          .getPerson()
                                                                          .getLastName()));
    }
    
    @Override
    public void deleteById(Integer id) throws ServiceException {
        try {
            studentRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Deleting a student fails", e);
        }
    }
    
    @Override
    public void update(StudentDTO studentModel) throws ServiceException {
        try {
            Student studentEntity = modelMapper.map(studentModel, Student.class);
            Student persistedStudent = studentRepository.findById(studentEntity.getId());
            
            if (studentModel.hasGroup()) {
                int groupId = studentModel.getGroup().getId();
                Group persistedGroup = groupRepository.findById(groupId);
                persistedStudent.setGroup(persistedGroup);
            } else {
                persistedStudent.setGroup(null);
            }
            
            User persistedUser = userRepository.findById(
                    persistedStudent.getUser().getId().intValue());
            String firstName = studentModel.getUser().getPerson().getFirstName();
            persistedUser.getPerson().setFirstName(firstName);
            String lastName = studentModel.getUser().getPerson().getLastName();
            persistedUser.getPerson().setLastName(lastName);
            persistedUser.setEmail(studentModel.getUser().getEmail());
            persistedStudent.setUser(persistedUser);
            studentRepository.saveAndFlush(persistedStudent);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("The student data was not updated", e);
        }
    }
    
    @Override 
    public StudentDTO getById(int id) throws ServiceException {
        try {
            Student studentEntity = studentRepository.findById(id);
            return modelMapper.map(studentEntity, StudentDTO.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting a student by its id fails", e);
        }
    }
    
    @Override
    public List<StudentDTO> getAll() throws ServiceException {
        try {
            List<Student> studentEntities = studentRepository.findAll();
            return modelMapper.map(studentEntities, STUDENT_MODEL_LIST_TYPE);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all students fails", e);
        }
    }
    
    @Override
    public void create(StudentDTO model) throws ServiceException {
        try {
            Student entity = modelMapper.map(model, Student.class);
            studentRepository.saveAndFlush(entity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Creating a student fails", e);
        }
    }
}
