package ua.com.foxminded.university.model;

import java.util.Objects;

public class TimetableModel {

    private int id;
    private GroupModel group;
    private CourseModel course;
    private long startTime;
    private long endTime;
    private String description;
    private WeekDayModel weekDay;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GroupModel getGroup() {
        return group;
    }

    public void setGroup(GroupModel group) {
        this.group = group;
    }

    public CourseModel getCourse() {
        return course;
    }

    public void setCourse(CourseModel course) {
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

    public WeekDayModel getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(WeekDayModel weekDay) {
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
        TimetableModel other = (TimetableModel) obj;
        return Objects.equals(course, other.course) && Objects.equals(description, other.description)
                && endTime == other.endTime && Objects.equals(group, other.group) && id == other.id
                && startTime == other.startTime && weekDay == other.weekDay;
    }

    @Override
    public String toString() {
        return "TimetableEntity [id=" + id + ", group=" + group + ", course=" + course + ", startTime=" + startTime
                + ", endTime=" + endTime + ", description=" + description + ", weekDay=" + weekDay + "]";
    }
}
