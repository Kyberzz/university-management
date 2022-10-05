package ua.com.foxminded.university;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.config.AppConfig;
import ua.com.foxminded.university.dao.jdbc.GroupJdbcDao;
import ua.com.foxminded.university.entity.GroupEntity;

@Slf4j
public class Main {

    public static void main(String[] args) {
        try {
            
            /*
            ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
            GroupJdbcDao groupDao = context.getBean("groupJdbcDao", GroupJdbcDao.class);
            GroupEntity group = groupDao.getById(1);
            System.out.println(group.getName());
            */
            
            
            EntityManagerFactory entityManagerFactory = Persistence
                    .createEntityManagerFactory("UniversityManager");
            GroupJdbcDao groupDao = new GroupJdbcDao(entityManagerFactory);
            GroupEntity group = groupDao.getById(1);
            System.out.println(group.getName());
            
            
            
        } catch (Exception e) {
            log.error("Error", e);
        }
        

    }
}
