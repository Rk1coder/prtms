package com.prtms.service;

import com.prtms.entity.*;
import com.prtms.rule.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthAssessmentServiceTest {
    private final HealthAssessmentService service = new HealthAssessmentService(List.of(
            new BatteryHealthRule(), new TemperatureHealthRule(), new LinkQualityHealthRule()));

    @ParameterizedTest
    @CsvSource({
            "80,55,90,READY",
            "30,60,80,DEGRADED",
            "80,75,80,DEGRADED",
            "80,55,30,DEGRADED",
            "15,55,90,NOT_READY",
            "30,90,80,NOT_READY",
            "80,75,20,NOT_READY",
            "15,90,20,NOT_READY"
    })
    void mostSevereRuleWins(int battery, double temperature, int link, PlatformStatus expected) {
        Telemetry telemetry = new Telemetry(null, battery, temperature, link, LocalDateTime.now());
        assertEquals(expected, service.assess(telemetry));
    }
}
