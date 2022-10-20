package ua.com.foxminded.university.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.service.ServiceException;
import ua.com.foxminded.university.service.StudentService;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService<StudentModel> {
    
    private StudentRepository studentDao;
    
    @Autowired
    public StudentServiceImpl(StudentRepository studentDao) {
        this.studentDao = studentDao;
    }
    
    @Override
    @Transactional
    public int updateStudent(StudentModel studentModel) throws ServiceException {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(studentModel.getId());
        studentEntity.setFirstName(studentModel.getFirstName());
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(studentModel.getGroup().getId());
        studentEntity.setGroup(groupEntity);
        studentEntity.setLastName(studentModel.getLastName());
        int updatedStudentsQuantity = 0;
       
        try {
            studentDao.update(studentEntity);
        } catch (RepositoryException e) {
            throw new ServiceException("Udating the student data failed.", e);
        }
        return updatedStudentsQuantity;
    }
}
