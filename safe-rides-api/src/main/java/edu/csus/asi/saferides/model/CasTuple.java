package edu.csus.asi.saferides.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * Model to transfer the service url and the CAS ticket
 */
public class CasTuple {

    @ApiModelProperty(value = "The service url provided to CAS")
    private String service;

    @ApiModelProperty(value = "The ticket provided by CAS")
    private String ticket;

    /**
     * Get the service url provided to CAS
     *
     * @return service url provided to CAS
     */
    public String getService() {
        return service;
    }

    /**
     * Set the service url provided to CAS
     *
     * @param service service url provided to CAS
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * Get the ticket provided by CAS
     *
     * @return ticket provided by CAS
     */
    public String getTicket() {
        return ticket;
    }

    /**
     * Set the ticket provided by CAS
     *
     * @param ticket ticket provided by CAS
     */
    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
