package ua.com.foxminded.university.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    public static final String STUDENT = "STUDENT";
    public static final String TEACHER = "TEACHER";
    public static final String STAFF = "STAFF";
    public static final String ADMIN = "ADMIN";

    @Bean 
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager detailsManager = new JdbcUserDetailsManager();
        detailsManager.setDataSource(dataSource);
        return detailsManager;
    }

    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/", "/index", "/images/**").permitAll()
            .antMatchers("/timetables/**").hasAnyRole(STUDENT, TEACHER, STAFF, ADMIN)
            .antMatchers("/teachers/**").hasAnyRole(TEACHER, STAFF, ADMIN)
            .antMatchers("/students/**").hasAnyRole(TEACHER, STAFF, ADMIN)
            .antMatchers("/groups/**").hasAnyRole(TEACHER, STAFF, ADMIN)
            .antMatchers("/courses/**").hasAnyRole(TEACHER, STAFF, ADMIN)
            .antMatchers("/users/**").hasAnyRole(ADMIN)
            .anyRequest().authenticated()
            .and()
            .exceptionHandling().accessDeniedPage("/")
            .and()
            .formLogin(form -> form.loginPage("/login").permitAll().failureUrl("/"))
            .logout(logout -> logout.logoutUrl("/logout")
                                    .logoutSuccessUrl("/"));
        return http.build();
    }
}
