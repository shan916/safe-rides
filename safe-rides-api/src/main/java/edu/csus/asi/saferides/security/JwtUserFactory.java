package edu.csus.asi.saferides.security;

import edu.csus.asi.saferides.model.AuthorityName;
import edu.csus.asi.saferides.model.User;
import edu.csus.asi.saferides.utility.Util;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class to create JWTUser objects
 */
public final class JwtUserFactory {

    /**
     * Empty constructor
     */
    private JwtUserFactory() {
    }

    /**
     * Create JWTUser object
     *
     * @param user user
     * @return JWTUser for user
     */
    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                null,
                Util.mapToGrantedAuthorities(user.getAuthorityLevel()),
                user.isActive(),
                user.getTokenValidFrom()
        );
    }
}
