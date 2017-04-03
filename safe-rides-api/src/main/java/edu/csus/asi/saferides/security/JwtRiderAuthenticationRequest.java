package edu.csus.asi.saferides.security;

import java.io.Serializable;

public class JwtRiderAuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

    private String oneCardId;
    
    public JwtRiderAuthenticationRequest() {
        super();
    }

    public JwtRiderAuthenticationRequest(String oneCardId) {
        this.setOneCardId(oneCardId);
    }

	public String getOneCardId() {
		return oneCardId;
	}

	public void setOneCardId(String oneCardId) {
		this.oneCardId = oneCardId;
	}
}
