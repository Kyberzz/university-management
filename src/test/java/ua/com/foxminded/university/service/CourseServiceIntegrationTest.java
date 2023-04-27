package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entitymother.CourseEntityMother;
import ua.com.foxminded.university.entitymother.TeacherEntityMother;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.modelmother.CourseModelMother;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.TeacherRepository;

@SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class CourseServiceIntegrationTest {
    
    public static final int COURSE_QUANTITY = 2;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    private TeacherEntity teacherEntity_1;
    private TeacherEntity teacherEntity_2;
    private CourseEntity courseEntity;
    private CourseModel courseModel;
    
    @BeforeEach
    void setUp() {
        teacherEntity_1 = TeacherEntityMother.complete().build();
        teacherEntity_2 = TeacherEntityMother.complete().build();
        teacherRepository.saveAndFlush(teacherEntity_1);
        teacherRepository.saveAndFlush(teacherEntity_2);
        
        courseEntity = CourseEntityMother.complete().build();
        courseRepository.saveAndFlush(courseEntity);
        
        courseModel = CourseModelMother.complete().build();
    }
    
    @Test
    void addTeacherToCourse() throws ServiceException {
        TeacherModel teacherModel_1 = TeacherModel.builder()
                                                  .id(teacherEntity_1.getId()).build();
        TeacherModel teacherModel_2 = TeacherModel.builder()
                                                  .id(teacherEntity_2.getId()).build();
        
        Set<TeacherModel> teachers = new HashSet<>();
        teachers.add(teacherModel_1);
        teachers.add(teacherModel_2);
        courseModel.setTeachers(teachers);
        courseModel.setId(courseEntity.getId());
        
        courseService.addTeacherToCourse(courseModel);
        
        CourseEntity persistedCourse = courseRepository.findById(
                courseModel.getId().intValue());
        
        assertEquals(COURSE_QUANTITY, persistedCourse.getTeachers().size());
    }
}
