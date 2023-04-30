package ua.com.foxminded.university.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoleAuthority {
    ROLE_ADMIN("Admin"),
    ROLE_STAFF("Staff"), 
    ROLE_STUDENT("Student"),
    ROLE_TEACHER("Teacher");
    
    private final String representation;
}
