package com.prtms.rule;

import com.prtms.entity.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BatteryHealthRuleTest {
    @ParameterizedTest
    @CsvSource({"0,NOT_READY",
            "19,NOT_READY",
            "20,DEGRADED",
            "34,DEGRADED",
            "35,READY",
            "100,READY"})
    void evaluatesSyntheticThresholds(int value, PlatformStatus expected) {
        Telemetry telemetry = new Telemetry(null, value, 55.0, 90, LocalDateTime.now());
        assertEquals(expected, new BatteryHealthRule().evaluate(telemetry));
    }
}
