package com.prtms.service;

import com.prtms.dto.CreatePlatformRequest;
import com.prtms.entity.Platform;
import com.prtms.entity.PlatformStatus;
import com.prtms.entity.PlatformType;
import com.prtms.exception.DuplicatePlatformException;
import com.prtms.exception.PlatformNotFoundException;
import com.prtms.repository.PlatformRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatformServiceTest {
    @Mock
    private PlatformRepository repository;

    @InjectMocks
    private PlatformService service;

    @Test
    void shouldCreatePlatformSuccessfully() {
        var request = new CreatePlatformRequest("UAV-001", "Demo UAV", PlatformType.UAV);
        when(repository.save(any(Platform.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.createPlatform(request);

        assertEquals("UAV-001", response.platformCode());
        assertEquals("Demo UAV", response.name());
        assertEquals(PlatformType.UAV, response.type());
        assertEquals(PlatformStatus.UNKNOWN, response.status());
        verify(repository).save(argThat(platform ->
                platform.getPlatformCode().equals("UAV-001")
                        && platform.getStatus() == PlatformStatus.UNKNOWN));
    }

    @Test
    void shouldThrowExceptionWhenPlatformAlreadyExists() {
        when(repository.existsByPlatformCode("UAV-001")).thenReturn(true);

        var exception = assertThrows(DuplicatePlatformException.class, () ->
                service.createPlatform(new CreatePlatformRequest("UAV-001", "Demo UAV", PlatformType.UAV)));

        assertEquals("Platform already exists: UAV-001", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenPlatformNotFound() {
        when(repository.findByPlatformCode("UAV-001")).thenReturn(Optional.empty());

        var exception = assertThrows(PlatformNotFoundException.class,
                () -> service.getPlatformByCode("UAV-001"));

        assertEquals("Platform not found: UAV-001", exception.getMessage());
    }
}
