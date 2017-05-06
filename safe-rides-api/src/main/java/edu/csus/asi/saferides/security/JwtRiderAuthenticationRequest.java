package edu.csus.asi.saferides.security;

import java.io.Serializable;

/**
 * This class is used for authenticating riders and allowing only one request per session
 */
public class JwtRiderAuthenticationRequest implements Serializable {

    // versioning for serialized object (is not really needed)
    private static final long serialVersionUID = -8445943548965154778L;

    private String oneCardId;

    /**
     * Used by JPA
     */
    public JwtRiderAuthenticationRequest() {
        super();
    }

    /**
     * Used by JPA to instantiate a new JWT Authentication Request from the posted object
     *
     * @param oneCardId of the rider
     */
    public JwtRiderAuthenticationRequest(String oneCardId) {
        this.setOneCardId(oneCardId);
    }

    /**
     * Get onecard id
     *
     * @return oneCardId
     */
    public String getOneCardId() {
        return oneCardId;
    }

    /**
     * Set onecard id
     *
     * @param oneCardId rider's oneCardId
     */
    public void setOneCardId(String oneCardId) {
        this.oneCardId = oneCardId;
    }
}

