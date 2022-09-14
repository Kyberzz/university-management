package ua.com.foxminded.university.dao.jdbc;

import java.util.ArrayList;

import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.jdbc.mapper.GroupMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.StudentMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.TimetableMapper;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.TimetableEntity;

@Slf4j
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
    public GroupEntity getTimetableListByGroupId(int id) throws DaoException {
        try {
            log.debug("Get timetable list by group id={}", id);
            String query =  queries.getProperty(GET_TIMETABLE_LIST_BY_GROUP_ID);
            GroupEntity groupWithTimetableList = jdbcTemplate.query(query,
                    preparedStatement -> preparedStatement.setInt(1, id),
                    (resultSet) -> {
                        GroupEntity group = null;
                        
                        while(resultSet.next()) {
                            if (group == null) {
                                group = groupMapper.mapRow(resultSet, resultSet.getRow());
                                group.setTimetableList(new ArrayList<>());
                            }
                            
                            TimetableEntity timetable = timetableMapper.mapRow(resultSet, resultSet.getRow());
                            group.getTimetableList().add(timetable);
                        }
                        return group;
                    });
            log.trace("Timetable list of group with id={} was received.", groupWithTimetableList.getId());
            return groupWithTimetableList;
        } catch (DataAccessException e) {
            throw new DaoException("Getting timetable list by the group id failed.", e);
        }
    }
    
    @Override
    public GroupEntity getStudentListByGroupId(int id) throws DaoException {
        try {
            log.debug("Get students list by group id={}", id);
            String query = queries.getProperty(GET_STUDENT_LIST_BY_GROUP_ID);
            GroupEntity groupWithStudentList = jdbcTemplate.query(query,
                    preparedStatement -> preparedStatement.setInt(1, id), 
                    (resultSet) -> {
                        GroupEntity group = null;

                        while (resultSet.next()) {
                            if (group == null) {
                                group = groupMapper.mapRow(resultSet, resultSet.getRow());
                                group.setStudentList(new ArrayList<>());
                            }
                            StudentEntity student = studentMapper.mapRow(resultSet, resultSet.getRow());
                            group.getStudentList().add(student);
                        }
                        return group;
                    });
            log.trace("Students list of the group with id={} was received", groupWithStudentList.getId());
            return groupWithStudentList;
        } catch (DataAccessException e) {
            throw new DaoException("Getting students list by the group id failed.", e);
        }
    }
    
    @Override
    public int insert(GroupEntity entity) throws DaoException {
        try {
            log.debug("Insert group with id={}", entity.getId());
            String query = queries.getProperty(INSERT);
            
            int insertedGroupsQuantity = jdbcTemplate.update(query,
                    preparedStatement -> preparedStatement.setString(1, entity.getName()));
            log.trace("Group with id={} was inserted.", entity.getId());
            return insertedGroupsQuantity;
        } catch (DataAccessException e) {
            throw new DaoException("Inserting the group to the database failed.", e);
        }
    }
    
    @Override
    public GroupEntity getById(int id) throws DaoException {
        try {
            log.debug("Get group by id={}", id);
            String query = queries.getProperty(GET_BY_ID);
            GroupEntity group = jdbcTemplate.queryForObject(query, 
                    (resultSet, rowNum) -> groupMapper.mapRow(resultSet, rowNum), 
                    id);
            log.trace("Group with id={} was received.", group.getId());
            return group;
        } catch (DataAccessException e) {
            throw new DaoException("Getting the group by its id failed.", e);
        }
    }
    
    @Override
    public int update(GroupEntity entity) throws DaoException {
        try {
            log.debug("Update group with id={}.", entity.getId());
            String query = queries.getProperty(UPDATE);
            
            int updatedGroupQuantity = jdbcTemplate.update(query, 
                    preparedStatement -> {
                        preparedStatement.setString(1, entity.getName());
                        preparedStatement.setInt(2, entity.getId());
                    });
            log.trace("Group with id={} was updated.", entity.getId());
            return updatedGroupQuantity;
        } catch (DataAccessException e) {
            throw new DaoException("Updating the group failed.", e);
        }
    }
    
    @Override
    public int deleteById(int id) throws DaoException {
        try {
            log.debug("Delete group by id={}.", id);
            String query = queries.getProperty(DELETE_BY_ID);
            
            int deletedGroupsQuantity = jdbcTemplate.update(query, 
                    preparedStatement -> preparedStatement.setInt(1, id));
            log.trace("Group with id={} was deleted.", id);
            return deletedGroupsQuantity;
        } catch (DataAccessException e) {
            throw new DaoException("Deleting the group by its id failed.", e);
        }
    }
}
