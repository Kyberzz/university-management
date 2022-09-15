package ua.com.foxminded.university.service.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.service.ServiceException;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTestTestTest {
    
    @InjectMocks
    StudentServiceImpl studentService;
    
    @Mock
    StudentDao studentDaoMock;
    

    @Test
    void updateStudent_CallingInnerMethod_CorrectCallQuantity() throws ServiceException, DaoException {
        StudentModel studentModel = new StudentModel();
        studentModel.setGroup(new GroupModel());
        studentService.updateStudent(studentModel);
        verify(studentDaoMock, times(1)).update(ArgumentMatchers.<StudentEntity>any());
    }
}
