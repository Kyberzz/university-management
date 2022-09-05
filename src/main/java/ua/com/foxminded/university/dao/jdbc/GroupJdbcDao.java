package ua.com.foxminded.university.dao.jdbc;

import java.util.ArrayList;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.jdbc.mapper.GroupMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.StudentMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.TimetableMapper;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.TimetableEntity;

@Repository
public class GroupJdbcDao implements GroupDao {
    
    private static final String GET_TIMETABLE_LIST_BY_GROUP_ID = "group.getTimetableListByGroupId";
    private static final String GET_STUDENT_LIST_BY_GROUP_ID = "group.getStudentListByGroupId";
    private static final String INSERT = "group.insert";
    private static final String GET_BY_ID = "group.getById";
    private static final String UPDATE = "group.update";
    private static final String DELETE_BY_ID = "group.deleteById";
    
    private Environment queries;
    private JdbcTemplate jdbcTemplate;
    private GroupMapper groupMapper;
    private StudentMapper studentMapper;
    private TimetableMapper timetableMapper;
    
    public GroupJdbcDao(Environment queries, JdbcTemplate jdbcTemplate, GroupMapper groupMapper,
            StudentMapper studentMapper, TimetableMapper timetableMapper) {
        this.queries = queries;
        this.jdbcTemplate = jdbcTemplate;
        this.groupMapper = groupMapper;
        this.studentMapper = studentMapper;
        this.timetableMapper = timetableMapper;
    }

    @Override
    public GroupEntity getTimetableListByGroupId(int id) {
        GroupEntity groupWithTimetableList = jdbcTemplate.queryForObject(
                queries.getProperty(GET_TIMETABLE_LIST_BY_GROUP_ID), 
                (resultSet, rowNum) -> {
                    GroupEntity group = null;
                    
                    if (group == null) {
                        group = groupMapper.mapRow(resultSet, rowNum);
                        group.setTimetableList(new ArrayList<>());
                    }
                    
                    TimetableEntity timetable = timetableMapper.mapRow(resultSet, rowNum);
                    group.getTimetableList().add(timetable);
                    return group;
                }, 
                id);
        return groupWithTimetableList;
    }
    
    @Override
    public GroupEntity getStudentListByGroupId(int id) {
        GroupEntity groupWithStudentList = jdbcTemplate.queryForObject(
                queries.getProperty(GET_STUDENT_LIST_BY_GROUP_ID), 
                (resultSet, rowNum) -> {
                    GroupEntity group = null;
                    
                    if (group == null) {
                        group = groupMapper.mapRow(resultSet, rowNum);
                        group.setStudentList(new ArrayList<>());
                    }
                    
                    StudentEntity student = studentMapper.mapRow(resultSet, rowNum);
                    group.getStudentList().add(student);
                    return group;
                }, 
                id);
        return groupWithStudentList;
    }
    
    @Override
    public int insert(GroupEntity entity) {
        return jdbcTemplate.update(queries.getProperty(INSERT),
                                   preparedStatement -> preparedStatement.setString(1, entity.getName()));
    }
    
    @Override
    public GroupEntity getById(int id) {
        GroupEntity group = jdbcTemplate.queryForObject(queries.getProperty(GET_BY_ID), 
                (resultSet, rowNum) -> groupMapper.mapRow(resultSet, rowNum), 
                id);
        return group;
    }
    
    @Override
    public int update(GroupEntity entity) {
        return jdbcTemplate.update(queries.getProperty(UPDATE),
                                   preparedStatement -> {
                                       preparedStatement.setString(1, entity.getName());
                                       preparedStatement.setInt(2, entity.getId());
                                   });
    }
    
    @Override
    public int deleteById(int id) {
        return jdbcTemplate.update(queries.getProperty(DELETE_BY_ID), 
                                   preparedStatement -> preparedStatement.setInt(1, id));
    }
}
