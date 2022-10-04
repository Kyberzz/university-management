package ua.com.foxminded.university;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.config.AppConfig;
import ua.com.foxminded.university.dao.jdbc.GroupJdbcDao;
import ua.com.foxminded.university.entity.GroupEntity;

@Slf4j
public class Boot {

    public static void main(String[] args) {
        try {
            ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
            GroupJdbcDao groupDao = context.getBean("groupJdbcDao", GroupJdbcDao.class);
            GroupEntity group = groupDao.getById(1);
            System.out.println(group.getName());
            
        } catch (Exception e) {
            log.error("Error", e);
        }
        

    }
}
