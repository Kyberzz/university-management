package ua.com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ua.com.foxminded.university.entity.GroupEntity;

public final class GroupMapper implements RowMapper<GroupEntity> {
    
    private static final String GROUP_ID = "group_id";
    private static final String GROUP_NAME = "group_name";
    
    @Override
    public GroupEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        GroupEntity group = new GroupEntity();
        group.setId(resultSet.getInt(GROUP_ID));
        group.setName(resultSet.getString(GROUP_NAME));
        return group;
    }
}
