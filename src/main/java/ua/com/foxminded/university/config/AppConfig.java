package ua.com.foxminded.university.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
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

@EnableJpaRepositories(basePackages = "ua.com.foxminded.university.repository")
@EnableTransactionManagement
@PropertySource("/jdbc.properties")
@ComponentScan(basePackages = "ua.com.foxminded.university")
@Configuration
public class AppConfig {
    
    private static final String ENTITY_PACKAGE = "ua.com.foxminded.university.entity";
    private static final String PASSWORD = "jdbc.password";
    private static final String USERNAME = "jdbc.user";
    private static final String URL = "jdbc.url";
    private static final String DRIVER_CLASS_NAME = "jdbc.driverClassName";
    
    private Environment environment;
    
    @Autowired
    public AppConfig(Environment environment) {
        this.environment = environment;
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
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "none");
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
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
