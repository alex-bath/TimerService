package com.ticketclever.go.timerservice.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.LocalTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Legs {

    private String id;
    private String journeyId;
    private String serviceId;
    private String trainUId;
    private Location origin;
    private Location destination;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private Instant date;

    public Legs(String id,
                String journeyId,
                String serviceId,
                String trainUId,
                Location origin,
                Location destination,
                LocalTime departureTime,
                LocalTime arrivalTime,
                Instant date) {
        this.id = id;
        this.journeyId = journeyId;
        this.serviceId = serviceId;
        this.trainUId = trainUId;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.date = date;
    }

    @JsonCreator
    public Legs(@JsonProperty("id") String id,
                @JsonProperty("journeyId") String journeyId,
                @JsonProperty("serviceId") String serviceId,
                @JsonProperty("trainUId") String trainUId,
                @JsonProperty("origin") Location origin,
                @JsonProperty("destination") Location destination,
                @JsonProperty("departureTime") long departureTime,
                @JsonProperty("arrivalTime") long arrivalTime,
                @JsonProperty("date") Instant date) {
        this.id = id;
        this.journeyId = journeyId;
        this.serviceId = serviceId;
        this.trainUId = trainUId;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = LocalTime.ofSecondOfDay(departureTime);
        this.arrivalTime = LocalTime.ofSecondOfDay(arrivalTime);
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public String getTrainUId() {
        return trainUId;
    }

    public void setTrainUId(String trainUId) {
        this.trainUId = trainUId;
    }

    public Location getOrigin() {
        return origin;
    }

    public void setOrigin(Location origin) {
        this.origin = origin;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        return "Legs{" +
                "id=" + id +
                ", journeyId='" + journeyId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", trainUId='" + trainUId + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", date=" + date +
                '}';
    }
}
