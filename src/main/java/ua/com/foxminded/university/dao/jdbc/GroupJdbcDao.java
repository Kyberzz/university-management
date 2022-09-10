package ua.com.foxminded.university.dao.jdbc;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.jdbc.mapper.GroupMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.StudentMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.TimetableMapper;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;
import ua.com.foxminded.university.entity.TimetableEntity;

@Repository
public class GroupJdbcDao implements GroupDao {
    
    private static final Logger logger = LoggerFactory.getLogger(GroupJdbcDao.class);
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
            logger.debug("Get timetable list of the group with id={}", id);
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
            logger.trace("Timetable list of the group with id={} was received.", 
                         groupWithTimetableList.getId());
            return groupWithTimetableList;
        } catch (DataAccessException e) {
            String errorMessage = "Getting the timetable list by its group id failed.";
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
    
    @Override
    public GroupEntity getStudentListByGroupId(int id) throws DaoException {
        try {
            logger.debug("Get students list of the group with id={}", id);
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
            logger.trace("Students list of the group with id={} was received", groupWithStudentList.getId());
            return groupWithStudentList;
        } catch (DataAccessException e) {
            String errorMessage = "Getting the student list by its group id failed.";
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
    
    @Override
    public int insert(GroupEntity entity) throws DaoException {
        try {
            logger.debug("Insert the group with id={}", entity.getId());
            String query = queries.getProperty(INSERT);
            int rowQuantity = jdbcTemplate.update(query,
                    preparedStatement -> preparedStatement.setString(1, entity.getName()));
            logger.trace("The group with id={} was inserted.", entity.getId());
            return rowQuantity;
        } catch (DataAccessException e) {
            String errorMessage = "Inserting the group data to the database failed.";
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
    
    @Override
    public GroupEntity getById(int id) throws DaoException {
        try {
            logger.debug("Get the group with id={}", id);
            String query = queries.getProperty(GET_BY_ID);
            GroupEntity group = jdbcTemplate.queryForObject(query, 
                    (resultSet, rowNum) -> groupMapper.mapRow(resultSet, rowNum), 
                    id);
            logger.trace("The group with id={} was received.", group.getId());
            return group;
        } catch (DataAccessException e) {
            String errorMessage = "Getting the group data by its id from the database failed.";
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
    
    @Override
    public int update(GroupEntity entity) throws DaoException {
        try {
            logger.debug("Update the group with id={}.", entity.getId());
            String query = queries.getProperty(UPDATE);
            int rowQuantity = jdbcTemplate.update(query, 
                    preparedStatement -> {
                        preparedStatement.setString(1, entity.getName());
                        preparedStatement.setInt(2, entity.getId());
                    });
            logger.trace("The group with id={} was updated.", entity.getId());
            return rowQuantity;
        } catch (DataAccessException e) {
            String errorMessage = "Updating the database group data failed.";
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
    
    @Override
    public int deleteById(int id) throws DaoException {
        try {
            logger.debug("Delete the group with id={}.", id);
            String query = queries.getProperty(DELETE_BY_ID);
            int rowQuantity = jdbcTemplate.update(query, 
                    preparedStatement -> preparedStatement.setInt(1, id));
            logger.trace("The group with id={}.", id);
            return rowQuantity;
        } catch (DataAccessException e) {
            String errorMessage = "Deleting the group data from the database failed.";
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
}
