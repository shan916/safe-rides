package edu.csus.asi.saferides.security;

import edu.csus.asi.saferides.model.Authority;
import edu.csus.asi.saferides.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

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
                mapToGrantedAuthorities(user.getAuthorities()),
                user.isActive(),
                user.getTokenValidFrom()
        );
    }

    /**
     * Helper method to map list of Authority to list of GrantedAuthority
     *
     * @param authorities to map to GrantedAuthority-ies
     * @return list of GrantedAuthority
     */
    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
                .collect(Collectors.toList());
    }
}
