package ua.com.foxminded.university;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.config.AppConfig;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.jdbc.StudentJdbcDao;
import ua.com.foxminded.university.entity.StudentEntity;

@Slf4j
public class Main {

    public static void main(String[] args) {
        try {
            
            
            ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
            StudentDao studentDao = context.getBean("studentJdbcDao", StudentJdbcDao.class);
            StudentEntity student = studentDao.getById(1);
            System.out.println(student.getFirstName());
            
            
        } catch (Exception e) {
            log.error("Error", e);
        }
    }
}
