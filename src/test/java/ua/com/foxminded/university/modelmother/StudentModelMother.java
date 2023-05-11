package ua.com.foxminded.university.modelmother;

import ua.com.foxminded.university.model.PersonModel;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.model.UserModel;

public class StudentModelMother {
    
    public static StudentModel.StudentModelBuilder complete() {
        PersonModel person = PersonModelMother.complete().build();
        UserModel user = UserModelMother.complete().person(person).build();
        return StudentModel.builder().user(user);
    }
}
