package ua.com.foxminded.university.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Authority {
    ADMIN("Administrator"), 
    STAFF("Staff"), 
    STUDENT("Student");
    
    private final String representation;
}
