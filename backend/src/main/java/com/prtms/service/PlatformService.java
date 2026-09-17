package com.prtms.service;

import com.prtms.dto.CreatePlatformRequest;
import com.prtms.dto.PlatformResponse;
import com.prtms.entity.Platform;
import com.prtms.exception.DuplicatePlatformException;
import com.prtms.exception.PlatformNotFoundException;
import com.prtms.repository.PlatformRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlatformService {
    private static final Logger log = LoggerFactory.getLogger(PlatformService.class);
    private final PlatformRepository repository;

    public PlatformService(PlatformRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public PlatformResponse createPlatform(CreatePlatformRequest request) {
        if (repository.existsByPlatformCode(request.platformCode())) {
            throw new DuplicatePlatformException(request.platformCode());
        }
        Platform platform = new Platform(request.platformCode(), request.name(), request.type());
        Platform saved = repository.save(platform);
        log.info("Platform created: {}", saved.getPlatformCode());
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PlatformResponse> getAllPlatforms() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PlatformResponse getPlatformByCode(String platformCode) {
        Platform platform = repository.findByPlatformCode(platformCode)
                .orElseThrow(() -> new PlatformNotFoundException(platformCode));
        return toResponse(platform);
    }

    private PlatformResponse toResponse(Platform platform) {
        return new PlatformResponse(platform.getId(), platform.getPlatformCode(),
                platform.getName(), platform.getType(), platform.getStatus());
    }
}
