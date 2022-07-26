package ua.com.foxminded.universitymanagement.entity;

import java.util.Objects;

public class TimetableEntity {

    private Integer id;
    private Integer groupId;
    private Integer courseId;
    private long startTime;
    private long endTime;
    private String description;
    private WeekDayEntity weekDay;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
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
        return Objects.hash(courseId, description, endTime, groupId, id, startTime, weekDay);
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
        return Objects.equals(courseId, other.courseId) && Objects.equals(description, other.description)
                && endTime == other.endTime && Objects.equals(groupId, other.groupId) && Objects.equals(id, other.id)
                && startTime == other.startTime && weekDay == other.weekDay;
    }
}
