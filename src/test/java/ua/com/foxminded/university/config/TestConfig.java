package ua.com.foxminded.university.config;

import javax.sql.DataSource;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;

@TestConfiguration
public class TestConfig {
    
    public static final String SCHEMA = "UNIVERSITY";
    
    @Bean
    public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection(
            DataSource dataSource) {
        DatabaseDataSourceConnectionFactoryBean connectionFactory = 
                new DatabaseDataSourceConnectionFactoryBean();
        connectionFactory.setDataSource(dataSource);
        connectionFactory.setSchema(SCHEMA);
        return connectionFactory;
    }
}
