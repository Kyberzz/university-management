package ua.com.foxminded.university.security;

import java.util.Collection;
import java.util.function.Supplier;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.method.AbstractFallbackMethodSecurityMetadataSource;
import org.springframework.security.access.method.DelegatingMethodSecurityMetadataSource;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
/*
@Component
public class AccessDecisionVoterAdapter implements AuthorizationManager<Object> {

    public static final int ACCESS_GRANTED = 1;
    public static final int ACCESS_ABSTAIN = 0;
    public static final int ACCESS_DENIED = -1;

    private final AccessDecisionVoter<Object> accessDecisionVoter;
    private final SecurityMetadataSource securityMetadataSource;

    public AccessDecisionVoterAdapter(AccessDecisionVoter<Object> accessDecisionVoter,
            SecurityMetadataSource securityMetadataSource) {
        this.accessDecisionVoter = accessDecisionVoter;
        this.securityMetadataSource = securityMetadataSource;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, Object object) {
        Collection<ConfigAttribute> attributes = this.securityMetadataSource.getAttributes(object);
        int decision = this.accessDecisionVoter.vote(authentication.get(), object, attributes);

        switch (decision) {

        case ACCESS_GRANTED:
            return new AuthorizationDecision(true);
        case ACCESS_DENIED:
            return new AuthorizationDecision(false);
        }
        return null;
    }
}
*/
