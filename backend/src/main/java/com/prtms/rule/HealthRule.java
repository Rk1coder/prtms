package com.prtms.rule;

import com.prtms.entity.PlatformStatus;
import com.prtms.entity.Telemetry;

public interface HealthRule {
    PlatformStatus evaluate(Telemetry telemetry);
}
