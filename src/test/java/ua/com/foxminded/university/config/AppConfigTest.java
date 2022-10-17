package ua.com.foxminded.university.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@ComponentScan(basePackages = "ua.com.foxminded.university")
//@PropertySource("/jdbc.properties")
@Configuration(proxyBeanMethods = false)
public class AppConfigTest {
    
    private static final String ENTITY_PACKAGE = "ua.com.foxminded.university";
    
    @Autowired
    private Environment environment;
    
    @Value("/db-schema.sql")
    private Resource schema;
    
    @Value("/test-db-data.sql")
    private Resource data;
    
    
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return jpaTransactionManager;
    }
    
    
    
    
    @Bean
    @DependsOn("dataSource")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = 
                new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPackagesToScan(ENTITY_PACKAGE);
        
        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        
        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("javax.persistence.schema-generation.database.action", "create");
     //   jpaProperties.setProperty("javax.persistence.schema-generation.create-source", "script");
        jpaProperties.setProperty("javax.persistence.schema-generation.create-script-source", 
                                  "schema.sql");
    //    jpaProperties.setProperty("javax.persistence.sql-load-script-source", "test-data.sql"); 
        
        
      //  jpaProperties.setProperty("hibernate.hbm2ddl.import_files", "test-db-schema.sql");
    //    jpaProperties.setProperty("hibernate.hbm2ddl.import_files", "test-db-data.sql");
     //   jpaProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
      //  jpaProperties.setProperty("javax.persistence.sql-load-script-source", "test-db-data.sql");
      //  jpaProperties.setProperty("hibernate.hbm2ddl.auto", "none");
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
        //     jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        
     //   jpaProperties.setProperty("hibernate.show_sql", "true");
        
        entityManagerFactory.setJpaProperties(jpaProperties);
        return entityManagerFactory;
    }
    
    /*
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                                            .generateUniqueName(true)
                                            .setType(EmbeddedDatabaseType.H2)
                                            .setScriptEncoding("UTF-8")
                                        //    .addScript("/test-db-schema.sql")
                                        //    .addScript("/test-db-data.sql")
                                          //  .continueOnError(true)
                                            .build();
    }
    */

    /*
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:~/test");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }
    */
      /*  
    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource());
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }
        */ 
    
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getProperty("jdbc.url"));
        dataSource.setUsername(environment.getProperty("jdbc.user"));
        dataSource.setPassword(environment.getProperty("jdbc.password"));
        return dataSource;
    }
    
    /*
    private DatabasePopulator databasePopulator() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.setContinueOnError(true);
        databasePopulator.addScript(schema);
        databasePopulator.addScript(data);
        return databasePopulator;
    }
    */
}
