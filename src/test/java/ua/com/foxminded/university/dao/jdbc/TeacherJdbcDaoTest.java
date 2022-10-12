package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManagerFactory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.university.config.TestAppConfig;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.entity.TeacherEntity;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = TestAppConfig.class)
@ExtendWith(SpringExtension.class)
class TeacherJdbcDaoTest {
    
    private static final String COURSE_DESCRIPTION = "some description";
    private static final String COURSE_NAME = "Programming";
    private static final String TEACHER_LAST_NAME = "Ritchie";
    private static final String TEACHER_FIRST_NAME = "Dennis";
    private static final int NEW_TEACHER_ID = 3;
    private static final int TEACHER_ID = 2;
    private static final int COURSE_ID_NUMBER = 2;
    private static final int FIST_ELEMENT = 0;
    private static final int COURSES_QUANTITY = 2;
    private static final int TEACHER_ID_NUMBER = 2;
    
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Test
    void insert_InsertingTeacherDatabaseData_DatabaseHasCorrectData() throws DaoException {
        TeacherEntity teacher = new TeacherEntity();
        teacher.setFirstName(TEACHER_FIRST_NAME);
        teacher.setLastName(TEACHER_LAST_NAME);
        TeacherDao teacherDao = new TeacherJdbcDao(entityManagerFactory);
        TeacherEntity teacherWithId = teacherDao.insert(teacher);

        TeacherEntity insertedTeacher = entityManagerFactory.createEntityManager()
                                                            .find(TeacherEntity.class, NEW_TEACHER_ID);
        
        assertEquals(NEW_TEACHER_ID, teacherWithId.getId());
        assertEquals(TEACHER_FIRST_NAME, insertedTeacher.getFirstName());
        assertEquals(TEACHER_LAST_NAME, insertedTeacher.getLastName());
    }
    
    @Test
    void getById_ReceivingTeacherDatabaseData_CorrectReceivedData() throws DaoException {
        TeacherDao teacherDao = new TeacherJdbcDao(entityManagerFactory);
        TeacherEntity teacher = teacherDao.getById(TEACHER_ID_NUMBER);
        
        assertEquals(TEACHER_FIRST_NAME, teacher.getFirstName());
        assertEquals(TEACHER_LAST_NAME, teacher.getLastName());
        assertEquals(TEACHER_ID_NUMBER, teacher.getId());
    }
    
    @Test
    void update_UpdatingTeacherDatabaseData_DatabaseHasCorrectData() throws DaoException {
        TeacherDao teacherDao = new TeacherJdbcDao(entityManagerFactory);
        TeacherEntity teacherData = new TeacherEntity();
        teacherData.setId(TEACHER_ID);
        teacherData.setFirstName(TEACHER_FIRST_NAME);
        teacherData.setLastName(TEACHER_LAST_NAME);
        teacherDao.update(teacherData);
        
        TeacherEntity updatedTeacherData = entityManagerFactory.createEntityManager()
                                                               .find(TeacherEntity.class, TEACHER_ID);
        
        assertEquals(TEACHER_ID, updatedTeacherData.getId());
        assertEquals(TEACHER_FIRST_NAME, updatedTeacherData.getFirstName());
        assertEquals(TEACHER_LAST_NAME, updatedTeacherData.getLastName());
    }
    
    @Test
    void deleteById_DeletingTeacherDatabaseData_DatabaseHasNoData() throws DaoException {
        TeacherDao teacherDao = new TeacherJdbcDao(entityManagerFactory);
        teacherDao.deleteById(TEACHER_ID_NUMBER);
        TeacherEntity teacher = new TeacherEntity();
        teacher.setId(TEACHER_ID_NUMBER);
        
        boolean persisentTeacher = entityManagerFactory.createEntityManager()
                                                       .contains(teacher);
        assertFalse(persisentTeacher);
    }
    
    @Test
    void getCourseListByTeacherId_ReceivingTeacherDatabaseData_CorrectReceivedData() throws DaoException {
        TeacherDao teacherDao = new TeacherJdbcDao(entityManagerFactory);
        TeacherEntity teacherData = teacherDao.getCourseListByTeacherId(TEACHER_ID_NUMBER);
        
        assertEquals(TEACHER_ID_NUMBER, teacherData.getId());
        assertEquals(TEACHER_FIRST_NAME, teacherData.getFirstName());
        assertEquals(TEACHER_LAST_NAME, teacherData.getLastName());
        assertEquals(TEACHER_ID_NUMBER, teacherData.getCourseList().get(FIST_ELEMENT).getTeacher().getId());
        assertEquals(COURSE_ID_NUMBER, teacherData.getCourseList().get(FIST_ELEMENT).getId());
        assertEquals(COURSE_NAME, teacherData.getCourseList().get(FIST_ELEMENT).getName());
        assertEquals(COURSE_DESCRIPTION, teacherData.getCourseList().get(FIST_ELEMENT).getDescription());
        assertEquals(COURSES_QUANTITY, teacherData.getCourseList().size());
        
    }
}
