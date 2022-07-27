package ua.com.foxminded.universitymanagement.entity;

import java.util.Objects;

public class TimetableEntity {

    private int id;
    private GroupEntity group;
    private CourseEntity course;
    private long startTime;
    private long endTime;
    private String description;
    private WeekDayEntity weekDay;

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

    public CourseEntity getCourse() {
        return course;
    }

    public void setCourse(CourseEntity course) {
        this.course = course;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WeekDayEntity getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(WeekDayEntity weekDay) {
        this.weekDay = weekDay;
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, description, endTime, group, id, startTime, weekDay);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TimetableEntity other = (TimetableEntity) obj;
        return Objects.equals(course, other.course) && Objects.equals(description, other.description)
                && endTime == other.endTime && Objects.equals(group, other.group) && id == other.id
                && startTime == other.startTime && weekDay == other.weekDay;
    }
}
