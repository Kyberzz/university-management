package ua.com.foxminded.university.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Authority {
    ADMIN("Admin"), 
    STAFF("Staff"), 
    STUDENT("Student"),
    TEACHER("Teacher");
    
    private final String representation;
}
