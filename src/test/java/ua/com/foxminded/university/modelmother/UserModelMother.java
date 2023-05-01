package ua.com.foxminded.university.modelmother;

import ua.com.foxminded.university.model.UserModel;

public class UserModelMother {
    
    public static final String PASSWORD = "pass";
    public static final String EMAIL = "my@email";
    
    public static UserModel.UserModelBuilder complete() {
        return UserModel.builder().email(EMAIL)
                                  .enabled(true)
                                  .password(PASSWORD);
    }
}