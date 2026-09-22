package com.prtms.rule;

import com.prtms.entity.PlatformStatus;
import com.prtms.entity.Telemetry;
import org.springframework.stereotype.Component;

// Synthetic thresholds for this learning demo only.
@Component
public class TemperatureHealthRule implements HealthRule {
    @Override
    public PlatformStatus evaluate(Telemetry telemetry) {
        if (telemetry.getTemperature() > 85) {
            return PlatformStatus.NOT_READY;
        }
        if (telemetry.getTemperature() > 70) {
            return PlatformStatus.DEGRADED;
        }
        return PlatformStatus.READY;
    }
}
