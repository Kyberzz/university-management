package ua.com.foxminded.universitymanagement.entity;

import java.util.List;
import java.util.Objects;

public class CourseEntity {
    private Integer id;
    private Integer teacherId;
    private String name;
    private String description;
    private List<TimetableEntity> timetableList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
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
        return Objects.hash(description, id, name, teacherId, timetableList);
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
                && Objects.equals(name, other.name) && Objects.equals(teacherId, other.teacherId)
                && Objects.equals(timetableList, other.timetableList);
    }
}
