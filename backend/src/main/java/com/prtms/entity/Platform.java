package com.prtms.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "platforms")
public class Platform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String platformCode;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlatformType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlatformStatus status = PlatformStatus.UNKNOWN;

    protected Platform() {
        // JPA requires a no-argument constructor.
    }

    public Platform(String platformCode, String name, PlatformType type) {
        this.platformCode = platformCode;
        this.name = name;
        this.type = type;
    }

    public void setStatus(PlatformStatus status) { this.status = status; }

    public Long getId() { return id; }
    public String getPlatformCode() { return platformCode; }
    public String getName() { return name; }
    public PlatformType getType() { return type; }
    public PlatformStatus getStatus() { return status; }
}
