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
        System.out.println(student.toString());
        
    }

}
