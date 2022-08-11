package ua.com.foxminded.university.entity;

import java.util.List;
import java.util.Objects;

public class TeacherEntity {

    private int id;
    private String firstName;
    private String lastName;
    private List<CourseEntity> courseList;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<CourseEntity> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<CourseEntity> courseList) {
        this.courseList = courseList;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseList, firstName, id, lastName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TeacherEntity other = (TeacherEntity) obj;
        return Objects.equals(courseList, other.courseList) && Objects.equals(firstName, other.firstName)
                && id == other.id && Objects.equals(lastName, other.lastName);
    }

    @Override
    public String toString() {
        return "TeacherEntity [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", courseList="
                + courseList + "]";
    }
}
