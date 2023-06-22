package ua.com.foxminded.university.dtomother;

import ua.com.foxminded.university.dto.UserPersonDTO;

public class PersonDTOMother {
    
    private static final String FIRST_NAME = "Elon";
    private static final String LAST_NAME = "Musk";
    
    public static UserPersonDTO.UserPersonDTOBuilder complete() {
        return UserPersonDTO.builder().firstName(FIRST_NAME)
                                      .lastName(LAST_NAME);
    }
}
