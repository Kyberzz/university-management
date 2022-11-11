package ua.com.foxminded.university;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import ua.com.foxminded.university.config.RepositoryConfig;


//@EnableAutoConfiguration
//@SpringBootConfiguration
//@Import({RepositoryConfig.class})
@SpringBootApplication
public class UniversityManagementApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(UniversityManagementApplication.class, args);
    }
}
