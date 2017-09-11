package edu.csus.asi.saferides.security.controller;

import edu.csus.asi.saferides.security.JwtTokenUtil;
import edu.csus.asi.saferides.security.JwtUserFactory;
import edu.csus.asi.saferides.security.model.Authority;
import edu.csus.asi.saferides.security.model.AuthorityName;
import edu.csus.asi.saferides.security.model.CasTuple;
import edu.csus.asi.saferides.security.model.User;
import edu.csus.asi.saferides.security.repository.AuthorityRepository;
import edu.csus.asi.saferides.security.repository.UserRepository;
import edu.csus.asi.saferides.security.service.JwtAuthenticationResponse;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * API controller for authenticating with CAS
 */
@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://codeteam6.io", "http://codeteam6.io"})
@RequestMapping("/cas")
public class CasClientController {

    @Value("${cas.server-url-prefix}")
    private String casServerUrlPrefix;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

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

            User user = userRepository.findByUsernameIgnoreCase(principal.getName());

            // if user is not in the database create a new user and add it to the database
            if (null == user) {
                user = new User();
                user.setUsername(principal.getName());
                Authority authority = authorityRepository.findByName(AuthorityName.ROLE_RIDER);
                ArrayList authorityList = new ArrayList<Authority>();
                authorityList.add(authority);
                user.setAuthorities(authorityList);
                user.setActive(true);
                userRepository.save(user);
            }

            // create a token for the new user
            UserDetails userDetails = JwtUserFactory.create(user);
            final String token = jwtTokenUtil.generateToken(userDetails);

            // Return the token
            return ResponseEntity.ok(new JwtAuthenticationResponse(token));
        } catch (TicketValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
