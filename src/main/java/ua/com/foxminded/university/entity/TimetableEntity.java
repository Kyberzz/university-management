package ua.com.foxminded.university.entity;

import java.util.Objects;

public class TimetableEntity {

    private Integer id;
    private GroupEntity group;
    private CourseEntity course;
    private long startTime;
    private long endTime;
    private String description;
    private WeekDayEntity weekDay;
    
    public TimetableEntity() {
    }

    public TimetableEntity(Integer id) {
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    @Override
    public String toString() {
        return "TimetableEntity [id=" + id + ", group=" + group + ", course=" + course + ", startTime=" + startTime
                + ", endTime=" + endTime + ", description=" + description + ", weekDay=" + weekDay + "]";
    }
}
