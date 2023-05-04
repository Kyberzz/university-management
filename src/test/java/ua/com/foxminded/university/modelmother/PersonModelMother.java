package ua.com.foxminded.university.modelmother;

import ua.com.foxminded.university.model.PersonModel;

public class PersonModelMother {
    
    private static final String FIRST_NAME = "Elon";
    private static final String LAST_NAME = "Musk";
    
    public static PersonModel.PersonModelBuilder complete() {
        return PersonModel.builder().firstName(FIRST_NAME)
                                    .lastName(LAST_NAME);
    }
}