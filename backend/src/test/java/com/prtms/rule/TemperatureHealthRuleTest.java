package com.prtms.rule;

import com.prtms.entity.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TemperatureHealthRuleTest {
    @ParameterizedTest
    @CsvSource({"-10,READY",
            "70,READY",
            "70.1,DEGRADED",
            "85,DEGRADED",
            "85.1,NOT_READY",
            "100,NOT_READY"})
    void evaluatesSyntheticThresholds(double value, PlatformStatus expected) {
        Telemetry telemetry = new Telemetry(null, 80, value, 90, LocalDateTime.now());
        assertEquals(expected, new TemperatureHealthRule().evaluate(telemetry));
    }
}
