package ua.com.foxminded.university.comparator;

import java.util.Comparator;

import ua.com.foxminded.university.dto.UserDTO;

public class UserDTOComparator implements Comparator<UserDTO> {
    
    @Override
    public int compare(UserDTO userA, UserDTO userB) {
        return Integer.compare(userA.getId(), userB.getId());
    }
}
