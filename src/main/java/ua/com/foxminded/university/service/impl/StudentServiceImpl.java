package ua.com.foxminded.university.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.service.ServiceException;
import ua.com.foxminded.university.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService<StudentModel> {
    
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    
    private StudentDao studentDao;
    
    @Autowired
    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }
    
    @Override
    public int updateStudent(StudentModel studentModel) throws ServiceException {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setFirstName(studentModel.getFirstName());
        studentEntity.setGroup(new GroupEntity(studentModel.getGroup().getId()));
        studentEntity.setId(studentModel.getId());
        studentEntity.setLastName(studentModel.getLastName());
        int updatedStudentsQuantity = 0;
       
        try {
            updatedStudentsQuantity = studentDao.update(studentEntity);
        } catch (DaoException e) {
            String errorMessage = "Udating the student data failed.";
            logger.error(errorMessage, e);
            throw new ServiceException(errorMessage, e);
        }
        return updatedStudentsQuantity;
    }
}
