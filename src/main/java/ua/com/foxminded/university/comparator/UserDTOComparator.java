package ua.com.foxminded.university.comparator;

import java.util.Comparator;

import ua.com.foxminded.university.dto.UserDTO;

public class UserDTOComparator implements Comparator<UserDTO> {
    
    @Override
    public int compare(UserDTO firstUser, UserDTO secondUser) {
        if (firstUser.hasPerson() && secondUser.hasPerson()) {
            return firstUser.getPerson().getLastName().toLowerCase().compareTo(
                    secondUser.getPerson().getLastName().toLowerCase());
        } else {
            return Integer.compare(firstUser.getId(), secondUser.getId());
        }
    }
}
