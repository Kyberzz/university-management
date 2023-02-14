package ua.com.foxminded.university.entity;

public enum Authority {
    ROLE_ADMIN ("Administrator"), 
    ROLE_STAFF("Staff"), 
    ROLE_STUDENT("Student");
    
    private final String representation;

    private Authority(String represantation) {
        this.representation = represantation;
    }

    public String getRepresentation() {
        return representation;
    }
}
