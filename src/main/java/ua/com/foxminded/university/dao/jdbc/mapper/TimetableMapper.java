package ua.com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.WeekDayEntity;

public final class TimetableMapper implements RowMapper<TimetableEntity> {
    
    public static final String WEEK_DAY = "week_day";
    public static final String START_TIME = "start_time";
    public static final String GROUP_ID = "group_id";
    public static final String END_TIME = "end_time";
    public static final String DESCRIPTION = "timetable_description";
    public static final String COURSE_ID = "course_id";
    public static final String ID = "timetable_id";
    
    @Override
    public TimetableEntity mapRow(ResultSet resultSet, int numRow) throws SQLException {
        TimetableEntity timetable = new TimetableEntity();
        timetable.setCourse(new CourseEntity(resultSet.getInt(COURSE_ID)));
        timetable.setDescription(resultSet.getString(DESCRIPTION));
        timetable.setEndTime(resultSet.getLong(END_TIME));
        timetable.setGroup(new GroupEntity(resultSet.getInt(GROUP_ID)));
        timetable.setId(resultSet.getInt(ID));
        timetable.setStartTime(resultSet.getLong(START_TIME));
        timetable.setWeekDay(WeekDayEntity.valueOf(resultSet.getString(WEEK_DAY)));
        return timetable;
    }
}
