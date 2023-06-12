package ua.com.foxminded.university.modelmother;

import ua.com.foxminded.university.dto.GroupDTO;

public class GroupDTOMother {
    
    public static final String groupName = "k-2";
    
    public static GroupDTO.GroupDTOBuilder complete() {
        return GroupDTO.builder().name(groupName);
    }
}