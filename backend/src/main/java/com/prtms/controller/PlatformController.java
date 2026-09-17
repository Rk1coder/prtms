package com.prtms.controller;

import com.prtms.dto.CreatePlatformRequest;
import com.prtms.dto.PlatformResponse;
import com.prtms.service.PlatformService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platforms")
public class PlatformController {
    private final PlatformService service;

    public PlatformController(PlatformService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlatformResponse createPlatform(@Valid @RequestBody CreatePlatformRequest request) {
        return service.createPlatform(request);
    }

    @GetMapping
    public List<PlatformResponse> getAllPlatforms() {
        return service.getAllPlatforms();
    }

    @GetMapping("/{platformCode}")
    public PlatformResponse getPlatformByCode(@PathVariable String platformCode) {
        return service.getPlatformByCode(platformCode);
    }
}
