package ua.com.foxminded.university.objectmother;

import ua.com.foxminded.university.entity.GroupEntity;

public class GroupEntityMother {
    public static final String GROUP_NAME = "kt-2";
    
    public static GroupEntity.GroupEntityBuilder complete() {
        return GroupEntity.builder().name(GROUP_NAME);
    }
}
