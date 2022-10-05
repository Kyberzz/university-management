package ua.com.foxminded.university.config;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

//@EnableTransactionManagement
// @PropertySource("/jdbc.properties")
@ComponentScan(basePackages = "ua.com.foxminded.university")
@Configuration
public class AppConfig {
    
    private static final String PASSWORD = "jdbc.password";
    private static final String USERNAME = "jdbc.username";
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
     //   entityManagerFactory.setDataSource(dataSource());
     //   entityManagerFactory.setPackagesToScan("ua.com.foxminded.university.entity");
   //     entityManagerFactory.setPersistenceProvider(new HibernatePersistenceProvider());
        
        JpaVendorAdapter vendorAdaptor = new HibernateJpaVendorAdapter();
        entityManagerFactory.setJpaVendorAdapter(vendorAdaptor);
        return entityManagerFactory;
    }
    
    /*
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
    */
    
    /*
    @Bean
    public PlatformTransactionManager transactionManager() {
     //   JpaTransactionManager transactionManager = new JpaTransactionManager();
     //   transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return new DataSourceTransactionManager(dataSource());
    }
    */
    
    
    
    /*
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty(DRIVER_CLASS_NAME));
        dataSource.setUrl( environment.getProperty(URL));
        dataSource.setUsername(environment.getProperty(USERNAME));
        dataSource.setPassword(environment.getProperty(PASSWORD));
        return dataSource;
    }
    */

    /*
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
    */
}
