package edu.csus.asi.saferides.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.csus.asi.saferides.serialization.LocalDateTimeDeserializer;
import edu.csus.asi.saferides.serialization.LocalDateTimeSerializer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * JWT user object that can be mapped to from an application user or rider to be used in generating a token
 */
public class JwtUser implements UserDetails {

    private static final long serialVersionUID = -8915361252628832537L;
    private final Long id;
    private final String username;
    private final String firstname;
    private final String lastname;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean enabled;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final LocalDateTime lastPasswordResetDate;

    /**
     * Instantiate a new JWTUser object
     *
     * @param id                    database primary key id
     * @param username              oneCardId or username
     * @param firstname             first name
     * @param lastname              last name
     * @param password              password
     * @param authorities           authorities / roles
     * @param enabled               enabled flag or active
     * @param lastPasswordResetDate date of last password reset
     */
    public JwtUser(
            Long id,
            String username,
            String firstname,
            String lastname,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            boolean enabled,
            LocalDateTime lastPasswordResetDate
    ) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    /**
     * Get id
     *
     * @return id
     */
    @JsonIgnore
    public Long getId() {
        return id;
    }

    /**
     * Get username
     *
     * @return username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * returns true
     *
     * @return true
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        throw new UnsupportedOperationException();
    }

    /**
     * returns true
     *
     * @return true
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        throw new UnsupportedOperationException();
    }

    /**
     * returns true
     *
     * @return true
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get firstName
     *
     * @return first name
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Get lastName
     *
     * @return last name
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Get password
     *
     * @return password
     */
    @JsonIgnore
    @Override
    public String getPassword() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get authorities
     *
     * @return authorities / roles
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Get enabled status
     *
     * @return enabled / active status
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Get lastPasswordResetDate
     *
     * @return date last password change
     */
    @JsonIgnore
    public LocalDateTime getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }
}
