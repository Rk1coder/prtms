package com.prtms.rule;

import com.prtms.entity.PlatformStatus;
import com.prtms.entity.Telemetry;
import org.springframework.stereotype.Component;

// Synthetic thresholds for this learning demo only.
@Component
public class LinkQualityHealthRule implements HealthRule {
    @Override
    public PlatformStatus evaluate(Telemetry telemetry) {
        if (telemetry.getLinkQuality() < 25) {
            return PlatformStatus.NOT_READY;
        }
        if (telemetry.getLinkQuality() < 50) {
            return PlatformStatus.DEGRADED;
        }
        return PlatformStatus.READY;
    }
}
