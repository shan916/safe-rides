package edu.csus.asi.saferides.security;

import edu.csus.asi.saferides.security.model.Authority;
import edu.csus.asi.saferides.security.model.AuthorityName;
import edu.csus.asi.saferides.security.model.User;
import edu.csus.asi.saferides.security.repository.AuthorityRepository;
import edu.csus.asi.saferides.security.service.JwtUserDetailsServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The 'interceptor' for all requests that receive an authentication challenge (401)
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    // the output to logging should be removed later on
    private final Log logger = LogFactory.getLog(this.getClass());

    // dependency injection
    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // set name of the response key that stores the JWT
    @Value("${jwt.header}")
    private String tokenHeader;

    /**
     * Invoked once per request. Checks and validates the authentication token.
     * Also differentiates between checking for rider authentication and application user authentication
     * Rider authentication is less strict. It allows any valid onecard id; however, the only role that is allowed for
     * a rider is rider. The application user validates the user details (without the password).
     *
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authToken = request.getHeader(this.tokenHeader);

        // Set SecurityContext / validate token if authToken is not null
        if (authToken != null) {
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            ArrayList<AuthorityName> authoritiesFromToken = jwtTokenUtil.getAuthoritiesFromToken(authToken);

            logger.info("checking authentication for user " + username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails;
                // if rider
                if (authoritiesFromToken.size() == 1 && authoritiesFromToken.get(0).equals(AuthorityName.ROLE_RIDER)) {
                    try {
                        // find rider in rides table
                        userDetails = this.userDetailsService.loadRiderByOnecard(username);
                    } catch (Exception e) {
                        // else create a new 'user' with rider rights
                        User riderUser = new User(username, "anon_fname", "anon_lname");

                        ArrayList<Authority> authorities = new ArrayList<>();
                        authorities.add(authorityRepository.findByName(AuthorityName.ROLE_RIDER));
                        riderUser.setAuthorities(authorities);

                        userDetails = JwtUserFactory.create(riderUser);
                    }
                } else {
                    userDetails = this.userDetailsService.loadUserByUsername(username);
                }

                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    logger.info("authenticated user " + username + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        chain.doFilter(request, response);
    }
}