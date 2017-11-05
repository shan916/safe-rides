package edu.csus.asi.saferides.controller;

import edu.csus.asi.saferides.model.Authority;
import edu.csus.asi.saferides.model.AuthorityName;
import edu.csus.asi.saferides.model.CasTuple;
import edu.csus.asi.saferides.model.User;
import edu.csus.asi.saferides.repository.AuthorityRepository;
import edu.csus.asi.saferides.repository.UserRepository;
import edu.csus.asi.saferides.security.JwtTokenUtil;
import edu.csus.asi.saferides.security.service.JwtAuthenticationResponse;
import edu.csus.asi.saferides.security.service.JwtUserDetailsServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * API controller for authenticating with CAS
 */
@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://codeteam6.io"})
@RequestMapping("/cas")
public class CasClientController {

    @Value("${cas.server-url-prefix}")
    private String casServerUrlPrefix;

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private JwtTokenUtil jwtTokenUtil;
    private JwtUserDetailsServiceImpl userDetailsService;

    /**
     * Dependency injection
     *
     * @param userRepository        User Repository
     * @param authorityRepository   Authority Repository
     * @param jwtTokenUtil          JWT Token Util
     * @param jwtUserDetailsService JWT User Details Service
     */
    @Autowired
    public CasClientController(UserRepository userRepository, AuthorityRepository authorityRepository, JwtTokenUtil jwtTokenUtil, JwtUserDetailsServiceImpl jwtUserDetailsService) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = jwtUserDetailsService;
    }

    /**
     * Check with CAS whether the service and ticket pair is correct. If so, authenticate the user.
     *
     * @param casTuple service and ticket pair
     * @return JWT on success or 400 on failure
     */
    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    @ApiOperation(value = "validate", nickname = "validate", notes = "Validate a ticket/service pair with the CAS server. Returns a JWT if valid.")
    public ResponseEntity<?> validate(@RequestBody CasTuple casTuple) {
        AttributePrincipal principal = null;
        Cas30ServiceTicketValidator tv = new Cas30ServiceTicketValidator(casServerUrlPrefix);

        try {
            Assertion a = tv.validate(casTuple.getTicket(), casTuple.getService());
            principal = a.getPrincipal();

            UserDetails user = null;
            try {
                user = userDetailsService.loadUserByUsername(principal.getName());
            } catch (UsernameNotFoundException ex) {
                User newUser = new User();
                newUser.setUsername(principal.getName());
                Authority authority = authorityRepository.findByName(AuthorityName.ROLE_RIDER);
                ArrayList authorityList = new ArrayList<Authority>();
                authorityList.add(authority);
                newUser.setAuthorities(authorityList);
                newUser.setActive(true);
                userRepository.save(newUser);
                user = userDetailsService.loadUserByUsername(newUser.getUsername());
            }

            // create a token for the new user
            final String token = jwtTokenUtil.generateToken(user);

            // Return the token
            return ResponseEntity.ok(new JwtAuthenticationResponse(token));
        } catch (TicketValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
