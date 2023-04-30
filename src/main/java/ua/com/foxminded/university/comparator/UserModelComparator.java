package ua.com.foxminded.university.comparator;

import java.util.Comparator;

import ua.com.foxminded.university.model.UserModel;

public class UserModelComparator implements Comparator<UserModel> {
    
    @Override
    public int compare(UserModel userA, UserModel userB) {
        return Integer.compare(userA.getId(), userB.getId());
    }
}
