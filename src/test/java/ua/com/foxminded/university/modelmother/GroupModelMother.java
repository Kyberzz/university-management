package ua.com.foxminded.university.modelmother;

import ua.com.foxminded.university.model.GroupModel;

public class GroupModelMother {
    
    public static final String groupName = "k-2";
    
    public static GroupModel.GroupModelBuilder complete() {
        return GroupModel.builder().name(groupName);
    }
}