package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.entity.StudentEntity;

public interface StudentDao {
    
    public StudentEntity getStudentById(int id);
}
