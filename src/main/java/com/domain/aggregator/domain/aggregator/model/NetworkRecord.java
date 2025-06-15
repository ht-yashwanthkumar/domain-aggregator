package com.domain.aggregator.domain.aggregator.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

@Valid
public class NetworkRecord {

    /**
     * The time stamp.
     */
    @NotNull(message = "Timestamp cannot be null")
    private LocalDateTime timestamp;

    /**
     * The source ip address.
     */
    @NotBlank(message = "Source IP cannot be blank")
    @Pattern(regexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)(\\.|$)){4}$", message = "Invalid Source IP format")
    private String sourceIp;

    /**
     * The source port.
     */
    @NotBlank(message = "Source IP port cannot be blank")
    private String sourcePort;

    /**
     * The destination ip address.
     */
    @NotBlank(message = "Destination IP cannot be blank")
    @Pattern(regexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)(\\.|$)){4}$", message = "Invalid Destination IP format")
    private String destinationIp;

    /**
     * The destination port.
     */
    @NotBlank(message = "Destination IP port cannot be blank")
    private String destinationPort;

    /**
     * The domain.
     */
    @NotBlank(message = "Domain cannot be blank")
    private String domain;

    public NetworkRecord(LocalDateTime timestamp, String sourceIp, String sourcePort, String destinationIp, String destinationPort, String domain) {
        this.timestamp = timestamp;
        this.sourceIp = sourceIp;
        this.sourcePort = sourcePort;
        this.destinationIp = destinationIp;
        this.destinationPort = destinationPort;
        this.domain = domain;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getDestinationIp() {
        return destinationIp;
    }

    public void setDestinationIp(String destinationIp) {
        this.destinationIp = destinationIp;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(String sourcePort) {
        this.sourcePort = sourcePort;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    @Override
    public String toString() {
        return "NetworkRecord{" +
                "timestamp=" + timestamp +
                ", sourceIp='" + sourceIp + '\'' +
                ", sourcePort='" + sourcePort + '\'' +
                ", destinationIp='" + destinationIp + '\'' +
                ", destinationPort='" + destinationPort + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }
}
