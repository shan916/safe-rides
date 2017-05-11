package edu.csus.asi.saferides.security.service;

import java.io.Serializable;

/**
 * Returned when a successful authentication event occurs.
 */
public class JwtAuthenticationResponse implements Serializable {

    // versioning for serialized object (is not really needed but will be useful for other clients to detect change)
    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;

    /**
     * Instantiate a new token with the token string
     *
     * @param token the token
     */
    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    /**
     * Get token
     *
     * @return token
     */
    public String getToken() {
        return this.token;
    }
}
