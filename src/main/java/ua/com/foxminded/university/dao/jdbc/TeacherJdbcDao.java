package ua.com.foxminded.university.dao.jdbc;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.TeacherDao;
import ua.com.foxminded.university.dao.jdbc.mapper.CourseMapper;
import ua.com.foxminded.university.dao.jdbc.mapper.TeacherMapper;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;

@Slf4j
@Repository
public class TeacherJdbcDao implements TeacherDao {
    
    private static final String GET_COURSE_LIST_BY_TEACHER_ID = "teacher.getCourseListByTeacherId";
    private static final String DELETE_BY_ID = "teacher.deleteById";
    private static final String UPDATE = "teacher.update";
    private static final String INSERT = "teacher.insert";
    private static final String GET_BY_ID = "teacher.getById";
    
    private JdbcTemplate jdbcTemplate;
    private Environment queries;
    private TeacherMapper teacherMapper;
    private CourseMapper courseMapper;
    
    @Autowired
    public TeacherJdbcDao(JdbcTemplate jdbcTemplate, Environment queries, TeacherMapper teacherMapper,
            CourseMapper courseMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.queries = queries;
        this.teacherMapper = teacherMapper;
        this.courseMapper = courseMapper;
    }

    @Override
    public TeacherEntity getCourseListByTeacherId(int id) throws DaoException {
        try {
            log.debug("Get courses list by teacher id={}.", id);
            String query = queries.getProperty(GET_COURSE_LIST_BY_TEACHER_ID);
            TeacherEntity teacherWithCourseList = jdbcTemplate.query(query, 
                    preparedStatement -> preparedStatement.setInt(1, id), 
                    (resultSet) -> {
                        TeacherEntity teacher = null;
                        
                        while (resultSet.next()) {
                            if (teacher == null) {
                                teacher = teacherMapper.mapRow(resultSet, resultSet.getRow());
                                teacher.setCourseList(new ArrayList<>());
                            }

                            CourseEntity course = courseMapper.mapRow(resultSet, resultSet.getRow());
                            teacher.getCourseList().add(course);
                        }
                        return teacher;
                    });
            log.trace("Courses list of teacher id={} was received");
            return teacherWithCourseList;
        } catch (DataAccessException e) {
            throw new DaoException("Getting the course list data by the teacher id from the database failed.",
                                   e);
        }
    }
    
    @Override
    public int deleteById(int id) throws DaoException {
        try {
            log.debug("Delete teacher with id={}.", id);
            String query = queries.getProperty(DELETE_BY_ID);
            int deletedTeachersQuantity = jdbcTemplate.update(query, 
                    preparedStatement -> preparedStatement.setInt(1, id));
            log.trace("Teacher with id={} was deleted.", id);
            return deletedTeachersQuantity;
        } catch (DataAccessException e) {
            throw new DaoException("Deleting the database teacher data failed.", e);
        }
    }
    
    @Override
    public int update(TeacherEntity entity) throws DaoException {
        try {
            log.debug("Udate teacher with id={}.", entity.getId());
            String query = queries.getProperty(UPDATE);
            int updatedTeachersQuantity = jdbcTemplate.update(query, 
                    preparedStatement -> {
                        preparedStatement.setString(1, entity.getFirstName());
                        preparedStatement.setString(2, entity.getLastName());
                        preparedStatement.setInt(3, entity.getId());
                    });
            log.trace("Teacher with id={} was updated.", entity.getId());
            return updatedTeachersQuantity;
        } catch (DataAccessException e){
            throw new DaoException("Updating the database teacher data failed.", e);
        }
    }
    
    @Override
    public TeacherEntity getById(int id) throws DaoException {
        try {
            log.debug("Get teacher with id={}.", id);
            String query = queries.getProperty(GET_BY_ID);
            TeacherEntity teacherEntity = jdbcTemplate.queryForObject(query,
                    (resultSet, rowNum) -> teacherMapper.mapRow(resultSet, rowNum),
                    id);
            log.trace("Teacher with id={} was received.", teacherEntity.getId());
            return teacherEntity;
        } catch (DataAccessException e) {
            throw new DaoException("Getting the database teacher data failed.", e);
        }
    }
    
    @Override
    public int insert(TeacherEntity entity) throws DaoException {
        try {
            log.debug("Insert teacher with id={} to the database.", entity.getId());
            String query = queries.getProperty(INSERT);
            int insertedTeachersQuantity = jdbcTemplate.update(query, 
                    preparedStatement -> {
                        preparedStatement.setString(1, entity.getFirstName());
                        preparedStatement.setString(2, entity.getLastName());
                    });
            log.trace("Teacher with id={} was added to the database.", entity.getId());
            return insertedTeachersQuantity;
        } catch (DataAccessException e) {
            throw new DaoException("Inserting the database teacher data failed.", e);
        }
    }
}
