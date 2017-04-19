package edu.csus.asi.saferides.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * Commence an authentication scheme.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    // versioning for serialized object (is not really needed)
    private static final long serialVersionUID = -8970718410437077606L;

    // load value of the allowed origins from the application config file
    @Value("${ac.alloworigin}")
    private String acAllowOrigin;

    /**
     * This is invoked when user tries to access a secured REST resource without supplying any credentials
     *
     * @param request       that resulted in an AuthenticationException
     * @param response      so that the user agent can begin authentication
     * @param authException that caused the invocation
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // set Access-Control-Allow-Origin header to appease chrome (and prevent -1)
        response.addHeader("Access-Control-Allow-Origin", acAllowOrigin);

        // return 401
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}