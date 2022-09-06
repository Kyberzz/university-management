package ua.com.foxminded.university.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService<StudentModel> {
    
    private StudentDao studentDao;
    
    @Autowired
    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }
    
    @Override
    public int updateStudent(StudentModel studentModel) {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setFirstName(studentModel.getFirstName());
        studentEntity.setGroup(new GroupEntity(studentModel.getGroup().getId()));
        studentEntity.setId(studentModel.getId());
        studentEntity.setLastName(studentModel.getLastName());
        return studentDao.update(studentEntity);
    }
}
