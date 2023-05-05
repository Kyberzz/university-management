package ua.com.foxminded.university.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties
@Getter
@Setter
public class UserDetailsManagerConfig {

    private String createUserSql; 
    private String createAuthoritySql; 
    private String deleteUserAuthoritiesSql; 
    private String updateUserQuery; 
    private String groupAuthoritiesByEmailQuery; 
    private String authoritiesByEmailQuery;
    private String deleteUserSql;
    private String usersByEmailQuery;
}
