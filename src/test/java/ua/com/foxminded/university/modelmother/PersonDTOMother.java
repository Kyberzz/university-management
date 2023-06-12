package ua.com.foxminded.university.modelmother;

import ua.com.foxminded.university.dto.PersonDTO;

public class PersonDTOMother {
    
    private static final String FIRST_NAME = "Elon";
    private static final String LAST_NAME = "Musk";
    
    public static PersonDTO.PersonDTOBuilder complete() {
        return PersonDTO.builder().firstName(FIRST_NAME)
                                  .lastName(LAST_NAME);
    }
}