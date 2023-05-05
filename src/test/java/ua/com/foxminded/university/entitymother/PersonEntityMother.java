package ua.com.foxminded.university.entitymother;

import ua.com.foxminded.university.entity.PersonEntity;

public class PersonEntityMother {
    
    public static final String FIRST_NAME = "Elon";
    public static final String LAST_NAME = "Musk";
    
    public static final PersonEntity.PersonEntityBuilder complete() {
        return PersonEntity.builder().firstName(FIRST_NAME)
                                     .lastName(LAST_NAME);
    }
}