package ua.com.foxminded.university;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.com.foxminded.university.dao.jdbc.JdbcStudentDao;
import ua.com.foxminded.university.dao.jdbc.JdbcTimetableDao;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.TimetableEntity;

public class Main {
    
    public static void main(String[] arg) {
        
        ApplicationContext context = new ClassPathXmlApplicationContext("dao.xml");
        JdbcStudentDao studentDao = context.getBean("studentDao", JdbcStudentDao.class);
        StudentEntity student = studentDao.getStudentById(1);
        System.out.println(student.toString());
        
        JdbcTimetableDao timetableDao = context.getBean("timetableDao", JdbcTimetableDao.class);
        List<TimetableEntity> timetableList = timetableDao.getTimetableByStudentId(1);
        timetableList.stream().forEach(timetable -> System.out.println(timetable.toString()));
        
        
        
    }
}
