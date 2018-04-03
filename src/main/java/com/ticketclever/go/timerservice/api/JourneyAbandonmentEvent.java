package com.ticketclever.go.timerservice.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JourneyAbandonmentEvent {

    private String journeyId;
    private Instant dateAbandoned;

    /**
     * JourneyAbandonmentEvent constructor.
     * @param journeyId of the activation.
     * @param dateAbandoned of the activation.
     */
    @JsonCreator
    public JourneyAbandonmentEvent(@JsonProperty("journeyId") String journeyId, @JsonProperty("dateAbandoned") Instant dateAbandoned) {
        this.journeyId = journeyId;
        this.dateAbandoned = dateAbandoned;
    }

    /**
     * Get the journey id.
     * @return String of the journey Id.
     */
    public String getJourneyId() {
        return journeyId;
    }

    /**
     * Get the date the journey was abandoned.
     * @return String of the abandoned date.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public Instant getDateAbandoned() {
        return dateAbandoned;
    }

    @Override
    public String toString() {
        return "JourneyAbandonmentEvent{" +
                "journeyId='" + journeyId + '\'' +
                ", dateAbandoned='" + dateAbandoned + '\'' +
                '}';
    }

}