package ua.com.foxminded.university;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.foxminded.university.config.AppConfig;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.TimetableDao;
import ua.com.foxminded.university.dao.jdbc.CourseJdbcDao;
import ua.com.foxminded.university.dao.jdbc.GroupJdbcDao;
import ua.com.foxminded.university.dao.jdbc.StudentJdbcDao;
import ua.com.foxminded.university.dao.jdbc.TeacherJdbcDao;
import ua.com.foxminded.university.dao.jdbc.TimetableJdbcDao;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.model.CourseModel;
import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.impl.CourseServiceImpl;

public class Boot {
    
    private static final Logger logger = LoggerFactory.getLogger(Boot.class);
    
    public static void main(String[] arg) {
     //   LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
     //   StatusPrinter.print(loggerContext);
        

        /*
         * ApplicationContext context = new ClassPathXmlApplicationContext("dao.xml");
         * JdbcStudentDao studentDao = context.getBean("studentDao",
         * JdbcStudentDao.class); StudentEntity student = studentDao.getById(1);
         * JdbcTimetableDao timetableDao = context.getBean("timetableDao",
         * JdbcTimetableDao.class); TimetableEntity timetable = timetableDao.getById(1);
         * System.out.println(timetable.toString());
         * System.out.println(student.toString());
         */
        
        
        try {
            ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
            
            CourseDao courseDao = context.getBean("courseJdbcDao", CourseJdbcDao.class);
            CourseEntity course = courseDao.getTimetableListByCourseId(1);
          //  System.out.println(course.toString());
            

            StudentDao studentDao = context.getBean("studentJdbcDao", StudentJdbcDao.class);
            StudentEntity student = studentDao.getGroupByStudentId(1);
         //   System.out.println(student.toString());
            
            /*
            GroupDao groupDao = context.getBean("groupJdbcDao", GroupJdbcDao.class);
            GroupEntity group = groupDao.getById(1);
            System.out.println(group.toString());


            TeacherDao teacherDao = context.getBean("teacherJdbcDao", TeacherJdbcDao.class);
            TeacherEntity teacher = teacherDao.getById(1);
            System.out.println(teacher.toString());
            
            
            TimetableDao timetableDao = context.getBean("timetableJdbcDao", TimetableJdbcDao.class);
            TimetableEntity timetable = timetableDao.getById(1);
            System.out.println(timetable.toString());
            
            CourseModel courseModel = new CourseModel();
            courseModel.setDescription("something");
            courseModel.setId(3);
            courseModel.setName("Philosophy");
            courseModel.setTeacher(new TeacherModel(1));
            
            CourseService<CourseModel> courseService = context.getBean("courseServiceImpl", CourseServiceImpl.class);
            int status = courseService.updateCourse(courseModel);
            System.out.println(status);
            */
            
        } catch (Exception e) {
            logger.error("Error");
        }
        
        
    }
}
