package ua.com.foxminded.university.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.model.Authority;

@EnableWebSecurity
@RequiredArgsConstructor
@PropertySource("classpath:user-details-manager-config.properties")
@EnableConfigurationProperties(UserDetailsManagerConfig.class)
public class SecurityConfig {

    public static final String STUDENT = Authority.STUDENT.toString();
    public static final String TEACHER = Authority.TEACHER.toString();
    public static final String STAFF = Authority.STAFF.toString();
    public static final String ADMIN = Authority.ADMIN.toString();
    
    @Bean 
    public UserDetailsManager userDetailsManager(DataSource dataSource, 
                                                 UserDetailsManagerConfig config) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setCreateAuthoritySql(config.getCreateAuthoritySql());
        manager.setDeleteUserSql(config.getDeleteUserSql());
        manager.setDeleteUserAuthoritiesSql(config.getDeleteUserAuthoritiesSql());
        manager.setGroupAuthoritiesByUsernameQuery(config.getGroupAuthoritiesByEmailQuery());
        manager.setUsersByUsernameQuery(config.getUsersByEmailQuery());
        manager.setAuthoritiesByUsernameQuery(config.getAuthoritiesByEmailQuery());
        manager.setUpdateUserSql(config.getUpdateUserQuery());
        manager.setCreateUserSql(config.getCreateUserSql());
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
                .mvcMatchers("/courses/{courseId:\\d+}/assign-teacher*")
                    .hasAnyRole(ADMIN, STAFF)
                .mvcMatchers("/courses/{courseId:\\d+}/deassign-teacher*")
                    .hasAnyRole(ADMIN, STAFF)
                .mvcMatchers("/courses/**").hasAnyRole(ADMIN)
                .mvcMatchers("/groups/{id:\\d+}/update").hasAnyRole(ADMIN, STAFF)
                .mvcMatchers("/groups/{id:\\d+}").hasAnyRole(ADMIN, STAFF)
                .mvcMatchers("/groups/create*").hasAnyRole(ADMIN, STAFF)
                .mvcMatchers("/groups/list*").hasAnyRole(ADMIN, STAFF, TEACHER, STUDENT)
                .mvcMatchers("/groups/**").hasAnyRole(ADMIN)
                .mvcMatchers("/students/**").hasAnyRole(ADMIN)
//                .mvcMatchers("/**").hasAnyAuthority(ADMIN)
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