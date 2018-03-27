package com.ticketclever.go.timerservice.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Activation {

    private String journeyId;
    private String identityId;
    private String photoCardNumber;
    private String accountId;
    private String mobileId;
    private int priceQuoted;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime activationTime;
    private String activationLocation;
    private String crsOrigin;
    private String crsDestination;
    private String nlcOrigin;
    private String nlcDestination;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime departureDateTime;
    private String description;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime cancellationTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime updateDate;
    private boolean allocationRequested;
    private boolean nonRevenue;
    private String journeyRef;
    private int numberOfChanges;
    private String fareTimeband;
    private String duration;

    private List<Legs> legs;

    public Activation() {
    }

    @JsonCreator()
    public Activation(@JsonProperty("journeyId") String journeyId,
                      @JsonProperty("identityId") String identityId,
                      @JsonProperty("photoCardNumber") String photoCardNumber,
                      @JsonProperty("accountId") String accountId,
                      @JsonProperty("mobileId") String mobileId,
                      @JsonProperty("priceQuoted") int priceQuoted,
                      @JsonProperty("activationTime") LocalDateTime activationTime,
                      @JsonProperty("activationLocation") String activationLocation,
                      @JsonProperty("crsOrigin") String crsOrigin,
                      @JsonProperty("crsDestination") String crsDestination,
                      @JsonProperty("nlcOrigin") String nlcOrigin,
                      @JsonProperty("nlcDestination") String nlcDestination,
                      @JsonProperty("departureDateTime") LocalDateTime departureDateTime,
                      @JsonProperty("description") String description,
                      @JsonProperty("status") String status,
                      @JsonProperty("cancellationTime") LocalDateTime cancellationTime,
                      @JsonProperty("createdDate") LocalDateTime createdDate,
                      @JsonProperty("updateDate") LocalDateTime updateDate,
                      @JsonProperty("allocationRequested") boolean allocationRequested,
                      @JsonProperty("nonRevenue") boolean nonRevenue,
                      @JsonProperty("journeyRef") String journeyRef,
                      @JsonProperty("numberOfChanges") int numberOfChanges,
                      @JsonProperty("fareTimeband") String fareTimeband,
                      @JsonProperty("duration") String duration,
                      @JsonProperty("legs") List<Legs> legs) {

        this.journeyId = journeyId;
        this.identityId = identityId;
        this.photoCardNumber = photoCardNumber;
        this.accountId = accountId;
        this.mobileId = mobileId;
        this.priceQuoted = priceQuoted;
        this.activationTime = activationTime;
        this.activationLocation = activationLocation;
        this.crsOrigin = crsOrigin;
        this.crsDestination = crsDestination;
        this.nlcOrigin = nlcOrigin;
        this.nlcDestination = nlcDestination;
        this.departureDateTime = departureDateTime;
        this.description = description;
        this.status = status;
        this.cancellationTime = cancellationTime;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
        this.allocationRequested = allocationRequested;
        this.nonRevenue = nonRevenue;
        this.journeyRef = journeyRef;
        this.numberOfChanges = numberOfChanges;
        this.fareTimeband = fareTimeband;
        this.duration = duration;
        this.legs = Optional.ofNullable(legs).filter(arr -> !arr.isEmpty()).orElseGet(Collections::emptyList);
    }

    /**
     * Set Legs, if null set empty collection.
     * @param legs list of legs
     */
    public void setLegs(List<Legs> legs) {
        this.legs = Optional.ofNullable(legs).filter(arr -> !arr.isEmpty()).orElseGet(Collections::emptyList);
    }

    public AllocatableTicketDetails toAllocatableTicketDetails() {
        return new AllocatableTicketDetails(
                this.journeyId,
                this.identityId,
                this.accountId,
                this.priceQuoted,
                this.activationTime,
                this.activationLocation,
                this.crsOrigin,
                this.crsDestination,
                this.departureDateTime);
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public String getPhotoCardNumber() {
        return photoCardNumber;
    }

    public void setPhotoCardNumber(String photoCardNumber) {
        this.photoCardNumber = photoCardNumber;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getMobileId() {
        return mobileId;
    }

    public void setMobileId(String mobileId) {
        this.mobileId = mobileId;
    }

    public int getPriceQuoted() {
        return priceQuoted;
    }

    public void setPriceQuoted(int priceQuoted) {
        this.priceQuoted = priceQuoted;
    }

    public LocalDateTime getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(LocalDateTime activationTime) {
        this.activationTime = activationTime;
    }

    public String getActivationLocation() {
        return activationLocation;
    }

    public void setActivationLocation(String activationLocation) {
        this.activationLocation = activationLocation;
    }

    public String getCrsOrigin() {
        return crsOrigin;
    }

    public void setCrsOrigin(String crsOrigin) {
        this.crsOrigin = crsOrigin;
    }

    public String getCrsDestination() {
        return crsDestination;
    }

    public void setCrsDestination(String crsDestination) {
        this.crsDestination = crsDestination;
    }

    public String getNlcOrigin() {
        return nlcOrigin;
    }

    public void setNlcOrigin(String nlcOrigin) {
        this.nlcOrigin = nlcOrigin;
    }

    public String getNlcDestination() {
        return nlcDestination;
    }

    public void setNlcDestination(String nlcDestination) {
        this.nlcDestination = nlcDestination;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCancellationTime() {
        return cancellationTime;
    }

    public void setCancellationTime(LocalDateTime cancellationTime) {
        this.cancellationTime = cancellationTime;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public boolean isAllocationRequested() {
        return allocationRequested;
    }

    public void setAllocationRequested(boolean allocationRequested) {
        this.allocationRequested = allocationRequested;
    }

    public boolean isNonRevenue() {
        return nonRevenue;
    }

    public void setNonRevenue(boolean nonRevenue) {
        this.nonRevenue = nonRevenue;
    }

    public String getJourneyRef() {
        return journeyRef;
    }

    public void setJourneyRef(String journeyRef) {
        this.journeyRef = journeyRef;
    }

    public int getNumberOfChanges() {
        return numberOfChanges;
    }

    public void setNumberOfChanges(int numberOfChanges) {
        this.numberOfChanges = numberOfChanges;
    }

    public String getFareTimeband() {
        return fareTimeband;
    }

    public void setFareTimeband(String fareTimeband) {
        this.fareTimeband = fareTimeband;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Activation{" +
                "journeyId='" + journeyId + '\'' +
                ", identityId='" + identityId + '\'' +
                ", photoCardNumber='" + photoCardNumber + '\'' +
                ", accountId='" + accountId + '\'' +
                ", mobileId='" + mobileId + '\'' +
                ", priceQuoted=" + priceQuoted +
                ", activationTime=" + activationTime +
                ", activationLocation='" + activationLocation + '\'' +
                ", crsOrigin='" + crsOrigin + '\'' +
                ", crsDestination='" + crsDestination + '\'' +
                ", nlcOrigin='" + nlcOrigin + '\'' +
                ", nlcDestination='" + nlcDestination + '\'' +
                ", departureDateTime=" + departureDateTime +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", cancellationTime=" + cancellationTime +
                ", createdDate=" + createdDate +
                ", updateDate=" + updateDate +
                ", allocationRequested=" + allocationRequested +
                ", nonRevenue=" + nonRevenue +
                ", journeyRef='" + journeyRef + '\'' +
                ", numberOfChanges=" + numberOfChanges +
                ", fareTimeband='" + fareTimeband + '\'' +
                ", duration='" + duration + '\'' +
                ", legs=" + legs +
                '}';
    }
}