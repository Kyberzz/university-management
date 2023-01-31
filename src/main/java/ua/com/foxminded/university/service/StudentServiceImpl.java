package ua.com.foxminded.university.service;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.postgresql.gss.GSSOutputStream;
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
            return modelMapper.map(studentEntity, StudentModel.class);
        } catch (IllegalArgumentException | ConfigurationException | MappingException | 
                 RepositoryException e) {
            throw new ServiceException("Getting student by its id failed.", e);
        }
    }
    
    @Override
    public List<StudentModel> getAllStudentsIncludingEmails() throws ServiceException {
        try {
            List<StudentEntity> studentEntities = studentRepository.getAllStudentsIncludingEmails();
            ModelMapper modelMapper = new ModelMapper();
            Type listType = new TypeToken<List<StudentModel>>() {}.getType();
            return modelMapper.map(studentEntities, listType);
        } catch (IllegalArgumentException | ConfigurationException | 
                 MappingException | RepositoryException e) {
            throw new ServiceException("Getting all students was failed", e);
        }
    }
    
    @Override
    public void editStudent(StudentModel model) throws ServiceException {
        try {
            ModelMapper modelMapper = new ModelMapper();
            StudentEntity changedEntity = modelMapper.map(model, StudentEntity.class);
            StudentEntity originEntity = studentRepository.findById(model.getId());
            
            if (changedEntity.getUser() != null) {
                String email = changedEntity.getUser().getEmail();
                
                if (email.equals(EMPTY_STRING)) {
                    if (originEntity.getUser() != null) {
                        String originEmail = originEntity.getUser().getEmail();
                        changedEntity.getUser().setEmail(originEmail);
                    } else {
                        changedEntity.setUser(null);
                    }
                }
                if (email.equals(String.valueOf(ZERO))) {
                    changedEntity.setUser(null);
                }
            }
            
            if (changedEntity.getGroup() != null) {
                Integer groupId = changedEntity.getGroup().getId();
                
                if (groupId == null && originEntity.getGroup() != null) {
                        Integer originGroupId = originEntity.getGroup().getId();
                        changedEntity.getGroup().setId(originGroupId);
                } else if (groupId == null && originEntity.getGroup() == null) {
                    changedEntity.setGroup(null);
                } else if (groupId == ZERO) {
                    changedEntity.setGroup(null);
                } 
            }
            
            
            
           
            
            
            
            
            
            
            /*
            String firstName = changedEntity.getFirstName();
            
            if (firstName == null || firstName.equals(EMPTY_STRING)) {
                changedEntity.setFirstName(originEntity.getFirstName());
            }
            
            String lastName = changedEntity.getLastName();
            
            if (lastName == null || lastName.equals(EMPTY_STRING)) {
                changedEntity.setLastName(originEntity.getLastName());
            }
            
            if (changedEntity.getGroup() != null) {
                Integer groupId = changedEntity.getGroup().getId();
                
                if (groupId == 0) {
                    changedEntity.setGroup(null);
                }
                
                if (groupId == null) {
                    Integer originId = originEntity.getGroup().getId();
                    changedEntity.getGroup().setId(originId);
                }
            }
            
            if (changedEntity.getUser() != null) {
                String email = changedEntity.getUser().getEmail();
                
                if (email.equals(EMPTY_STRING)) {
                    String originEmail = originEntity.getUser().getEmail(); 
                    changedEntity.getUser().setEmail(originEmail);
                }
                
                if (email.equals(String.valueOf(ZERO))) {
                    changedEntity.setUser(null);
                }
            }
            */
            studentRepository.saveAndFlush(changedEntity);
        } catch (RepositoryException | IllegalArgumentException | ConfigurationException | 
                 MappingException e) {
            throw new ServiceException("Udating the student data failed.", e);
        }
    }
}
