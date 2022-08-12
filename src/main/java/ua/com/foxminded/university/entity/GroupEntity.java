package ua.com.foxminded.university.entity;

import java.util.List;
import java.util.Objects;

public class GroupEntity {

    private int id;
    private String name;
    private List<TimetableEntity> timetableList;
    private List<StudentEntity> studentList;
    
    public GroupEntity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TimetableEntity> getTimetableList() {
        return timetableList;
    }

    public void setTimetableList(List<TimetableEntity> timetableList) {
        this.timetableList = timetableList;
    }

    public List<StudentEntity> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<StudentEntity> studentList) {
        this.studentList = studentList;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, studentList, timetableList);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GroupEntity other = (GroupEntity) obj;
        return id == other.id && Objects.equals(name, other.name) && Objects.equals(studentList, other.studentList)
                && Objects.equals(timetableList, other.timetableList);
    }

    @Override
    public String toString() {
        return "GroupEntity [id=" + id + ", name=" + name + ", timetableList=" + timetableList + ", studentList="
                + studentList + "]";
    }
}
