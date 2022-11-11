package ua.com.foxminded.university.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAutoConfiguration
@PropertySource("/application.properties")
@EnableJpaRepositories(basePackages = "ua.com.foxminded.university.repository", 
                       bootstrapMode = BootstrapMode.LAZY)
@EnableTransactionManagement
@ComponentScan(basePackages = "ua.com.foxminded.university")
@Configuration(proxyBeanMethods = false)
public class RepositoryConfigTest {
    private static final String ENTITY_PACKAGE = "ua.com.foxminded.university.entity";
    private static final String PASSWORD = "spring.datasource.password";
    private static final String USERNAME = "spring.datasource.username";
    private static final String URL = "spring.datasource.url";
    private static final String DRIVER_CLASS_NAME = "spring.datasource.driver-class-name";
    
    @Autowired
    private Environment environment;
    
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
    
 //  @Bean(initMethod = "migrate")
    /*
    @Bean
    public Flyway flyway() {
        Flyway flyway = Flyway.configure().dataSource(dataSource())
                                          .cleanDisabled(false)
                                          .baselineOnMigrate(true)
                                          .load();
        //flyway.clean();
        flyway.migrate();
      
    //    FluentConfiguration config = Flyway.configure();
    //    config.cleanDisabled(false);
    //    config.baselineOnMigrate(true);
    //    config.dataSource(dataSource());
    //    Flyway flyway = new Flyway(config);
    //    flyway.clean();
   //     flyway.migrate();
        
        return flyway;
        
    }
    */
    @Bean
    public FlywayMigrationStrategy flywayStrategy() {
        FlywayMigrationStrategy strategy = new FlywayMigrationStrategy() {
            
            @Override
            public void migrate(Flyway flyway) {
                flyway.clean();
                flyway.migrate();
            }
        };
        return strategy;
    }
    
    /*
    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        FlywayMigrationStrategy strategy = new FlywayMigrationStrategy() {
            
            @Override
            public void migrate(Flyway flyway) {
                Flyway.configure().dataSource(dataSource());
                flyway.clean();
                flyway.migrate();
            }
        };
        return strategy;
    }
    */
    
    /*
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }
    */
    @Bean
    @DependsOn("flywayStrategy")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);
        
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(jpaVendorAdapter);
        factory.setPackagesToScan("ua.com.foxminded.university.entity");
        factory.setDataSource(dataSource());
        
        Properties jpaProperties = new Properties();
//        jpaProperties.setProperty("javax.persistence.schema-generation.database.action", "drop-and-create");
        jpaProperties.setProperty("javax.persistence.schema-generation.database.action", "none");
     //   jpaProperties.setProperty("javax.persistence.schema-generation.create-source", "script-then-metadata");
     //   jpaProperties.setProperty("javax.persistence.schema-generation.create-script-source", "test-schema.sql");
     //   jpaProperties.setProperty("javax.persistence.schema-generation.create-database-schemase", "true");
    //    jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
          jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
        
        factory.setJpaProperties(jpaProperties);
        return factory;
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
}
