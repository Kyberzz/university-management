package ua.com.foxminded.university.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    private final TemplateEngine templateEngine;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, 
                                                                              ServletException {
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        String email = userDetails.getUsername();
        WebContext context = new WebContext(request, response, request.getServletContext());
        context.setVariable("email", email);
        String renderedTemplate = templateEngine.process("index", context);
        
        response.setContentType("text/html");
        response.getWriter().write(renderedTemplate);
    }
}
