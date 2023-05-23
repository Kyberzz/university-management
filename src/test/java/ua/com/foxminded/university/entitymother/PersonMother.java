package ua.com.foxminded.university.entitymother;

import ua.com.foxminded.university.entity.Person;

public class PersonMother {
    
    public static final String FIRST_NAME = "Elon";
    public static final String LAST_NAME = "Musk";
    
    public static final Person.PersonBuilder complete() {
        return Person.builder().firstName(FIRST_NAME)
                                     .lastName(LAST_NAME);
    }
}