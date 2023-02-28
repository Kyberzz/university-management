package ua.com.foxminded.university.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@PropertySource("/security-config-queries.properties")
public class SecurityConfig {

    public static final String CREATE_AUTHORITY_SLQ = "createAuthoritySql"; 
    public static final String DELETE_USER_AUTHORITIES_QUERY = "deleteUserAuthoritiesQuery"; 
    public static final String UPDATE_USER_SQL = "updateUserQuery"; 
    public static final String GROUP_AUTHORITIES_BY_EMAIL_QUERY = "groupAuthoritiesByEmailQuery"; 
    public static final String AUTHORITIES_BY_EMAIL_QUERY = "authoritiesByEmailQuery";
    public static final String DELETE_USER_SQL = "deleteUserSql";
    public static final String USERS_BY_EMAIL_QUERY = "usersByEmailQuery";
    public static final String STUDENT = "STUDENT";
    public static final String TEACHER = "TEACHER";
    public static final String STAFF = "STAFF";
    public static final String ADMIN = "ADMIN";
    
    private final Environment environment;
    private final DataSource dataSource;
    
    @Bean 
    public UserDetailsManager userDetailsManager() throws Exception {
        JdbcUserDetailsManager detailsManager = new JdbcUserDetailsManager(dataSource);
        detailsManager.setCreateAuthoritySql(environment.getProperty(CREATE_AUTHORITY_SLQ));
        detailsManager.setDeleteUserSql(environment.getProperty(DELETE_USER_SQL));
        detailsManager.setDeleteUserAuthoritiesSql(
                environment.getProperty(DELETE_USER_AUTHORITIES_QUERY));
        detailsManager.setGroupAuthoritiesByUsernameQuery(
                environment.getProperty(GROUP_AUTHORITIES_BY_EMAIL_QUERY));
        detailsManager.setUsersByUsernameQuery(environment.getProperty(USERS_BY_EMAIL_QUERY));
        detailsManager.setAuthoritiesByUsernameQuery(
                environment.getProperty(AUTHORITIES_BY_EMAIL_QUERY));
        detailsManager.setUpdateUserSql(environment.getProperty(UPDATE_USER_SQL));
        return detailsManager;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                .mvcMatchers("/", "/index", "/images/**").permitAll()
                .mvcMatchers("/timetables/**").hasAnyRole(STUDENT, TEACHER, STAFF, ADMIN)
                .mvcMatchers("/teachers/**").hasAnyRole(TEACHER, STAFF, ADMIN)
                .mvcMatchers("/students/**").hasAnyRole(TEACHER, STAFF, ADMIN)
                .mvcMatchers("/groups/**").hasAnyRole(TEACHER, STAFF, ADMIN)
                .mvcMatchers("/courses/**").hasAnyRole(TEACHER, STAFF, ADMIN)
                .mvcMatchers("/users/**").hasAnyRole(ADMIN)
                .anyRequest().authenticated())
            .formLogin(form -> form.loginPage("/login").permitAll())
            .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/"));
        return http.build();
    }
}
