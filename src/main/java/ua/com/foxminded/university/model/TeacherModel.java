package ua.com.foxminded.university.model;

import java.util.List;
import java.util.Objects;

public class TeacherModel {

    private int id;
    private String firstName;
    private String lastName;
    private List<CourseModel> courseList;
    
    public TeacherModel() {
    }

    public TeacherModel(int id) {
        this.id = id;
    }

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

    public List<CourseModel> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<CourseModel> courseList) {
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
        TeacherModel other = (TeacherModel) obj;
        return Objects.equals(courseList, other.courseList) && Objects.equals(firstName, other.firstName)
                && id == other.id && Objects.equals(lastName, other.lastName);
    }

    @Override
    public String toString() {
        return "TeacherEntity [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", courseList="
                + courseList + "]";
    }
}
