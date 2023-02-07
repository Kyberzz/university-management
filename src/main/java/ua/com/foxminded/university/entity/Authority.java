package ua.com.foxminded.university.entity;

public enum Authority {
    ADMINISTRATOR ("Administrator"), 
    STAFF("Staff"), 
    STUDENT("Student");
    
    private final String representation;

    private Authority(String represantation) {
        this.representation = represantation;
    }

    public String getRepresentation() {
        return representation;
    }
}
