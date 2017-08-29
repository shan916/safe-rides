package edu.csus.asi.saferides.security.controller;

import io.swagger.annotations.ApiOperation;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * API controller for authenticating with CAS
 */
@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://codeteam6.io"})
@RequestMapping("/cas")
public class CasClientController {

    @Value("${cas.server-url-prefix}")
    private String casServerUrlPrefix;

    /**
     * Check with CAS wether the service and ticket pair is correct. If so, authenticate the user.
     *
     * @param service the service the ticket was generated for
     * @param ticket  the ticket presented by CAS to the client
     * @return JWT on success or 500 on failure
     */
    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    @ApiOperation(value = "validate", nickname = "validate", notes = "Validate a ticket/service pair with the CAS server. Returns a JWT if valid.")
    public ResponseEntity<?> validate(@RequestParam String service, @RequestParam String ticket) {
        AttributePrincipal principal = null;
        Cas30ServiceTicketValidator tv = new Cas30ServiceTicketValidator(casServerUrlPrefix);

        try {
            Assertion a = tv.validate(ticket, service);
            principal = a.getPrincipal();

            return ResponseEntity.ok(principal.getName());
        } catch (TicketValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
