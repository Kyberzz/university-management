package ua.com.foxminded.university;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import ua.com.foxminded.university.config.AppConfig;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.jdbc.CourseJdbcDao;
import ua.com.foxminded.university.service.ServiceException;
import ua.com.foxminded.university.service.impl.CourseServiceImpl;

public class Boot {

    public static void main(String[] args) throws DaoException, ServiceException {
        
        AbstractApplicationContext contex = new AnnotationConfigApplicationContext(AppConfig.class);
        CourseServiceImpl courseService = contex.getBean("courseServiceImpl", CourseServiceImpl.class);
        System.out.println(courseService.getTimetableListByCourseId(1).toString());

    }
}
