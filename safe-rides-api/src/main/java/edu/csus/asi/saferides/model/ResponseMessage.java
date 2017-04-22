package edu.csus.asi.saferides.model;

//shows a message to the Coordinator when a driver has not been assigned for a while.
public class ResponseMessage {

    private String message;

    public ResponseMessage(String s) {
        this.message = s;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}