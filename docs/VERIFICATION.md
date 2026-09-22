# Verification — 22 September 2026

## Commands and results

- Actual JDK 17 (Temurin, downloaded temporarily to `/tmp/prtms-tools/jdk17`).
- `JAVA_HOME=/tmp/prtms-tools/jdk17 ./mvnw -o clean test`: **BUILD SUCCESS**.
- **33 tests, 0 failures, 0 errors, 0 skipped**.
- `npm run build`: **PASS**, Vite 7.3.6, 35 modules.
- npm dependency audit after Vite patch update: **0 known vulnerabilities**.
- 38 live HTTP assertions passed on the final backend; an additional invalid-code POST returned 400 with a field error.
- Browser form checks performed in Codex in-app browser against localhost:5173 and the real backend.
- Desktop visual inspection and 390×844 mobile inspection passed; document width 375, viewport 390 (no page overflow).

## Definition of Done

- [x] Spring Boot application starts — Java 17 startup log and HTTP responses.
- [x] H2 connects — startup log, successful storage/retrieval and console page.
- [x] Platform create works — HTTP 201 and browser form.
- [x] Platform list works — HTTP 200 and table.
- [x] Platform detail works — HTTP 200.
- [x] Telemetry submit works — HTTP 201 and browser form.
- [x] Telemetry is stored — subsequent GET returns saved values.
- [x] Health rules work — 18 rule boundary/normal cases.
- [x] READY works — 80 / 55 / 90, HTTP and browser.
- [x] DEGRADED works — 30 / 60 / 80, HTTP and browser.
- [x] NOT_READY works — 15 / 90 / 20, HTTP and browser.
- [x] Platform status updates — subsequent detail, table and summary checks.
- [x] Telemetry history works — per-reading statuses, descending order, latest ten after thirteen submissions.
- [x] Readiness endpoint works — UNKNOWN/null before telemetry and current status/time afterward.
- [x] Validation works — invalid ranges, blanks, null fields, invalid enum and unsafe platform code.
- [x] Exception handling works — 400, 404 and 409 HTTP responses.
- [x] Swagger works — /swagger-ui.html loads and /v3/api-docs lists six operations over five paths.
- [x] CORS works — OPTIONS response allows localhost:5173; cross-origin browser POST succeeds.
- [x] Health rule unit tests pass.
- [x] React application starts — Vite and browser.
- [x] Platform form works — form resets, list refreshes, duplicate message displayed.
- [x] Platform table works.
- [x] Summary cards work — state changes reflected; final observed counts 3 / 1 / 1 / 1.
- [x] Platform selection works — switching to untouched UGV clears UAV data and shows UNKNOWN/empty.
- [x] Telemetry form works.
- [x] Readiness is displayed.
- [x] Telemetry history is displayed.
- [x] Loading/error/empty states exist — initial loading, zero platforms, zero telemetry; backend stopped to check connection error and restarted to check Retry.
- [x] Frontend build passes.
- [x] Backend tests pass.
- [x] README is complete.

## Review fix

Independent code review found that a code containing `/` could be created but not retrieved through a path parameter.
Live reproduction: POST `SLASH/001` returned 201; encoded-slash detail returned 400.
Four regression cases failed before the fix and passed afterward. Added a small `@Pattern` constraint
and matching HTML pattern/title. Valid codes such as UAV-001 still work in the browser.

The HTTP verification script initially had three assertion/reporting mistakes (urllib request method accessor,
H2 timestamp precision and counting paths rather than HTTP operations). These were corrected; the reported
38 checks are from the complete successful run, not partial attempts.

## Limits and decisions

- Synthetic, single-user local demo; no security or production infrastructure.
- In-memory data is deliberately lost on backend restart.
- Java records keep DTOs small, matching the original project style.
- Historical health is recomputed from that record's metrics using current demo rules.
- Timestamp has no timezone; local frontend/backend clock context is assumed.
- No claim of frontend automated unit-test coverage: frontend was built and exercised through a real browser.
- Maven/Java/Node tools were used locally; no global software installation or deployment was performed.

## Portable setup check

A new temporary folder was populated only with files eligible for Git (no target, node_modules,
dist or IDE files). On that copy:

- JDK 17 + Maven Wrapper: clean test passed, 33 tests, zero failures/errors.
- npm ci from the committed-format lockfile: install succeeded; npm run build passed.
- Backend started on an automatically assigned test port, leaving the user's running app untouched.
- Both localhost:5173 and 127.0.0.1:5173 passed GET CORS and POST preflight checks.

Quick Start uses relative paths, documents Windows wrapper usage, prerequisites, empty initial data
and the two-terminal workflow. First installation needs internet access for dependencies.
