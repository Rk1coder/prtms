package com.prtms.dto;

import com.prtms.entity.PlatformStatus;
import java.time.LocalDateTime;

public record TelemetryResponse(Long id, String platformCode, Integer batteryLevel,
        Double temperature, Integer linkQuality, PlatformStatus status, LocalDateTime timestamp) {
}
