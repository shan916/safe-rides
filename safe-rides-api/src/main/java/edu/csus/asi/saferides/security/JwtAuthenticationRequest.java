package edu.csus.asi.saferides.security;

import java.io.Serializable;

/**
 * Authentication request that is POSTed to the api.
 */
public class JwtAuthenticationRequest implements Serializable {

    // versioning for serialized object (is not really needed)
    private static final long serialVersionUID = -8445943548965154778L;

    private String username;
    private String password;

    /**
     * Used by JPA
     */
    public JwtAuthenticationRequest() {
        super();
    }

    /**
     * Used by JPA to instantiate a new JWT Authentication Request from the posted object
     *
     * @param username of application user
     * @param password of application user
     */
    public JwtAuthenticationRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    /**
     * Get username
     *
     * @return username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Set username
     *
     * @param username username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
