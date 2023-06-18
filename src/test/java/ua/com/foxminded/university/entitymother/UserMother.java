package ua.com.foxminded.university.entitymother;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import ua.com.foxminded.university.entity.UserPerson;
import ua.com.foxminded.university.entity.User;

public class UserMother {
    
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email@com";
    
    public static User.UserBuilder complete() {
        UserPerson person = PersonMother.complete().build();
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        
        return User.builder().email(EMAIL)
                             .enabled(true)
                             .password(encoder.encode(PASSWORD))
                             .person(person);
    }
}
