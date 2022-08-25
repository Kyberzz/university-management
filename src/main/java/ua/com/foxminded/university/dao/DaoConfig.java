package ua.com.foxminded.university.dao;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import ua.com.foxminded.university.dao.jdbc.JdbcCourseDao;
import ua.com.foxminded.university.dao.jdbc.JdbcGroupDao;
import ua.com.foxminded.university.dao.jdbc.JdbcStudentDao;

@PropertySource("/student-queries.properties")
@PropertySource({"/jdbc.properties", "/group-queries.properties"})
@Configuration
public class DaoConfig {
    
    private static final String SCHEMA_FILENAME = "db-schema.sql";
    private static final String PASSWORD = "jdbc.password";
    private static final String USERNAME = "jdbc.username";
    private static final String URL = "jdbc.url";
    private static final String DRIVER_CLASS_NAME = "jdbc.driverClassName";
    
    @Autowired
    Environment environment;
    
    @Value("/test-db-data.sql")
    private Resource dataScript;
    
    @Value("/student-queries.properties")
    private Resource studentQueriesResource;
    
    
    @Bean
    public StudentDao studentDao() throws IOException {
        return new JdbcStudentDao(jdbcTemplate(), studentQueries());
    }
    
    @Bean
    public GroupDao groupDao() {
        return new JdbcGroupDao(environment, jdbcTemplate());
    }
    
    @Bean
    public CourseDao courseDao() throws IOException {
        return new JdbcCourseDao(coursePropertiesFactoryBean().getObject(), jdbcTemplate());
    }
    
    @Bean
    public Properties studentQueries() throws IOException {
        Properties studentQueries = new Properties();
        studentQueries.load(studentQueriesResource.getInputStream());
        return studentQueries;
    }
    
    @Bean
    public PropertiesFactoryBean coursePropertiesFactoryBean() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/course-queries.properties"));
        return propertiesFactoryBean;
        
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
    
    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource());
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }
    
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty(DRIVER_CLASS_NAME));
        dataSource.setUrl( environment.getProperty(URL));
        dataSource.setUsername(environment.getProperty(USERNAME));
        dataSource.setPassword(environment.getProperty(PASSWORD));
        return dataSource;
    }
    
    private DatabasePopulator databasePopulator() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.setContinueOnError(true);
        databasePopulator.addScript(new ClassPathResource(SCHEMA_FILENAME));
        databasePopulator.addScript(dataScript);
        return databasePopulator;
    }
}
