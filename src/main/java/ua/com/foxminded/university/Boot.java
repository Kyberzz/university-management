package ua.com.foxminded.university;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.TimetableDao;
import ua.com.foxminded.university.dao.jdbc.CourseJdbcDao;
import ua.com.foxminded.university.dao.jdbc.JdbcDaoConfig;
import ua.com.foxminded.university.dao.jdbc.GroupJdbcDao;
import ua.com.foxminded.university.dao.jdbc.StudentJdbcDao;
import ua.com.foxminded.university.dao.jdbc.TeacherJdbcDao;
import ua.com.foxminded.university.dao.jdbc.TimetableJdbcDao;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entity.TimetableEntity;

public class Boot {

    public static void main(String[] arg) {

        /*
         * ApplicationContext context = new ClassPathXmlApplicationContext("dao.xml");
         * JdbcStudentDao studentDao = context.getBean("studentDao",
         * JdbcStudentDao.class); StudentEntity student = studentDao.getById(1);
         * JdbcTimetableDao timetableDao = context.getBean("timetableDao",
         * JdbcTimetableDao.class); TimetableEntity timetable = timetableDao.getById(1);
         * System.out.println(timetable.toString());
         * System.out.println(student.toString());
         */

        ApplicationContext context = new AnnotationConfigApplicationContext(JdbcDaoConfig.class);
       
        CourseDao courseDao = context.getBean("courseJdbcDao", CourseJdbcDao.class);
        CourseEntity course = courseDao.getById(1);
        System.out.println(course.toString());

        GroupDao groupDao = context.getBean("groupJdbcDao", GroupJdbcDao.class);
        GroupEntity group = groupDao.getById(1);
        System.out.println(group.toString());

        StudentDao studentDao = context.getBean("studentJdbcDao", StudentJdbcDao.class);
        StudentEntity student = studentDao.getGroupByStudentId(1);
        System.out.println(student.toString());

        TeacherDao teacherDao = context.getBean("teacherJdbcDao", TeacherJdbcDao.class);
        TeacherEntity teacher = teacherDao.getById(1);
        System.out.println(teacher.toString());
        
        
        TimetableDao timetableDao = context.getBean("timetableJdbcDao", TimetableJdbcDao.class);
        TimetableEntity timetable = timetableDao.getById(1);
        System.out.println(timetable.toString());
    }
}
