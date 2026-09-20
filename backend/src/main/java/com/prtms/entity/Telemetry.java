package com.prtms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "telemetry")
public class Telemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "platform_id", nullable = false)
    private Platform platform;

    @Column(nullable = false)
    private Integer batteryLevel;
    @Column(nullable = false)
    private Double temperature;
    @Column(nullable = false)
    private Integer linkQuality;
    @Column(nullable = false)
    private LocalDateTime timestamp;

    protected Telemetry() {
        // Required by JPA.
    }

    public Telemetry(Platform platform, Integer batteryLevel, Double temperature,
                     Integer linkQuality, LocalDateTime timestamp) {
        this.platform = platform;
        this.batteryLevel = batteryLevel;
        this.temperature = temperature;
        this.linkQuality = linkQuality;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public Platform getPlatform() { return platform; }
    public Integer getBatteryLevel() { return batteryLevel; }
    public Double getTemperature() { return temperature; }
    public Integer getLinkQuality() { return linkQuality; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
