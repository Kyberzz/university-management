package ua.com.foxminded.university.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.model.Authority;

@EnableWebSecurity
@RequiredArgsConstructor
@PropertySource("classpath:security-config-queries.properties")
public class SecurityConfig {

    public static final String CREATE_USER_SLQ = "createUserSql"; 
    public static final String CREATE_AUTHORITY_SLQ = "createAuthoritySql"; 
    public static final String DELETE_USER_AUTHORITIES_SQL = "deleteUserAuthoritiesSql"; 
    public static final String UPDATE_USER_SQL = "updateUserQuery"; 
    public static final String GROUP_AUTHORITIES_BY_EMAIL_QUERY = "groupAuthoritiesByEmailQuery"; 
    public static final String AUTHORITIES_BY_EMAIL_QUERY = "authoritiesByEmailQuery";
    public static final String DELETE_USER_SQL = "deleteUserSql";
    public static final String USERS_BY_EMAIL_QUERY = "usersByEmailQuery";
    public static final String STUDENT = Authority.STUDENT.toString();
    public static final String TEACHER = Authority.TEACHER.toString();
    public static final String STAFF = Authority.STAFF.toString();
    public static final String ADMIN = Authority.ADMIN.toString();
    
    private final Environment environment;
    
    @Bean 
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setCreateAuthoritySql(environment.getProperty(CREATE_AUTHORITY_SLQ));
        manager.setDeleteUserSql(environment.getProperty(DELETE_USER_SQL));
        manager.setDeleteUserAuthoritiesSql(
                environment.getProperty(DELETE_USER_AUTHORITIES_SQL));
        manager.setGroupAuthoritiesByUsernameQuery(
                environment.getProperty(GROUP_AUTHORITIES_BY_EMAIL_QUERY));
        manager.setUsersByUsernameQuery(environment.getProperty(USERS_BY_EMAIL_QUERY));
        manager.setAuthoritiesByUsernameQuery(
                environment.getProperty(AUTHORITIES_BY_EMAIL_QUERY));
        manager.setUpdateUserSql(environment.getProperty(UPDATE_USER_SQL));
        manager.setCreateUserSql(environment.getProperty(CREATE_USER_SLQ));
        return manager;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                .mvcMatchers("/", "/index*", "/images/**").permitAll()
                .mvcMatchers("/users/**").hasAnyRole(ADMIN)
                .mvcMatchers("/courses/list*").hasAnyRole(ADMIN, STAFF, TEACHER, STUDENT)
                .mvcMatchers("/courses/{id:\\d+}").hasAnyRole(ADMIN, STAFF, TEACHER)
                .mvcMatchers("/courses/create*").hasAnyRole(ADMIN, STAFF)
                .mvcMatchers("/courses/update*").hasAnyRole(ADMIN, STAFF)
                .mvcMatchers("/courses/**").hasAnyRole(ADMIN)
//                .mvcMatchers("/timetables/**").hasAnyRole(ADMIN)
//                .mvcMatchers("/teachers/**").hasAnyRole(ADMIN)
                .mvcMatchers("/students/**").hasAnyRole(ADMIN)
//                .mvcMatchers("/groups/**").hasAnyRole(ADMIN)
                .anyRequest().authenticated()
                )
            .formLogin(form -> form.loginPage("/login")
                                   .permitAll()
                                   .defaultSuccessUrl("/", true))
            .logout(logout -> logout.logoutUrl("/logout")
                                    .logoutSuccessUrl("/index"));
        return http.build();
    }
}
