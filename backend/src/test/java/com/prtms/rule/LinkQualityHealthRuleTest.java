package com.prtms.rule;

import com.prtms.entity.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LinkQualityHealthRuleTest {
    @ParameterizedTest
    @CsvSource({"0,NOT_READY",
            "24,NOT_READY",
            "25,DEGRADED",
            "49,DEGRADED",
            "50,READY",
            "100,READY"})
    void evaluatesSyntheticThresholds(int value, PlatformStatus expected) {
        Telemetry telemetry = new Telemetry(null, 80, 55.0, value, LocalDateTime.now());
        assertEquals(expected, new LinkQualityHealthRule().evaluate(telemetry));
    }
}
