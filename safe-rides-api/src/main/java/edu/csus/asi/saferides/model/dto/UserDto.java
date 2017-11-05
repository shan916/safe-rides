package edu.csus.asi.saferides.model.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Data Transfer Object for the User object.
 */
public class UserDto {

    private Long id;

    @NotNull
    @Size(min = 2, max = 30)
    @ApiModelProperty(value = "The user's username", readOnly = true)
    private String username;

    @NotNull
    @Size(min = 2, max = 30)
    @ApiModelProperty(value = "The user's first name")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 30)
    @ApiModelProperty(value = "The user's last name")
    private String lastName;

    @NotNull
    @ApiModelProperty(value = "Whether the user is active or not", readOnly = true)
    private boolean active;

    /**
     * Gets the id
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id
     *
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the username
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the first name
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets whether the user is active or not
     *
     * @return true/false if user is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets whether the user is active or not
     *
     * @param active true/false if user is active or not
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
