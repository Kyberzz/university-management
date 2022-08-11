package ua.com.foxminded.university;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.com.foxminded.university.dao.jdbc.JdbcStudentDao;
import ua.com.foxminded.university.entity.StudentEntity;

public class Main {
    
    public static void main(String[] arg) {
        
        ApplicationContext context = new ClassPathXmlApplicationContext("dao.xml");
        JdbcStudentDao studentDao = context.getBean("studentDao", JdbcStudentDao.class);
        StudentEntity student = studentDao.getStudentById(1);
        
        System.out.println(student.getId());
        System.out.println(student.getFirstName());
        System.out.println(student.getLastName());
        
        student.getGroup().getTimetableList().forEach(timetable -> {
            System.out.println(timetable.getId());
            System.out.println(timetable.getStartTime());
            System.out.println(timetable.getEndTime());
        });
    }
}
