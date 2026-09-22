package com.prtms.service;

import com.prtms.entity.PlatformStatus;
import com.prtms.entity.Telemetry;
import com.prtms.rule.HealthRule;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HealthAssessmentService {
    private final List<HealthRule> healthRules;

    public HealthAssessmentService(List<HealthRule> healthRules) {
        this.healthRules = healthRules;
    }

    public PlatformStatus assess(Telemetry telemetry) {
        PlatformStatus result = PlatformStatus.READY;
        for (HealthRule rule : healthRules) {
            PlatformStatus ruleStatus = rule.evaluate(telemetry);
            if (ruleStatus == PlatformStatus.NOT_READY) {
                return PlatformStatus.NOT_READY;
            }
            if (ruleStatus == PlatformStatus.DEGRADED) {
                result = PlatformStatus.DEGRADED;
            }
        }
        return result;
    }
}
