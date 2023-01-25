package ua.com.foxminded.university.security;

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
import ua.com.foxminded.university.model.UserModel;
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
        try {
            UserModel user = userService.getActiveUserAuthorityByEmail(email);
            String password = user.getPassword();
            String authority = user.getAuthority().getAuthority().toString();
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

            return User.withUsername(email)
                       .roles(authority)
                       .password(password)
                       .passwordEncoder(encoder::encode)
                       .build();
        } catch (ServiceException e) {
            throw new UsernameNotFoundException("Getting user credentials failed.");
        }
    }
}
