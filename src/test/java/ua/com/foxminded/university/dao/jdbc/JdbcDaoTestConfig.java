package ua.com.foxminded.university.dao.jdbc;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class JdbcDaoTestConfig {
    
    @Value("/student-queries.properties")
    private Resource studentQueriesResource;
    
    @Value("/test-student-queries.properties")
    private Resource testStudentQueriesResource;
    
    @Value("/test-course-queries.properties")
    private Resource testCoruceQueriesResource;
    
    @Value("/course-queries.properties")
    private Resource courseQueriesResource;
    
    @Bean
    public Properties courseQueries() throws IOException {
        Properties courseQueries = new Properties();
        courseQueries.load(courseQueriesResource.getInputStream());
        return courseQueries;
    }
    
    @Bean
    public Properties testCourseQueries() throws IOException {
        Properties testCourseProperties = new Properties();
        testCourseProperties.load(testCoruceQueriesResource.getInputStream());
        return testCourseProperties;
    }
    
    @Bean
    public Properties testStudentQueries() throws IOException {
        Properties testStudentQueries = new Properties();
        testStudentQueries.load(testStudentQueriesResource.getInputStream());
        return testStudentQueries;
    }
    
    @Bean 
    public Properties studentQueries() throws IOException {
        Properties studentQueries = new Properties();
        studentQueries.load(studentQueriesResource.getInputStream());
        return studentQueries;
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
    
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().generateUniqueName(true)
                                            .setType(EmbeddedDatabaseType.H2)
                                            .setScriptEncoding("UTF-8")
                                            .addScript("/test-db-schema.sql")
                                            .addScript("/test-db-data.sql")
                                            .build();
    }
}
