package ua.com.foxminded.university.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackages = "ua.com.foxminded.repository")
@EnableTransactionManagement
@ComponentScan(basePackages = "ua.com.foxminded.university")
@Configuration(proxyBeanMethods = false)
public class AppConfigTest {
    
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
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }
    
    @Bean
    @DependsOn("dataSource")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);
        
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(jpaVendorAdapter);
        factory.setPackagesToScan("ua.com.foxminded.university.entity");
        factory.setDataSource(dataSource());
        factory.setPackagesToScan("ua.com.foxminded.university.entity");
        
        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("javax.persistence.schema-generation.database.action", "drop-and-create");
        jpaProperties.setProperty("javax.persistence.schema-generation.create-source", "script-then-metadata");
        jpaProperties.setProperty("javax.persistence.schema-generation.create-script-source", "test-schema.sql");
        jpaProperties.setProperty("javax.persistence.schema-generation.create-database-schemase", "true");
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        
        factory.setJpaProperties(jpaProperties);
        return factory;
    }
}
