package ua.com.foxminded.university.objectmother;

import ua.com.foxminded.university.entity.PersonEntity;
import ua.com.foxminded.university.entity.RoleAuthority;
import ua.com.foxminded.university.entity.UserAuthorityEntity;
import ua.com.foxminded.university.entity.UserEntity;

public class UserEntityMother {
    
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email@com";
    
    
    public static UserEntity.UserEntityBuilder complete() {
        PersonEntity person = PersonEntityMother.complete().build();
        UserAuthorityEntity userAuthority = UserAuthorityEntity.builder()
                .roleAuthority(RoleAuthority.ROLE_ADMIN)
                .build();
        
        return UserEntity.builder().email(EMAIL)
                                   .enabled(true)
                                   .password(PASSWORD)
                                   .person(person)
                                   .userAuthority(userAuthority);
    }

}
