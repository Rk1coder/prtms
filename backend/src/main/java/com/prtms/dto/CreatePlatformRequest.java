package com.prtms.dto;

import com.prtms.entity.PlatformType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePlatformRequest(
        @NotBlank String platformCode,
        @NotBlank String name,
        @NotNull PlatformType type) {
}