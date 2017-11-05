package edu.csus.asi.saferides.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Nightly Stat data model for reports
 */
@Entity
public class NightlyStats {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "Nightly Stats entry id")
    private Long id;

    @ApiModelProperty(value = "Date data aggregated")
    @Column(unique = true)
    private LocalDate dateAggregated;

    @ApiModelProperty(value = "Total ride requests fulfilled")
    @Column
    private long requestsFulfilled;

    @ApiModelProperty(value = "Total ride requests canceled")
    @Column
    private long requestsCanceled;

    @ApiModelProperty(value = "Number of riders (total)")
    @Column
    private long riders;

    @ApiModelProperty(value = "Average trip distance")
    @Column
    private double averageDistance;

    @ApiModelProperty(value = "Median trip distance")
    @Column
    private double medianDistance;

    @ApiModelProperty(value = "Shortest distance for a ride request to be fulfilled")
    @Column
    private double shortestDistance;

    @ApiModelProperty(value = "Longest distance for a ride request to be fulfilled")
    @Column
    private double longestDistance;

    @ApiModelProperty(value = "Average time for a ride request to be assigned")
    @Column
    private double averageTimeToAssignment;

    @ApiModelProperty(value = " Median time for a ride request to be assigned")
    @Column
    private double medianTimeToAssignment;

    @ApiModelProperty(value = "Fastest time for a ride request to be assigned")
    @Column
    private double fastestTimeToAssignment;

    @ApiModelProperty(value = "Slowest time for a ride to be assigned")
    @Column
    private double slowestTimeToAssignment;

    @ApiModelProperty(value = "Average time for a ride to be completed")
    @Column
    private double averageFulfillmentTime;

    @ApiModelProperty(value = "Median time for a ride to be completed")
    @Column
    private double medianFulfillmentTime;

    @ApiModelProperty(value = "Fastest ride request completion time")
    @Column
    private double fastestFulfillmentTime;

    @ApiModelProperty(value = "Slowest ride request completion time")
    @Column
    private double slowestFulfillmentTime;

    /**
     * Constructor used by JPA
     */
    public NightlyStats() {
    }

    /**
     * Get the id
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the date the report was aggregated
     *
     * @return date the report was aggregated
     */
    public LocalDate getDateAggregated() {
        return dateAggregated;
    }

    /**
     * Set the date the report was aggregated
     *
     * @param dateAggregated date the report was aggregated
     */
    public void setDateAggregated(LocalDate dateAggregated) {
        this.dateAggregated = dateAggregated;
    }

    /**
     * Get the number of requests fulfilled
     *
     * @return number of requests fulfilled
     */
    public long getRequestsFulfilled() {
        return requestsFulfilled;
    }

    /**
     * Set the number of requests fulfilled
     *
     * @param requestsFulfilled number of requests fulfilled
     */
    public void setRequestsFulfilled(long requestsFulfilled) {
        this.requestsFulfilled = requestsFulfilled;
    }

    /**
     * Get the number of requests canceled
     *
     * @return number of requests canceled
     */
    public long getRequestsCanceled() {
        return requestsCanceled;
    }

    /**
     * Set the number of requests canceled
     *
     * @param requestsCanceled number of requests canceled
     */
    public void setRequestsCanceled(long requestsCanceled) {
        this.requestsCanceled = requestsCanceled;
    }

    /**
     * Get the number of riders (inc passengers)
     *
     * @return number of riders
     */
    public long getRiders() {
        return riders;
    }

    /**
     * Set the number of riders
     *
     * @param riders number of riders
     */
    public void setRiders(long riders) {
        this.riders = riders;
    }

    /**
     * Get the average trip distance
     *
     * @return average trip distance
     */
    public double getAverageDistance() {
        return averageDistance;
    }

    /**
     * Set the average trip distance
     *
     * @param averageDistance average trip distance
     */
    public void setAverageDistance(double averageDistance) {
        this.averageDistance = averageDistance;
    }


    /**
     * Get the median trip distance
     *
     * @return median trip distance
     */
    public double getMedianDistance() {
        return medianDistance;
    }

    /**
     * Set the median trip distance
     *
     * @param medianDistance median trip distance
     */
    public void setMedianDistance(double medianDistance) {
        this.medianDistance = medianDistance;
    }

    /**
     * Get the shortest trip distance
     *
     * @return shortest trip distance
     */
    public double getShortestDistance() {
        return shortestDistance;
    }

    /**
     * Set the shortest trip distance
     *
     * @param shortestDistance shortest trip distance
     */
    public void setShortestDistance(double shortestDistance) {
        this.shortestDistance = shortestDistance;
    }

    /**
     * Get the longest trip distance
     *
     * @return longest trip distance
     */
    public double getLongestDistance() {
        return longestDistance;
    }

    /**
     * Set the longest trip distance
     *
     * @param longestDistance longest trip distance
     */
    public void setLongestDistance(double longestDistance) {
        this.longestDistance = longestDistance;
    }

    /**
     * Get the average time to assignment
     *
     * @return average time to assignment
     */
    public double getAverageTimeToAssignment() {
        return averageTimeToAssignment;
    }

    /**
     * Set the average time to assignment
     *
     * @param averageTimeToAssignment average time to assignment
     */
    public void setAverageTimeToAssignment(double averageTimeToAssignment) {
        this.averageTimeToAssignment = averageTimeToAssignment;
    }

    /**
     * Get the median time to assignment
     *
     * @return median time to assignment
     */
    public double getMedianTimeToAssignment() {
        return medianTimeToAssignment;
    }

    /**
     * Set the median time to assignment
     *
     * @param medianTimeToAssignment median time to assignment
     */
    public void setMedianTimeToAssignment(double medianTimeToAssignment) {
        this.medianTimeToAssignment = medianTimeToAssignment;
    }

    /**
     * Get the fastest time to assignment
     *
     * @return fastest time to assignment
     */
    public double getFastestTimeToAssignment() {
        return fastestTimeToAssignment;
    }

    /**
     * Set the fastest time to assignment
     *
     * @param fastestTimeToAssignment fastest time to assignment
     */
    public void setFastestTimeToAssignment(double fastestTimeToAssignment) {
        this.fastestTimeToAssignment = fastestTimeToAssignment;
    }

    /**
     * Get the slowest time to assignment
     *
     * @return slowest time to assignment
     */
    public double getSlowestTimeToAssignment() {
        return slowestTimeToAssignment;
    }

    /**
     * Set the slowest time to assignment
     *
     * @param slowestTimeToAssignment slowest time to assignment
     */
    public void setSlowestTimeToAssignment(double slowestTimeToAssignment) {
        this.slowestTimeToAssignment = slowestTimeToAssignment;
    }

    /**
     * Get the average time to fulfillment
     *
     * @return average time to fulfillment
     */
    public double getAverageFulfillmentTime() {
        return averageFulfillmentTime;
    }

    /**
     * Set the average time to fulfillment
     *
     * @param averageFulfillmentTime average time to fulfillment
     */
    public void setAverageFulfillmentTime(double averageFulfillmentTime) {
        this.averageFulfillmentTime = averageFulfillmentTime;
    }

    /**
     * Get the median time to fulfillment
     *
     * @return median time to fulfillment
     */
    public double getMedianFulfillmentTime() {
        return medianFulfillmentTime;
    }

    /**
     * Set the median time to fulfillment
     *
     * @param medianFulfillmentTime median time to fulfillment
     */
    public void setMedianFulfillmentTime(double medianFulfillmentTime) {
        this.medianFulfillmentTime = medianFulfillmentTime;
    }

    /**
     * Get the fastest time to fulfillment
     *
     * @return fastest time to fulfillment
     */
    public double getFastestFulfillmentTime() {
        return fastestFulfillmentTime;
    }

    /**
     * Set the fastest time to fulfillment
     *
     * @param fastestFulfillmentTime fastest time to fulfillment
     */
    public void setFastestFulfillmentTime(double fastestFulfillmentTime) {
        this.fastestFulfillmentTime = fastestFulfillmentTime;
    }

    /**
     * Get the slowest time to fulfillment
     *
     * @return slowest time to fulfillment
     */
    public double getSlowestFulfillmentTime() {
        return slowestFulfillmentTime;
    }

    /**
     * Set the slowest time to fulfillment
     *
     * @param slowestFulfillmentTime slowest time to fulfillment
     */
    public void setSlowestFulfillmentTime(double slowestFulfillmentTime) {
        this.slowestFulfillmentTime = slowestFulfillmentTime;
    }
}