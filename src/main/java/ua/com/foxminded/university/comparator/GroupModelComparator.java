package ua.com.foxminded.university.comparator;

import java.util.Comparator;

import ua.com.foxminded.university.model.GroupModel;

public class GroupModelComparator implements Comparator<GroupModel> {

    @Override
    public int compare(GroupModel groupA, GroupModel anotherB) {
        return Integer.compare(groupA.getId(), anotherB.getId());
    }
}
