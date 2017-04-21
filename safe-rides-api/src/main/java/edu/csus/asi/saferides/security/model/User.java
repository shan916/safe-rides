package edu.csus.asi.saferides.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.csus.asi.saferides.security.ArgonPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/*
 * @author Ryan Long
 * 
 * Model object for User Entity
 * */


@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    @Size(min = 2, max = 30)
    private String username;

    @Column(nullable = false)
    @Size(min = 2, max = 30)
    private String firstname;

    @Column(nullable = false)
    @Size(min = 2, max = 30)
    private String lastname;

    @Column(nullable = false)
    @Size(min = 161, max = 161)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    @Size(min = 2, max = 50)
    private String email;

    @Column(nullable = false)
    private boolean enabled;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordResetDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "User_Authority",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
    private List<Authority> authorities;

    // protected Constructor required for JPA
    protected User() {
    }

    public User(String username, String firstName, String lastName, String password, String email) {
        this.username = username;
        this.firstname = firstName;
        this.lastname = lastName;
        setPassword(password);
        this.email = email;
        enabled = true;
    }

    public User(String username, String firstName, String lastName) {
        this.username = username;
        this.firstname = firstName;
        this.lastname = lastName;
        enabled = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public void setPassword(String password) {
        ArgonPasswordEncoder passwordEncoder = new ArgonPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }
}
