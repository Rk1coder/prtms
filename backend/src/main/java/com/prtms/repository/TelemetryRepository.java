package com.prtms.repository;

import com.prtms.entity.Platform;
import com.prtms.entity.Telemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TelemetryRepository extends JpaRepository<Telemetry, Long> {
    List<Telemetry> findTop10ByPlatformOrderByTimestampDescIdDesc(Platform platform);
    Optional<Telemetry> findTopByPlatformOrderByTimestampDescIdDesc(Platform platform);
}
