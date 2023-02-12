package ua.com.foxminded.university.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private static final String AUTHORITIES_BY_EMAIL_QUERY = "select email, authority "
            + "from university.authorities "
            + "join university.users on users.id=authorities.user_id "
            + "where email = ?";
    private static final String USERS_BY_EMAIL_QUERY = "select email, password, is_Active "
            + "from university.users where email = ?";
    
    
    
    @Bean
    public AccessDecisionVoter<Object> roleHierarchyVoter() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_STUDENT\n");
                         //    + "ROLE_STAFF > ROLE_TEACHER\n"
                         //    + "ROLE_TEACHER > ROLE_STUDENT\n");
        return new RoleHierarchyVoter(hierarchy);
    }
    
    @Bean 
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
        manager.setDataSource(dataSource);
        manager.setUsersByUsernameQuery(USERS_BY_EMAIL_QUERY);
        manager.setAuthoritiesByUsernameQuery(AUTHORITIES_BY_EMAIL_QUERY);
        return manager;
    }

    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(request -> request.mvcMatchers("/", "/index" ,"/images/**")
//                                                     .permitAll()
//                                                     .anyRequest()
//                                                     .authenticated())
        http.authorizeRequests().antMatchers("/", "/index", "/images/**").permitAll()
                                .antMatchers("/timetables/**").hasAnyRole("STUDENT")
                                .antMatchers("/users/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                                .and()
                                .formLogin(form -> form.loginPage("/login").permitAll())
                                .logout(logout -> logout.logoutUrl("/logout")
                                                        .logoutSuccessUrl("/"));
                                
        return http.build();
    }
}
