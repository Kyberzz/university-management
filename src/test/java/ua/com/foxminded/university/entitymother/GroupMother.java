package ua.com.foxminded.university.entitymother;

import ua.com.foxminded.university.entity.Group;

public class GroupMother {
    public static final String GROUP_NAME = "kt-3";
    
    public static Group.GroupBuilder complete() {
        return Group.builder().name(GROUP_NAME);
    }
}
