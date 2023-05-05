package ua.com.foxminded.university.entitymother;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import ua.com.foxminded.university.entity.PersonEntity;
import ua.com.foxminded.university.entity.UserEntity;

public class UserEntityMother {
    
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email@com";
    
    public static UserEntity.UserEntityBuilder complete() {
        PersonEntity person = PersonEntityMother.complete().build();
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        
        return UserEntity.builder().email(EMAIL)
                                   .enabled(true)
                                   .password(encoder.encode(PASSWORD))
                                   .person(person);
    }
}