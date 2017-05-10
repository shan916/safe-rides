package edu.csus.asi.saferides.security.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.csus.asi.saferides.serialization.LocalDateTimeDeserializer;
import edu.csus.asi.saferides.serialization.LocalDateTimeSerializer;
import edu.csus.asi.saferides.security.ArgonPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Application user than can be authenticated with a password
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
    @Column(nullable = false)
    @Size(min = 2, max = 30)
    private String firstname;

    /**
     * Last name
     */
    @Column(nullable = false)
    @Size(min = 2, max = 30)
    private String lastname;

    /**
     * Password
     */
    @Column(nullable = false)
    @Size(min = 161, max = 161)
    private String password;

    /**
     * Email
     */
    @Column(nullable = false)
    @Size(min = 2, max = 50)
    private String email;

    /**
     * Enabled / active flag
     */
    @Column(nullable = false)
    private boolean enabled;

    /**
     * Timestamp of last password reset
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lastPasswordResetDate;

    /**
     * M-M relationship join table for authorities / roles
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "User_Authority",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
    private List<Authority> authorities;

    /**
     * Constructor used by JPA
     */
    protected User() {
    }

    /**
     * Constructor for creating a user object
     *
     * @param username  username
     * @param firstName first name
     * @param lastName  last name
     * @param password  password (in plaintext)
     * @param email     email
     */
    public User(String username, String firstName, String lastName, String password, String email) {
        this.username = username;
        this.firstname = firstName;
        this.lastname = lastName;
        setPassword(password);
        this.email = email;
        enabled = true;
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
        this.firstname = firstName;
        this.lastname = lastName;
        enabled = true;
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
        this.username = username;
    }

    /**
     * Get user's first name
     *
     * @return first name of user
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Set user's first name
     *
     * @param firstname of user
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Get user's lastName
     *
     * @return last name of user
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Set user's last name
     *
     * @param lastname of user
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Get user's encoded password
     *
     * @return password (encoded)
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get user's email
     *
     * @return of user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set user's email
     *
     * @param email of user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get user's enabled / active flag
     *
     * @return user's status
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * Set user's enabled / active flag
     *
     * @param enabled of user
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Get user's last password reset date
     *
     * @return user's lastPasswordReset date
     */
    public LocalDateTime getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    /**
     * Set user's last password reset date
     *
     * @param lastPasswordResetDate of user
     */
    public void setLastPasswordResetDate(LocalDateTime lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    /**
     * Get user's authorities / roles
     *
     * @return user's authorities
     */
    public List<Authority> getAuthorities() {
        return authorities;
    }

    /**
     * Set user's authorities / roles
     *
     * @param authorities of user
     */
    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    /**
     * Set user's password. Encodes the password input
     *
     * @param password of user in plaintext
     */
    public void setPassword(String password) {
        ArgonPasswordEncoder passwordEncoder = new ArgonPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }
}
