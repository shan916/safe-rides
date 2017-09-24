package edu.csus.asi.saferides.controller;

import edu.csus.asi.saferides.model.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class SafeRidesExceptionHandler extends ResponseEntityExceptionHandler {

    // Should be only thrown when updating the ride request in the ride request controller
    @ExceptionHandler({ObjectOptimisticLockingFailureException.class})
    public ResponseEntity<?> databaseConcurrencyIssue() {
        return ResponseEntity.badRequest().body(new ResponseMessage("Could not update the ride request as either the driver or the ride request was already updated. Please refresh and try again."));
    }
}
