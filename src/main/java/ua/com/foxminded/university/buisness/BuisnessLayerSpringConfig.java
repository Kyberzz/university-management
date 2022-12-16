package ua.com.foxminded.university.buisness;

import java.util.Properties;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
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

@EnableJpaRepositories(basePackages = "ua.com.foxminded.university.buisness.entity.repository")
@EnableTransactionManagement
@PropertySource("/application.properties")
@ComponentScan(basePackages = "ua.com.foxminded.university.buisness")
@Configuration
public class BuisnessLayerSpringConfig {
    
    private static final String MODE_TYPE = "UNSPECIFIED";
    private static final String SHARED_CHACHE_MODE = "jakarta.persistence.sharedCache.mode";
    private static final String DATA_SCRIPT_SOURCE = "data.sql";
    private static final String DATA_LOADING = "jakarta.persistence.sql-load-script-source";
    private static final String DIALECT_TYPE = "org.hibernate.dialect.PostgreSQLDialect";
    private static final String PERSISTENCE_DIALECT = "hibernate.dialect";
    private static final String SCHEMA_GENERATION_ACTION = "jakarta.persistence.schema-generation"
            + ".database.action";
    private static final String ACTION_TYPE = "none";
    private static final String ENTITY_PACKAGE = "ua.com.foxminded.university.buisness.entity";
    private static final String PASSWORD = "spring.datasource.password";
    private static final String USERNAME = "spring.datasource.username";
    private static final String URL = "spring.datasource.url";
    private static final String DRIVER_CLASS_NAME = "spring.datasource.driver-class-name";
    
    Environment environment;
    
    @Autowired
    public BuisnessLayerSpringConfig(Environment environment) {
		this.environment = environment;
	}

	@Bean
    public FlywayMigrationStrategy flywayStrategy() {
        return new FlywayMigrationStrategy() {

            @Override
            public void migrate(Flyway flyway) {
                flyway.baseline();
                flyway.migrate();
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
        jpaProperties.setProperty(SHARED_CHACHE_MODE, MODE_TYPE);
        jpaProperties.setProperty(SCHEMA_GENERATION_ACTION, ACTION_TYPE);
        jpaProperties.setProperty(DATA_LOADING, DATA_SCRIPT_SOURCE);
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


