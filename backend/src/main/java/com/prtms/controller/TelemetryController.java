package com.prtms.controller;

import com.prtms.dto.*;
import com.prtms.service.TelemetryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TelemetryController {
    private final TelemetryService service;

    public TelemetryController(TelemetryService service) {
        this.service = service;
    }

    @PostMapping("/telemetry")
    @ResponseStatus(HttpStatus.CREATED)
    public TelemetryResponse addTelemetry(@Valid @RequestBody TelemetryRequest request) {
        return service.addTelemetry(request);
    }

    @GetMapping("/platforms/{platformCode}/telemetry")
    public List<TelemetryResponse> getHistory(@PathVariable String platformCode) {
        return service.getHistory(platformCode);
    }

    @GetMapping("/platforms/{platformCode}/readiness")
    public ReadinessResponse getReadiness(@PathVariable String platformCode) {
        return service.getReadiness(platformCode);
    }
}
