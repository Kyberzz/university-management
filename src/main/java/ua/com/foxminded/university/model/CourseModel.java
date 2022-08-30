package ua.com.foxminded.university.model;

import java.util.List;
import java.util.Objects;

public class CourseModel {
    private Integer id;
    private TeacherModel teacher;
    private String name;
    private String description;
    private List<TimetableModel> timetableList;
    
    public CourseModel() {
    }

    public CourseModel(Integer id) {
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TeacherModel getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherModel teacher) {
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TimetableModel> getTimetableList() {
        return timetableList;
    }

    public void setTimetableList(List<TimetableModel> timetableList) {
        this.timetableList = timetableList;
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, id, name, teacher, timetableList);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CourseModel other = (CourseModel) obj;
        return Objects.equals(description, other.description) && id == other.id && Objects.equals(name, other.name)
                && Objects.equals(teacher, other.teacher) && Objects.equals(timetableList, other.timetableList);
    }

    @Override
    public String toString() {
        return "CourseEntity [id=" + id + ", teacher=" + teacher + ", name=" + name + ", description=" + description
                + ", timetableList=" + timetableList + "]";
    }
}