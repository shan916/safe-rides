package edu.csus.asi.saferides.model;

/**
 * Response Object to be used for returning any message in the body of the response
 */
public class ResponseMessage {

    /**
     * The response message
     */
    private String message;

    /**
     * Constructor for the ResponseMessage requiring message to be set
     *
     * @param message the message
     */
    public ResponseMessage(String message) {
        this.message = message;
    }

    /**
     * Get the message
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message
     *
     * @param message of the ResponseMessage
     */
    public void setMessage(String message) {
        this.message = message;
    }
}