package edu.csus.asi.saferides.model;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * Configuration settings for the application (app start time, end time, etc)
 */
@Entity
public class Configuration {
    /**
     * Primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * When to start accepting new ride requests
     */
    @Column(nullable = false)
    private LocalTime startTime;

    /**
     * When to stop accepting new ride requests
     */
    @Column(nullable = false)
    private LocalTime endTime;

    /**
     * Days of week program is active
     */
    @ElementCollection
    @Column(nullable = false)
    private List<DayOfWeek> daysOfWeek;

    /**
     * active flag for a manual override
     */
    @Column(nullable = false)
    private boolean active;

    /**
     * Constructor used by JPA
     */
    protected Configuration() {
    }

    /**
     * Constructor for new configuration object
     *
     * @param startTime of the application accepting new ride requests
     * @param endTime   of the application accepting new ride requests
     */
    public Configuration(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        active = true;
    }

    /**
     * Get primary key
     *
     * @return configuration's primary key
     */
    public int getId() {
        return id;
    }

    /**
     * Set primary key
     *
     * @param id of configuration row
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get start time
     *
     * @return time of when to accept new ride requests
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Set start time
     *
     * @param startTime of accepting new ride requests
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Get end time
     *
     * @return time of when to stop accepting new ride requests
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Set end time
     *
     * @param endTime of accepting new ride requests
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Get (start) days of week application is accepting new ride requests
     *
     * @return days of week
     */
    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    /**
     * Set (start) days of week application is accepting new ride requests
     *
     * @param daysOfWeek of accepting new ride requests
     */
    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }


    /**
     * Check if active
     *
     * @return application ride request acceptance status
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Set active flag
     *
     * @param active application ride request acceptance status
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
