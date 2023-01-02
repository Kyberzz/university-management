package ua.com.foxminded.university;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration(exclude = {ThymeleafAutoConfiguration.class}) 
@SpringBootConfiguration
@ComponentScan
public class UniversityManagementApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(UniversityManagementApplication.class, args);
    }
}
