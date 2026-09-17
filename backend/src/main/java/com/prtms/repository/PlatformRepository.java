package com.prtms.repository;

import com.prtms.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlatformRepository extends JpaRepository<Platform, Long> {
    Optional<Platform> findByPlatformCode(String platformCode);
    boolean existsByPlatformCode(String platformCode);
}
