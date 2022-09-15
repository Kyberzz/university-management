package ua.com.foxminded.university.dao.jdbc;

import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.jdbc.mapper.GroupMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.StudentMapper;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.StudentEntity;

@Slf4j
@Repository
public class StudentJdbcDao implements StudentDao {
    
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
            log.debug("Get group by student id={}.", id);
            String query = queries.getProperty(GET_GROUP_BY_STUDENT_ID);
            StudentEntity studentWithGroupData = jdbcTemplate.queryForObject(query, 
                    (resultSet, rowNum) -> {
                        StudentEntity student = studentMapper.mapRow(resultSet, rowNum);
                        GroupEntity group = groupMapper.mapRow(resultSet, rowNum);
                        student.setGroup(group);
                        return student;
                    }, 
                    id);
            log.trace("Group having student id={} was received.", studentWithGroupData.getId());
            return studentWithGroupData;
        } catch (DataAccessException e) {
            throw new DaoException("Getting group by the student id failed.", e);
        }
    }
    
    @Override
    public int insert(StudentEntity entity) throws DaoException {
        try {
            log.debug("Insert student with id={}.", entity.getId());
            String query = queries.getProperty(INSERT);
            
            int insertedStudentsQuantity = jdbcTemplate.update(query, 
                    preparedStatement -> {
                        preparedStatement.setString(1, entity.getFirstName());
                        preparedStatement.setString(2, entity.getLastName());
                        preparedStatement.setInt(3, entity.getGroup().getId());
                    });
            log.trace("Student with id={} was inserted.", entity.getId());
            return insertedStudentsQuantity;
        } catch (DataAccessException e) {
            throw new DaoException("Inserting the student to the database failed.", e);
        }
    }
    
    @Override
    public StudentEntity getById(int id) throws DaoException {
        try {
            log.debug("Get student by id={}.", id);
            String query = queries.getProperty(GET_BY_ID);
            StudentEntity student = jdbcTemplate.queryForObject(query,
                    (resultSet, rowNum) -> studentMapper.mapRow(resultSet, rowNum), 
                    id);
            log.trace("Student with id={} was received.", student.getId());
            return student;
        } catch (DataAccessException e) {
            throw new DaoException("Getting the student by its id failed.", e);
        }
    }

    @Override
    public int update(StudentEntity entity) throws DaoException {
        try {
            log.debug("Update student with id={}.", entity.getId());
            String query = queries.getProperty(UPDATE);
            int updatedStudentsQuantity = jdbcTemplate.update(query, 
                    preparedStatement -> {
                        preparedStatement.setString(1, entity.getFirstName());
                        preparedStatement.setString(2, entity.getLastName());
                        preparedStatement.setObject(3, entity.getGroup().getId());
                        preparedStatement.setInt(4, entity.getId());
                    });
            log.debug("Student with id={} was updated.", entity.getId());
            return updatedStudentsQuantity;
        } catch (DataAccessException e) {
            throw new DaoException("Updating the student data failed.", e);
        }
    }
    
    public int deleteById(int id) throws DaoException {
        try {
            log.debug("Delete student by id={}.", id);
            String query = queries.getProperty(DELETE_BY_ID);
            int deletedStudentsQuantity = jdbcTemplate.update(query,
                    preparedStatement -> preparedStatement.setInt(1, id));
            log.trace("Student with id={} was deleted.", id);
            return deletedStudentsQuantity;
        } catch (DataAccessException e) {
            throw new DaoException("Deleting the student data failed.", e);
        }
    }
}
