package com.prtms.dto;

import com.prtms.entity.PlatformStatus;
import java.time.LocalDateTime;

public record ReadinessResponse(String platformCode, PlatformStatus status,
                                LocalDateTime latestTelemetryTime) {
}
