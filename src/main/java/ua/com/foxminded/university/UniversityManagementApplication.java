package ua.com.foxminded.university;

import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import ua.com.foxminded.university.buisness.BuisnessLayerSpringConfig;

@EnableAutoConfiguration
@SpringBootConfiguration
@Import({BuisnessLayerSpringConfig.class})
public class UniversityManagementApplication {
    
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(UniversityManagementApplication.class, args);
    }
}
