package com.prtms.dto;

import com.prtms.entity.PlatformType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreatePlatformRequest(
        @NotBlank @Size(max = 255)
        @Pattern(regexp = "[A-Za-z0-9_-]+", message = "use letters, numbers, hyphens or underscores")
        String platformCode,
        @NotBlank @Size(max = 255) String name,
        @NotNull PlatformType type) {
}