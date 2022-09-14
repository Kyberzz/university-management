package ua.com.foxminded.university;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import ua.com.foxminded.university.config.AppConfig;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.jdbc.CourseJdbcDao;

public class Boot {

    public static void main(String[] args) throws DaoException {
        
        AbstractApplicationContext contex = new AnnotationConfigApplicationContext(AppConfig.class);
        CourseJdbcDao courseDao = contex.getBean("courseJdbcDao", CourseJdbcDao.class);
        System.out.println(courseDao.getById(1));

    }
}
