package ua.com.foxminded.university.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.service.ServiceException;
import ua.com.foxminded.university.service.StudentService;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService<StudentModel> {
    
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
            throw new ServiceException("Udating the student data failed.", e);
        }
        return updatedStudentsQuantity;
    }
}
