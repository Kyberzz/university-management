package ua.com.foxminded.university.entity;

import java.util.List;
import java.util.Objects;

public class CourseEntity {
    private Integer id;
    private TeacherEntity teacher;
    private String name;
    private String description;
    private List<TimetableEntity> timetableList;
   
    public CourseEntity() {
    }

    public CourseEntity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TeacherEntity getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherEntity teacher) {
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

    public List<TimetableEntity> getTimetableList() {
        return timetableList;
    }

    public void setTimetableList(List<TimetableEntity> timetableList) {
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
        CourseEntity other = (CourseEntity) obj;
        return Objects.equals(description, other.description) && Objects.equals(id, other.id)
                && Objects.equals(name, other.name) && Objects.equals(teacher, other.teacher)
                && Objects.equals(timetableList, other.timetableList);
    }

    @Override
    public String toString() {
        return "CourseEntity [id=" + id + ", teacher=" + teacher + ", name=" + name + ", description=" + description
                + ", timetableList=" + timetableList + "]";
    }
}