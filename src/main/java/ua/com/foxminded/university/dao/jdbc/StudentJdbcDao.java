package ua.com.foxminded.university.dao.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.jdbc.mapper.GroupMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.StudentMapper;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;

@Repository
public class StudentJdbcDao implements StudentDao {
    
    private static final Logger logger = LoggerFactory.getLogger(StudentJdbcDao.class);
    private static final String DELETE_BY_ID = "student.deleteById";
    private static final String UPDATE = "student.update";
    private static final String GET_BY_ID = "student.getById";
    private static final String INSERT = "student.insert";
    private static final String GET_GROUP_BY_STUDENT_ID = "student.getGroupByStudentId";
    
    private JdbcTemplate jdbcTemplate;
    private Environment queries;
    private StudentMapper studentMapper;
    private GroupMapper groupMapper;
    
    public StudentJdbcDao(JdbcTemplate jdbcTemplate, Environment queries, StudentMapper studentMapper,
            GroupMapper groupMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
        this.studentMapper = studentMapper;
        this.groupMapper = groupMapper;
    }

    @Override
    public StudentEntity getGroupByStudentId(int id) throws DaoException {
        try {
            logger.debug("Get group with student id={}.", id);
            String query = queries.getProperty(GET_GROUP_BY_STUDENT_ID);
            StudentEntity studentWithGroupData = jdbcTemplate.queryForObject(query, 
                    (resultSet, rowNum) -> {
                        StudentEntity student = studentMapper.mapRow(resultSet, rowNum);
                        GroupEntity group = groupMapper.mapRow(resultSet, rowNum);
                        student.setGroup(group);
                        return student;
                    }, 
                    id);
            logger.trace("Group with student id={} was received.", 
                         studentWithGroupData.getId());
            return studentWithGroupData;
        } catch (DataAccessException e) {
            String errorMessage = "Getting the student data by its id from the database failed.";
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
    
    @Override
    public int insert(StudentEntity entity) throws DaoException {
        try {
            logger.debug("Insert student with id={} to the database.", entity.getId());
            String query = queries.getProperty(INSERT);
            int rowQuantity = jdbcTemplate.update(query, 
                    preparedStatement -> {
                        preparedStatement.setString(1, entity.getFirstName());
                        preparedStatement.setString(2, entity.getLastName());
                        preparedStatement.setInt(3, entity.getGroup().getId());
                    });
            logger.trace("Student with id={} was added to the database.", entity.getId());
            return rowQuantity;
        } catch (DataAccessException e) {
            String errorMessage = "Inserting the student data to the database failed.";
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
    
    @Override
    public StudentEntity getById(int id) throws DaoException {
        try {
            logger.debug("Get student with id={}.", id);
            String query = queries.getProperty(GET_BY_ID);
            StudentEntity student = jdbcTemplate.queryForObject(query, 
            (resultSet, rowNum) -> studentMapper.mapRow(resultSet, rowNum), 
            id);
            logger.trace("Student with id={} was received.", student.getId());
            return student;
        } catch (DataAccessException e) {
            String errorMessage = "Getting the database student data by its id failed.";
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
    
    @Override
    public int update(StudentEntity entity) throws DaoException {
        try {
            logger.debug("Update student with id={}.", entity.getId());
            String query = queries.getProperty(UPDATE);
            int rowQuantity = jdbcTemplate.update(query, 
                                       preparedStatement -> {
                                           preparedStatement.setString(1, entity.getFirstName());
                                           preparedStatement.setString(2, entity.getLastName());
                                           preparedStatement.setObject(3, entity.getGroup().getId());
                                           preparedStatement.setInt(4, entity.getId());
                                       });
            logger.debug("Student with id={} was updated.", entity.getId());
            return rowQuantity;
        } catch (DataAccessException e) {
            String errorMessage = "Updating the database studen data failed.";
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
    
    public int deleteById(int id) throws DaoException {
        try {
            logger.debug("Delete student with id={}.", id);
            String query = queries.getProperty(DELETE_BY_ID);
            int rowQuantity = jdbcTemplate.update(query, 
                                                  preparedStatement -> preparedStatement.setInt(1, id));
            logger.trace("Student with id={} was deleted.", id);
            return rowQuantity;
        } catch (DataAccessException e) {
            String errorMessage = "Deleting the database student data failed.";
            logger.error(errorMessage, e);
            throw new DaoException(errorMessage, e);
        }
    }
}
