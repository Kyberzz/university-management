package ua.com.foxminded.university.entitymother;

import ua.com.foxminded.university.entity.UserPerson;

public class UserPersonMother {
    
    public static final String FIRST_NAME = "Elon";
    public static final String LAST_NAME = "Musk";
    
    public static final UserPerson.UserPersonBuilder complete() {
        return UserPerson.builder().firstName(FIRST_NAME)
                                   .lastName(LAST_NAME);
    }
}
