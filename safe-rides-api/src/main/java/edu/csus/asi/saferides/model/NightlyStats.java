package edu.csus.asi.saferides.model;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Nightly Stat data model for reports
 */
@Entity
public class NightlyStats {

    /**
     * Primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Date data aggregated
     */
    @Column(unique = true)
    private LocalDate dateAggregated;

    /**
     * Total ride requests fulfilled
     */
    @Column
    private long requestsFulfilled;

    /**
     * Total ride requests canceled
     */
    @Column
    private long requestsCanceled;

    /**
     * Number of riders (total)
     */
    @Column
    private long riders;

    /**
     * Average trip distance
     */
    @Column
    private double averageDistance;

    /**
     * Median trip distance
     */
    @Column
    private double medianDistance;

    /**
     * Shortest distance for a ride request to be fulfilled
     */
    @Column
    private double shortestDistance;

    /**
     * Longest distance for a ride request to be fulfilled
     */
    @Column
    private double longestDistance;

    /**
     * Average time for a ride request to be assigned
     */
    @Column
    private double averageTimeToAssignment;

    /**
     * Median time for a ride request to be assigned
     */
    @Column
    private double medianTimeToAssignment;

    /**
     * Fastest time for a ride request to be assigned
     */
    @Column
    private double fastestTimeToAssignment;

    /**
     * Slowest time for a ride to be assigned
     */
    @Column
    private double slowestTimeToAssignment;

    /**
     * Average time for a ride to be completed
     */
    @Column
    private double averageFulfillmentTime;

    /**
     * Median time for a ride to be completed
     */
    @Column
    private double medianFulfillmentTime;

    /**
     * Fastest ride request completion time
     */
    @Column
    private double fastestFulfillmentTime;

    /**
     * Slowest ride request completion time
     */
    @Column
    private double slowestFulfillmentTime;

    /**
     * Constructor used by JPA
     */
    public NightlyStats() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateAggregated() {
        return dateAggregated;
    }

    public void setDateAggregated(LocalDate dateAggregated) {
        this.dateAggregated = dateAggregated;
    }

    public long getRequestsFulfilled() {
        return requestsFulfilled;
    }

    public void setRequestsFulfilled(long requestsFulfilled) {
        this.requestsFulfilled = requestsFulfilled;
    }

    public long getRequestsCanceled() {
        return requestsCanceled;
    }

    public void setRequestsCanceled(long requestsCanceled) {
        this.requestsCanceled = requestsCanceled;
    }

    public long getRiders() {
        return riders;
    }

    public void setRiders(long riders) {
        this.riders = riders;
    }

    public double getAverageDistance() {
        return averageDistance;
    }

    public void setAverageDistance(double averageDistance) {
        this.averageDistance = averageDistance;
    }

    public double getMedianDistance() {
        return medianDistance;
    }

    public void setMedianDistance(double medianDistance) {
        this.medianDistance = medianDistance;
    }

    public double getShortestDistance() {
        return shortestDistance;
    }

    public void setShortestDistance(double shortestDistance) {
        this.shortestDistance = shortestDistance;
    }

    public double getLongestDistance() {
        return longestDistance;
    }

    public void setLongestDistance(double longestDistance) {
        this.longestDistance = longestDistance;
    }

    public double getAverageTimeToAssignment() {
        return averageTimeToAssignment;
    }

    public void setAverageTimeToAssignment(double averageTimeToAssignment) {
        this.averageTimeToAssignment = averageTimeToAssignment;
    }

    public double getMedianTimeToAssignment() {
        return medianTimeToAssignment;
    }

    public void setMedianTimeToAssignment(double medianTimeToAssignment) {
        this.medianTimeToAssignment = medianTimeToAssignment;
    }

    public double getFastestTimeToAssignment() {
        return fastestTimeToAssignment;
    }

    public void setFastestTimeToAssignment(double fastestTimeToAssignment) {
        this.fastestTimeToAssignment = fastestTimeToAssignment;
    }

    public double getSlowestTimeToAssignment() {
        return slowestTimeToAssignment;
    }

    public void setSlowestTimeToAssignment(double slowestTimeToAssignment) {
        this.slowestTimeToAssignment = slowestTimeToAssignment;
    }

    public double getAverageFulfillmentTime() {
        return averageFulfillmentTime;
    }

    public void setAverageFulfillmentTime(double averageFulfillmentTime) {
        this.averageFulfillmentTime = averageFulfillmentTime;
    }

    public double getMedianFulfillmentTime() {
        return medianFulfillmentTime;
    }

    public void setMedianFulfillmentTime(double medianFulfillmentTime) {
        this.medianFulfillmentTime = medianFulfillmentTime;
    }

    public double getFastestFulfillmentTime() {
        return fastestFulfillmentTime;
    }

    public void setFastestFulfillmentTime(double fastestFulfillmentTime) {
        this.fastestFulfillmentTime = fastestFulfillmentTime;
    }

    public double getSlowestFulfillmentTime() {
        return slowestFulfillmentTime;
    }

    public void setSlowestFulfillmentTime(double slowestFulfillmentTime) {
        this.slowestFulfillmentTime = slowestFulfillmentTime;
    }
}