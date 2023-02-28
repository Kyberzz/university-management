package ua.com.foxminded.university.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoleAuthority {
    ROLE_ADMIN("Administrator"),
    ROLE_STAFF("Staff"), 
    ROLE_STUDENT("Student");
    
    private final String representation;
}
