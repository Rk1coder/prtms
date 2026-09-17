package com.prtms.dto;

import com.prtms.entity.PlatformStatus;
import com.prtms.entity.PlatformType;

public record PlatformResponse(Long id, String platformCode, String name,
                               PlatformType type, PlatformStatus status) {
}
