package com.ticketclever.go.timerservice.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {

    private String crs;
    private String nlc;
    private String tiploc;

    @JsonCreator
    public Location(@JsonProperty("crs") final String crs, @JsonProperty("nlc") final String nlc, @JsonProperty("tiploc") final String tiploc) {
        this.crs = crs;
        this.nlc = nlc;
        this.tiploc = tiploc;
    }

    @Override
    public String toString() {
        return "Location{" +
                "crs='" + crs + '\'' +
                ", nlc='" + nlc + '\'' +
                ", tiploc='" + tiploc + '\'' +
                '}';
    }

    public String getCrs() {
        return crs;
    }

    public String getNlc() {
        return nlc;
    }

    public String getTiploc() {
        return tiploc;
    }
}
