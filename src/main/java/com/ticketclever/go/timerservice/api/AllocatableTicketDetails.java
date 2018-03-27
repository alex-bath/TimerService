package com.ticketclever.go.timerservice.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AllocatableTicketDetails {

    @JsonProperty("journeyId")
    private String journeyId;

    @JsonProperty("identityId")
    private String identityId;

    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("priceQuoted")
    private Double priceQuoted;

    @JsonProperty("activationTime")
    private LocalDateTime activationTime;

    @JsonProperty("activationLocation")
    private String activationLocation;

    @JsonProperty("crsOrigin")
    private String crsOrigin;

    @JsonProperty("crsDestination")
    private String crsDestination;

    @JsonProperty("departureTime")
    private LocalDateTime departureTime;

    @JsonCreator
    public AllocatableTicketDetails(@JsonProperty("journeyId") String journeyId,@JsonProperty("identityId") String identityId,
                                    @JsonProperty("accountId") String accountId,
                                    @JsonProperty("priceQuoted") double priceQuoted,
                                    @JsonProperty("activationTime") LocalDateTime activationTime,
                                    @JsonProperty("activationLocation") String activationLocation,
                                    @JsonProperty("crsOrigin") String crsOrigin,
                                    @JsonProperty("crsDestination") String crsDestination,
                                    @JsonProperty("departureTime") LocalDateTime departureTime) {
        this.journeyId = journeyId;
        this.identityId = identityId;
        this.accountId = accountId;
        this.priceQuoted = priceQuoted;
        this.activationTime = activationTime;
        this.activationLocation = activationLocation;
        this.crsOrigin = crsOrigin;
        this.crsDestination = crsDestination;
        this.departureTime = departureTime;
    }

    public String getIdentityId() {
        return identityId;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getPriceQuoted() {
        return priceQuoted;
    }

    public LocalDateTime getActivationTime() {
        return activationTime;
    }

    public String getActivationLocation() {
        return activationLocation;
    }

    public String getCrsOrigin() {
        return crsOrigin;
    }

    public String getCrsDestination() {
        return crsDestination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public String getJourneyId() {
        return journeyId;
    }

    @Override
    public String toString() {
        return "AllocatableTicketDetails{" +
                "journeyId='" + journeyId + '\'' +
                ", identityId='" + identityId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", priceQuoted=" + priceQuoted +
                ", activationTime=" + activationTime +
                ", activationLocation='" + activationLocation + '\'' +
                ", crsOrigin='" + crsOrigin + '\'' +
                ", crsDestination='" + crsDestination + '\'' +
                ", departureTime=" + departureTime +
                '}';
    }
}