package ua.com.foxminded.university.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.sql.DataSource;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Profile("test")
@EnableJpaRepositories(basePackages = "ua.com.foxminded.university.repository", 
                       bootstrapMode = BootstrapMode.LAZY)
@EnableTransactionManagement
@ComponentScan(basePackages = "ua.com.foxminded.university.repository")
@Configuration
public class RepositoryTestConfig {
    
    private static final String MODE_TYPE = "UNSPECIFIED";
    private static final String SHARED_CHACHE_MODE = "jakarta.persistence.sharedCache.mode";
    private static final String DIALECT_TYPE = "org.hibernate.dialect.H2Dialect";
    private static final String PERSISTENCE_DIALECT = "hibernate.dialect";
    private static final String PERMISSION = "true";
    private static final String SCHEMA_CREATION_ACCESS = "jakarta.persistence.schema-generation"
            + ".create-database-schemase";
    private static final String CREATION_SCHEMA_PATH = "test-schema.sql";
    private static final String CREATION_SCHEMA_SCRIPT_SOURCE = "jakarta.persistence"
            + ".schema-generation.create-script-source";
    private static final String SOURCE_TYPE = "script-then-metadata";
    private static final String CREATION_SCHEMA_SOURCE = "jakarta.persistence.schema-generation"
            + ".create-source";
    private static final String SCHEMA_GENERATION_ACTION = "jakarta.persistence.schema-generation"
            + ".database.action";
    private static final String ACTION_TYPE = "drop-and-create";
    private static final String ENTITY_PACKAGE = "ua.com.foxminded.university.entity";
    
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
    
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
    
    @Bean
    @DependsOn("dataSource")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);
        
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(jpaVendorAdapter);
        factory.setPackagesToScan(ENTITY_PACKAGE);
        factory.setDataSource(dataSource());
        factory.setSharedCacheMode(SharedCacheMode.UNSPECIFIED);
        
        Properties jpaProperties = new Properties();
        
        jpaProperties.setProperty(SCHEMA_GENERATION_ACTION, ACTION_TYPE);
        jpaProperties.setProperty(CREATION_SCHEMA_SOURCE, SOURCE_TYPE);
        jpaProperties.setProperty(CREATION_SCHEMA_SCRIPT_SOURCE, CREATION_SCHEMA_PATH);
        jpaProperties.setProperty(SCHEMA_CREATION_ACCESS, PERMISSION);
        jpaProperties.setProperty(PERSISTENCE_DIALECT, DIALECT_TYPE);
        jpaProperties.setProperty(SHARED_CHACHE_MODE, MODE_TYPE);
        
        factory.setJpaProperties(jpaProperties);
        return factory;
    }
    
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }
}
