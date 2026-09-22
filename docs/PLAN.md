# PRTMS implementation plan

Goal: Complete the user-specified, synthetic Spring Boot + React learning POC.
Architecture: Controller → Service → Repository → H2; injected HealthRule implementations; React App → components → fetch API.
Constraints: Java 17, Spring Boot 3.5.x, plain JavaScript/CSS, no security, infrastructure, simulators or additional product scope.
Spec: User-provided detailed PRTMS request. Preserve existing platform work.

- [x] 1–2. Move existing backend into backend/, target Java 17; compile, run and check platform APIs.
- [x] 3. Add Telemetry, DTOs, repository, transactional service and REST endpoints; compile.
- [x] 4. Implement the three synthetic rules and list-based assessment; compile.
- [x] 5. Test healthy, degraded, critical and boundary values plus combined priority; run tests.
- [x] 6. Add springdoc and localhost:5173 CORS; check OpenAPI, UI and preflight.
- [x] 7. Bootstrap React/Vite JavaScript frontend; install and build.
- [x] 8–10. Add five small components, fetch service, forms, selection, history, readiness and plain CSS; handle loading, errors and empty states.
- [x] 11. Clean backend tests, frontend build, HTTP and browser demo checks; README and completion checklist.

Review focus: UNKNOWN without telemetry; boundary values; historical status independence; latest-ten ordering; failed requests and selection changes must not show stale platform data.
Validation: Existing PlatformService tests + four requested rule/assessment test classes; live HTTP checks for persistence/validation/errors; real browser form interaction.
