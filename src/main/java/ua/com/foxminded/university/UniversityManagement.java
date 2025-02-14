package ua.com.foxminded.university;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UniversityManagement {

    public static final String DEV_PROFILE = "dev";
    public static final String PROD_PROFILE = "prod";

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(UniversityManagement.class);
        application.setAdditionalProfiles(DEV_PROFILE);
        application.run(args);
    }
}
