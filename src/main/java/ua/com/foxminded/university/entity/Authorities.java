package ua.com.foxminded.university.entity;

public enum Authorities {
    ADMINISTRATOR ("Administrator"), 
    STAFF("Staff"), 
    STUDENT("Student");
    
    private final String representation;

    private Authorities(String represantation) {
        this.representation = represantation;
    }

    public String getRepresentation() {
        return representation;
    }
}
