package edu.csus.asi.saferides.model;

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