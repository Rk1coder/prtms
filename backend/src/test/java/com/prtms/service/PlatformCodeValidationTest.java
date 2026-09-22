package com.prtms.service;

import com.prtms.dto.CreatePlatformRequest;
import com.prtms.entity.PlatformType;
import jakarta.validation.Validation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PlatformCodeValidationTest {
    @ParameterizedTest
    @ValueSource(strings = {"UAV/001", "UAV\\001", "UAV 001", ".."})
    void rejectsCodesThatCannotBeUsedAsOneUrlSegment(String code) {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var request = new CreatePlatformRequest(code, "Demo UAV", PlatformType.UAV);
            assertFalse(factory.getValidator().validate(request).isEmpty());
        }
    }
}
