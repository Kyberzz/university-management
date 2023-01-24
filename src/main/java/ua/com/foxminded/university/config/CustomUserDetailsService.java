package ua.com.foxminded.university.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.AuthorityModel;
import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.service.AuthorityService;
import ua.com.foxminded.university.service.UserService;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserService<UserModel> userService;
    
    @Autowired
    public CustomUserDetailsService(UserService<UserModel> userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails userDetails = null;
        
        try {
            UserModel user = userService.getUserAuthorityByEmail(email);
            
            if (Boolean.TRUE.equals(user.getIsActive())) {
                String persistedEmail = user.getEmail();
                String password = user.getPassword();
                String authority = user.getAuthority().toString();
                PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                
                userDetails = User.withUsername(persistedEmail)
                                  .roles(authority)
                                  .password(password)
                                  .passwordEncoder(encoder::encode)
                                  .build();
            } else {
                // throw new Exception ?
            }
            return userDetails;
        } catch (ServiceException e) {
            throw new UsernameNotFoundException("Getting user credentials failed.");
        }
    }
}

