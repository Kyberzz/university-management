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
import ua.com.foxminded.university.model.CredentialsModel;
import ua.com.foxminded.university.service.CredentialsService;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private CredentialsService<CredentialsModel> credentialsService;

    @Autowired
    public CustomUserDetailsService(CredentialsService<CredentialsModel> credentialsService) {
        this.credentialsService = credentialsService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        try {
            CredentialsModel credentials = credentialsService.getByEmail(email);
            String persistedEmail = credentials.getEmail();
            String authority = credentials.getAuthority();
            String password = credentials.getPassword();
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            
            return User.withUsername(persistedEmail)
                       .roles(authority)
                       .password(password)
                       .passwordEncoder(encoder::encode)
                       .build();
        } catch (ServiceException e) {
            throw new UsernameNotFoundException("Getting user credentials failed.");
        }
    }
}
