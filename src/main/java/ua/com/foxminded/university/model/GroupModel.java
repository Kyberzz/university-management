package ua.com.foxminded.university.model;

import java.util.List;
import java.util.Objects;

public class GroupModel {

    private int id;
    private String name;
    private List<TimetableModel> timetableList;
    private List<StudentModel> studentList;
    
    public GroupModel() {
    }

    public GroupModel(int id) {
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

    public List<TimetableModel> getTimetableList() {
        return timetableList;
    }

    public void setTimetableList(List<TimetableModel> timetableList) {
        this.timetableList = timetableList;
    }

    public List<StudentModel> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<StudentModel> studentList) {
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
        GroupModel other = (GroupModel) obj;
        return id == other.id && Objects.equals(name, other.name) && Objects.equals(studentList, other.studentList)
                && Objects.equals(timetableList, other.timetableList);
    }

    @Override
    public String toString() {
        return "GroupEntity [id=" + id + ", name=" + name + ", timetableList=" + timetableList + ", studentList="
                + studentList + "]";
    }
}
