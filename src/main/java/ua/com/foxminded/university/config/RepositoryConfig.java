package ua.com.foxminded.university.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = "ua.com.foxminded.university.repository")
@EnableTransactionManagement
@PropertySource("/application.properties")
@ComponentScan(basePackages = "ua.com.foxminded.university")
@Configuration
public class RepositoryConfig {
    
    private static final String DIALECT_TYPE = "org.hibernate.dialect.PostgreSQL10Dialect";
    private static final String PERSISTENCE_DIALECT = "hibernate.dialect";
    private static final String SCHEMA_GENERATION_ACTION = "javax.persistence.schema-generation"
            + ".database.action";
    private static final String ACTION_TYPE = "none";
    private static final String ENTITY_PACKAGE = "ua.com.foxminded.university.entity";
    private static final String PASSWORD = "spring.datasource.password";
    private static final String USERNAME = "spring.datasource.username";
    private static final String URL = "spring.datasource.url";
    private static final String DRIVER_CLASS_NAME = "spring.datasource.driver-class-name";
    
    private Environment environment;
    
    @Autowired
    public RepositoryConfig(Environment environment) {
        this.environment = environment;
    }
    
    @Bean
    public FlywayMigrationStrategy flywayStrategy() {
        return new FlywayMigrationStrategy() {

            @Override
            public void migrate(Flyway flyway) {
                flyway.migrate();
                flyway.baseline();
            }
        };
    }
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = 
                new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPackagesToScan(ENTITY_PACKAGE);
        
        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        
        Properties jpaProperties = new Properties();
        jpaProperties.setProperty(SCHEMA_GENERATION_ACTION, ACTION_TYPE);
        jpaProperties.setProperty(PERSISTENCE_DIALECT, DIALECT_TYPE);
        entityManagerFactory.setJpaProperties(jpaProperties);
        return entityManagerFactory;
    }
    
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty(DRIVER_CLASS_NAME));
        dataSource.setUrl(environment.getProperty(URL));
        dataSource.setUsername(environment.getProperty(USERNAME));
        dataSource.setPassword(environment.getProperty(PASSWORD));
        return dataSource;
    }
    
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
    
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return jpaTransactionManager;
    }
}
