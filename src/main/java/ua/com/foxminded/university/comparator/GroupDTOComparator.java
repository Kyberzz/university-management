package ua.com.foxminded.university.comparator;

import java.util.Comparator;

import ua.com.foxminded.university.dto.GroupDTO;

public class GroupDTOComparator implements Comparator<GroupDTO> {

    @Override
    public int compare(GroupDTO groupA, GroupDTO anotherB) {
        return Integer.compare(groupA.getId(), anotherB.getId());
    }
}
