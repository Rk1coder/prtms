package com.prtms.dto;

import jakarta.validation.constraints.*;

public record TelemetryRequest(
        @NotBlank String platformCode,
        @NotNull @Min(0) @Max(100) Integer batteryLevel,
        @NotNull Double temperature,
        @NotNull @Min(0) @Max(100) Integer linkQuality) {
}
