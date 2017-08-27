package edu.csus.asi.saferides.model.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * Data Transfer Object for Configuration.
 * Contains a limited subset of the Configuration Entity Object.
 */
public class ConfigurationDto {
    /**
     * When to start accepting new ride requests
     */
    @ApiModelProperty(value = "The start time when ride requests will be accepted.", required = true)
    @NotNull(message = "The start time cannot be null.")
    private LocalTime startTime;

    /**
     * When to stop accepting new ride requests
     */
    @ApiModelProperty(value = "The time when ride requests will no longer be accepted.", required = true)
    @NotNull(message = "The end time cannot be null.")
    private LocalTime endTime;

    /**
     * Days of week program is active
     */
    @ApiModelProperty(value = "A list of the days of the week the program will start accepting rides on.", required = true)
    @NotNull(message = "The days of week the Safe Rides program will be active on cannot be null.")
    private List<DayOfWeek> daysOfWeek;

    /**
     * active flag for a manual override
     */
    @ApiModelProperty(value = "Signifies normal operation. If active is false, then Safe Rides is not accepting ride requests even if it is during an active time range.", required = true)
    @NotNull(message = "The active flag cannot be null.")
    private Boolean active;

    /**
     * Get the start time of when Safe Rides starts accepting requests
     *
     * @return start time
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Set the start time of when Safe Rides starts accepting requests
     *
     * @param startTime
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Get the end time when Safe Rides stops accepting requests
     *
     * @return end time
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Set the end time when Safe Rides stops accepting requests
     *
     * @param endTime
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Get the days of the days of the week that Safe Rides accepts requests
     *
     * @return
     */
    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    /**
     * Set the days of the days of the week that Safe Rides accepts requests
     *
     * @param daysOfWeek
     */
    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    /**
     * Get whether if Safe Rides accepts requests (override)
     *
     * @return
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Set whether if Safe Rides accepts requests (override)
     *
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
