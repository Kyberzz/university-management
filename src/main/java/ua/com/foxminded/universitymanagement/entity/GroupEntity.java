package ua.com.foxminded.universitymanagement.entity;

import java.util.List;
import java.util.Objects;

public class GroupEntity {

    private int id;
    private String name;
    private List<TimetableEntity> timetableLis;
    private List<StudentEntity> studentList;

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

    public List<TimetableEntity> getTimetableLis() {
        return timetableLis;
    }

    public void setTimetableLis(List<TimetableEntity> timetableLis) {
        this.timetableLis = timetableLis;
    }

    public List<StudentEntity> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<StudentEntity> studentList) {
        this.studentList = studentList;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, studentList, timetableLis);
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
                && Objects.equals(timetableLis, other.timetableLis);
    }
}
