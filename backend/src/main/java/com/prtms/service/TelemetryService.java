package com.prtms.service;

import com.prtms.dto.*;
import com.prtms.entity.*;
import com.prtms.exception.PlatformNotFoundException;
import com.prtms.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TelemetryService {
    private static final Logger log = LoggerFactory.getLogger(TelemetryService.class);
    private final PlatformRepository platforms;
    private final TelemetryRepository telemetryRepository;

    private final HealthAssessmentService healthAssessment;

    public TelemetryService(PlatformRepository platforms, TelemetryRepository telemetryRepository,
                            HealthAssessmentService healthAssessment) {
        this.healthAssessment = healthAssessment;
        this.platforms = platforms;
        this.telemetryRepository = telemetryRepository;
    }

    @Transactional
    public TelemetryResponse addTelemetry(TelemetryRequest request) {
        Platform platform = findPlatform(request.platformCode());
        Telemetry telemetry = new Telemetry(platform, request.batteryLevel(), request.temperature(),
                request.linkQuality(), LocalDateTime.now());
        telemetryRepository.save(telemetry);
        log.info("Telemetry received for: {}", platform.getPlatformCode());
        PlatformStatus previousStatus = platform.getStatus();
        platform.setStatus(healthAssessment.assess(telemetry));
        platforms.save(platform);
        log.info("Platform status updated: {} -> {}", previousStatus, platform.getStatus());
        return toResponse(telemetry);
    }

    @Transactional(readOnly = true)
    public List<TelemetryResponse> getHistory(String platformCode) {
        Platform platform = findPlatform(platformCode);
        return telemetryRepository.findTop10ByPlatformOrderByTimestampDescIdDesc(platform)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ReadinessResponse getReadiness(String platformCode) {
        Platform platform = findPlatform(platformCode);
        LocalDateTime latestTime = telemetryRepository.findTopByPlatformOrderByTimestampDescIdDesc(platform)
                .map(Telemetry::getTimestamp).orElse(null);
        return new ReadinessResponse(platformCode, platform.getStatus(), latestTime);
    }

    private Platform findPlatform(String code) {
        return platforms.findByPlatformCode(code)
                .orElseThrow(() -> new PlatformNotFoundException(code));
    }

    private TelemetryResponse toResponse(Telemetry telemetry) {
        return new TelemetryResponse(telemetry.getId(), telemetry.getPlatform().getPlatformCode(),
                telemetry.getBatteryLevel(), telemetry.getTemperature(), telemetry.getLinkQuality(),
                healthAssessment.assess(telemetry), telemetry.getTimestamp());
    }
}
