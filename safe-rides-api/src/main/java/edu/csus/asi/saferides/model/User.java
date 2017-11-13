package edu.csus.asi.saferides.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.csus.asi.saferides.serialization.LocalDateTimeDeserializer;
import edu.csus.asi.saferides.serialization.LocalDateTimeSerializer;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.List;

/**
 * Application user
 */
@Entity
public class User {
    /**
     * Primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Unique username
     */
    @Column(unique = true, nullable = false)
    @Size(min = 2, max = 30)
    private String username;

    /**
     * First name
     */
    @Size(min = 2, max = 30)
    private String firstName;

    /**
     * Last name
     */
    @Size(min = 2, max = 30)
    private String lastName;

    /**
     * Enabled / active flag
     */
    @Column(nullable = false)
    private boolean active;

    /**
     * Timestamp of last password reset
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime tokenValidFrom;

    @Enumerated(EnumType.STRING)
    private AuthorityName authorityLevel;

    /**
     * Constructor used by JPA
     */
    public User() {
    }

    /**
     * Constructor for creating a user object
     *
     * @param username  username
     * @param firstName first name
     * @param lastName  last name
     */
    public User(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        active = true;
    }

    /**
     * Get user's primary key
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set user's primary key
     *
     * @param id of user
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get user's username
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set user's username
     *
     * @param username of user
     */
    public void setUsername(String username) {
        this.username = username.replaceAll("\\s+", "").toLowerCase();
    }

    /**
     * Get user's first name
     *
     * @return first name of user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set user's first name
     *
     * @param firstName of user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get user's lastName
     *
     * @return last name of user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set user's last name
     *
     * @param lastName of user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get user's token valid from date
     *
     * @return user's tokenValidFrom date
     */
    public LocalDateTime getTokenValidFrom() {
        return tokenValidFrom;
    }

    /**
     * Set user's token valid from date
     *
     * @param tokenValidFrom of user
     */
    public void setTokenValidFrom(LocalDateTime tokenValidFrom) {
        this.tokenValidFrom = tokenValidFrom;
    }

    /**
     * Get user's authority level
     *
     * @return user's authorities
     */
    public AuthorityName getAuthorityLevel() {
        return authorityLevel;
    }

    /**
     * Set user's authority level
     *
     * @param authorityLevel user's authority level
     */
    public void setAuthorityLevel(AuthorityName authorityLevel) {
        this.authorityLevel = authorityLevel;
    }

    /**
     * Get user's active flag
     *
     * @return user's status
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Set user's active flag
     *
     * @param active of user
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
