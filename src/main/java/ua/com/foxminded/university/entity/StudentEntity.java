package ua.com.foxminded.university.entity;

import java.util.Objects;

public class StudentEntity {

    private int id;
    private GroupEntity group;
    private String firstName;
    private String lastName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, group, id, lastName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StudentEntity other = (StudentEntity) obj;
        return Objects.equals(firstName, other.firstName) && Objects.equals(group, other.group) && id == other.id
                && Objects.equals(lastName, other.lastName);
    }

    @Override
    public String toString() {
        return "StudentEntity [id=" + id + ", group=" + group + ", firstName=" + firstName + ", lastName=" + lastName
                + "]";
    }
}
