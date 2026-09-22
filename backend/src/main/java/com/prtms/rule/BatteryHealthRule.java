package com.prtms.rule;

import com.prtms.entity.PlatformStatus;
import com.prtms.entity.Telemetry;
import org.springframework.stereotype.Component;

// Synthetic thresholds for this learning demo only.
@Component
public class BatteryHealthRule implements HealthRule {
    @Override
    public PlatformStatus evaluate(Telemetry telemetry) {
        if (telemetry.getBatteryLevel() < 20) {
            return PlatformStatus.NOT_READY;
        }
        if (telemetry.getBatteryLevel() < 35) {
            return PlatformStatus.DEGRADED;
        }
        return PlatformStatus.READY;
    }
}
