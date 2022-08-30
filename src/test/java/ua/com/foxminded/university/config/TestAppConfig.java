package ua.com.foxminded.university.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@PropertySource({"/queries.properties", "/test-queries.properties"})
@Configuration
public class TestAppConfig {
    
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
