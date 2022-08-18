package ua.com.foxminded.university;


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
        StudentEntity student = studentDao.getById(1);
        JdbcTimetableDao timetableDao = context.getBean("timetableDao", JdbcTimetableDao.class);
        TimetableEntity timetable = timetableDao.getById(1);
        System.out.println(timetable.toString());
        System.out.println(student.toString());
        
    }
}
