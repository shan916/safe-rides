package edu.csus.asi.saferides.security;

import edu.csus.asi.saferides.security.model.AuthorityName;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

/**
 * Provides common utility methods for dealing with JWTs
 */
@Component
public class JwtTokenUtil implements Serializable {

    // versioning for serialized object (is not really needed)
    private static final long serialVersionUID = -3301605591108950415L;

    // JWT field names
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_AUTHORITIES = "authorities";

    // the secret key used for cryptographically signing the token
    @Value("${jwt.secret}")
    private String secret;

    // the length of time (in seconds) that a token will expire after creation
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Get a username from a token
     *
     * @param token token
     * @return username
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject().toLowerCase();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * Get claimed authorities from a token
     *
     * @param token token
     * @return list of authorities
     */
    @SuppressWarnings("unchecked")
    public ArrayList<AuthorityName> getAuthoritiesFromToken(String token) {
        ArrayList<AuthorityName> authorityNames = new ArrayList<AuthorityName>();
        try {
            final Claims claims = getClaimsFromToken(token);
            // cast claims to ArrayList
            ArrayList authorityClaimsArray = (ArrayList) claims.get(CLAIM_KEY_AUTHORITIES);
            // get authority item and add it to the authorityNames arraylist
            for (Object el : authorityClaimsArray) {
                authorityNames.add(Enum.valueOf(AuthorityName.class, ((LinkedHashMap<String, String>) el).get("authority")));
            }
        } catch (Exception e) {
            authorityNames = null;
        }
        return authorityNames;
    }

    /**
     * Get the created date from a token
     *
     * @param token token
     * @return date the token was created
     */
    public LocalDateTime getCreatedDateFromToken(String token) {
        LocalDateTime created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = LocalDateTime.ofEpochSecond((Long) claims.get(CLAIM_KEY_CREATED), 0, ZoneOffset.of(ZoneId.systemDefault().getId()));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    /**
     * Get the expiration date from a token
     *
     * @param token token
     * @return date the token will expire
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * Helper to get Claims from a token
     *
     * @param token token
     * @return Claims
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * Helper to generate an expiration date
     *
     * @return expiration ate
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * Helper to check if a token is expired
     *
     * @param token token
     * @return expired
     */
    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Helper to check if the the token was created before a date (last password reset)
     *
     * @param created           date token created
     * @param lastPasswordReset date user password changed
     * @return whether created date occurred before password change date (invalid)
     */
    private boolean isCreatedBeforeLastPasswordReset(LocalDateTime created, LocalDateTime lastPasswordReset) {
        return (lastPasswordReset != null && created.compareTo(lastPasswordReset) < 0);
    }

    /**
     * Create a token from user details
     *
     * @param userDetails for the user a token is to be generated for
     * @return token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_AUTHORITIES, userDetails.getAuthorities());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * Build token (mainly for internal use)
     *
     * @param claims claims
     * @return token
     */
    String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * Check if token can be refreshed or expiration date lengthened
     *
     * @param token             token
     * @param lastPasswordReset date user password last changed
     * @return whether a token cna be refreshed
     */
    public Boolean canTokenBeRefreshed(String token, LocalDateTime lastPasswordReset) {
        final LocalDateTime created = getCreatedDateFromToken(token);
        return isTokenExpired(token) && !isCreatedBeforeLastPasswordReset(created, lastPasswordReset);
    }

    /**
     * Get a new token from an existing token
     *
     * @param token token
     * @return new token
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * Check if a token is valid
     *
     * @param token       token
     * @param userDetails user details of a user
     * @return whether the token is still valid
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        JwtUser user = (JwtUser) userDetails;
        final String username = getUsernameFromToken(token);

        return (username.equals(user.getUsername()) && !isTokenExpired(token) && !isCreatedBeforeLastPasswordReset(getCreatedDateFromToken(token), user.getLastPasswordResetDate()));
    }
}