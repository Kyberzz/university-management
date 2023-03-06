package ua.com.foxminded.university.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableJpaRepositories(basePackages = "ua.com.foxminded.university.repository")
@EnableTransactionManagement
@RequiredArgsConstructor
@Profile("prod")
public class RepositoryConfig {

    private static final String SCHEMA_NAME = "university";
    private static final String MODE_TYPE = "UNSPECIFIED";
    private static final String SHARED_CHACHE_MODE = "jakarta.persistence.sharedCache.mode";
    private static final String DIALECT_TYPE = "org.hibernate.dialect.PostgreSQLDialect";
    private static final String PERSISTENCE_DIALECT = "hibernate.dialect";
    private static final String SCHEMA_GENERATION_ACTION = "jakarta.persistence"
            + ".schema-generation.database.action";
    private static final String ACTION_TYPE = "none";
    private static final String ENTITY_PACKAGE = "ua.com.foxminded.university.entity";
    
    @Bean
    public FlywayConfigurationCustomizer flywayConfigurationCustomizer() {
        return configuration -> configuration.schemas(SCHEMA_NAME);
    }

    @Bean
    public FlywayMigrationStrategy flywayStrategy() {
        return Flyway::migrate;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = 
                new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan(ENTITY_PACKAGE);

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty(SCHEMA_GENERATION_ACTION, ACTION_TYPE);
        jpaProperties.setProperty(PERSISTENCE_DIALECT, DIALECT_TYPE);
        jpaProperties.setProperty(SHARED_CHACHE_MODE, MODE_TYPE);
        
        entityManagerFactory.setJpaProperties(jpaProperties);
        return entityManagerFactory;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(
                entityManagerFactory(dataSource).getObject());
        return jpaTransactionManager;
    }
}
