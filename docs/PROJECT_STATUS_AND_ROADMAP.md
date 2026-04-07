# SensorILAB Project Status and Development Roadmap

## Date
April 7, 2026

## Executive Summary
Based on the current codebase, progress is strong for a focused MVP and partial for the broader final vision.

- Estimated completion for current 3-sensor MVP scope: about 80%
- Estimated completion for full target vision in project description: about 50%

## Current Strengths (Implemented)

### 1. Architecture and stack are in place
- Clean layering across core, data, domain, and presentation.
- Modern Android stack is used consistently: Kotlin, Compose, Coroutines/Flow, Room, Hilt.

### 2. Real-time pipeline is functional
- Live capture for accelerometer, gyroscope, and magnetometer is implemented.
- Flow-based streaming is integrated into ViewModel state and UI charts.

### 3. Product features already available
- Dashboard with live values and chart previews.
- Sensor detail view with full chart and controls.
- Session logging with Room persistence.
- CSV export and share flow.
- Sensor Labs (Shake challenge, Magnetic north).
- Biometric gate for logs screen.

### 4. Baseline testing exists
- Unit tests for ring buffer, lab logic, and collection scope behavior.
- Instrumentation smoke tests for tab visibility and logs auth prompt.

## Gaps Compared to Final Expectations

### 1. Sensor breadth gap
The current app supports three sensors only:
- Accelerometer
- Gyroscope
- Magnetometer

The project description targets a wider set:
- Environmental sensors
- Audio input
- Camera metadata
- Bluetooth signals
- Extended diagnostics use cases

### 2. Sampling-control gap
A fast-sampling toggle exists in UI, but it is not yet wired end-to-end to sensor registration behavior.

### 3. Production hardening gap
- Release minification is currently disabled.
- CI/release quality gates are not yet visible in this repository state.
- Non-functional requirements (observability, resilience, scalability checks) are still limited.

### 4. Testing depth gap
Current tests are a good start, but coverage is not yet sufficient for robust production confidence, especially around:
- Export edge cases
- Session lifecycle edge conditions
- Error handling and regression scenarios
- Wider instrumentation coverage on critical user journeys

## Recommended Development Plan

## Detailed Development Roadmap (Execution Plan)

## Planning assumptions
- Team shape: 1 to 3 Android engineers, 1 QA shared, no dedicated backend required yet.
- Sprint length: 1 week.
- Release target: internal beta in 6 weeks, public candidate in 10 to 12 weeks.
- Product strategy: stabilize current core first, then expand sensors incrementally.

## Milestone overview

### Milestone M1: Core reliability and sampling control (Weeks 1 to 2)
Objective: make the current 3-sensor product deterministic, testable, and stable under continuous use.

Deliverables:
- Real fast and normal sampling modes implemented end-to-end.
- Chart update path optimized for sustained stream.
- Session start, stop, export, and logs flows covered by regression tests.

Definition of done:
- Sampling mode switch changes effective sample cadence.
- App runs for 20+ minutes with no freeze, crash, or severe frame drops on at least 2 devices.
- Unit and instrumentation suites pass locally and in CI.

### Milestone M2: Environmental sensor expansion (Weeks 3 to 5)
Objective: move from single-purpose demo behavior toward instrumentation utility.

Deliverables:
- Light sensor support.
- Proximity sensor support.
- Pressure sensor support when available.
- Sensor capability metadata model for units and interpretation labels.

Definition of done:
- New sensors appear conditionally by availability.
- Each new sensor supports live view, log persistence, and CSV export.
- Each new sensor has at least one unit test and one instrumentation smoke path.

### Milestone M3: Production hardening and release preparation (Weeks 6 to 8)
Objective: increase operational confidence and release readiness.

Deliverables:
- CI pipeline with required checks.
- Release build optimization enabled and validated.
- Crash reporting and minimal analytics.
- Security and privacy review pass.

Definition of done:
- Every pull request has green checks before merge.
- Release build verified on low-end and mid-range Android devices.
- Release checklist completed with no blockers.

### Milestone M4: Advanced modalities and roadmap bridge (Weeks 9 to 12)
Objective: start larger vision modules while preserving architecture quality.

Deliverables:
- Bluetooth signal scanning module (MVP).
- Audio level capture module (MVP).
- Camera metadata capture module (MVP).
- Architecture decision records for next-generation analytics.

Definition of done:
- Each modality behind feature flags.
- Battery, permission, and privacy impact documented.
- No regression in core sensor workflows.

## Sprint-by-sprint breakdown

## Sprint 1 (Week 1): Sampling and data-path correctness
Primary outcome: real sampling control and predictable data flow.

Tasks:
- Introduce sampling mode enum and wire mode through ViewModel to repository and sensor registration.
- Replace placeholder fast sampling behavior with actual sensor delay selection.
- Add explicit state in UI for active sampling mode.
- Add unit tests for sampling mode propagation and repository behavior.

Acceptance criteria:
- Toggle changes observed sampling frequency by measurable ratio.
- No stale mode state after start and stop cycles.
- Existing tests continue to pass.

## Sprint 2 (Week 2): Chart performance and session reliability
Primary outcome: smooth rendering and robust session lifecycle.

Tasks:
- Optimize chart updates to reduce full dataset rebuild frequency.
- Profile and remove avoidable allocations in chart and stream path.
- Add tests for session lifecycle edge cases.
- Add instrumentation test for start to stream to stop flow.

Acceptance criteria:
- Stable rendering without severe stutter on 10-minute stream.
- Session records always close correctly after stop.
- Exportable CSV produced after each recorded session.

## Sprint 3 (Week 3): Light and proximity sensor integration
Primary outcome: first expansion beyond current 3-sensor set.

Tasks:
- Add sensor type entries and availability checks for light and proximity.
- Update dashboard and detail rendering for non-XYZ sensors where needed.
- Ensure logs and export support new schemas cleanly.
- Add interpretation copy for new sensors.

Acceptance criteria:
- Devices with supported hardware show and stream both sensors.
- Devices without hardware show graceful unavailable states.
- Tests added for capability mapping and UI visibility.

## Sprint 4 (Week 4): Pressure sensor and metadata standardization
Primary outcome: complete environmental sensor MVP with consistent model.

Tasks:
- Add pressure sensor support.
- Introduce capability metadata object including unit, axis model, and display format.
- Refactor chart and detail components to be data-model driven.
- Update export format documentation.

Acceptance criteria:
- Pressure readings logged and exported correctly.
- Metadata model used by all sensors in UI.
- No duplicated branching logic per sensor in screens.

## Sprint 5 (Week 5): Test hardening and quality gate expansion
Primary outcome: confidence before release hardening.

Tasks:
- Add regression tests for logs authentication flow outcomes.
- Add export failure-path tests and empty-session behavior tests.
- Add instrumentation test for navigation to each sensor details screen.
- Measure baseline startup time and memory usage.

Acceptance criteria:
- Coverage improved for critical paths.
- No known high-severity defects open from core flows.
- Performance baseline documented.

## Sprint 6 (Week 6): CI and release build hardening
Primary outcome: repeatable and secure delivery pipeline.

Tasks:
- Create CI workflow for lint, unit test, instrumentation smoke, and assemble release.
- Enable and validate release shrinking and obfuscation.
- Add mapping retention strategy and release notes automation.
- Define branch protection and merge policy.

Acceptance criteria:
- CI required checks block merges on failure.
- Release build installs and runs on test devices.
- No new critical startup or runtime issues in release variant.

## Sprint 7 (Week 7): Observability and privacy controls
Primary outcome: operational visibility and compliance posture.

Tasks:
- Add crash reporting integration.
- Add minimal analytics for feature usage with privacy-first defaults.
- Add consent and data-handling documentation.
- Audit export and storage behavior for scoped storage compliance.

Acceptance criteria:
- Crash events visible with version tagging.
- No sensitive raw personal data sent externally.
- Privacy documentation and user-facing disclosure updated.

## Sprint 8 (Week 8): Beta readiness and stabilization
Primary outcome: internal beta candidate.

Tasks:
- Device matrix testing across Android versions and vendors.
- Triage and fix top defects.
- Finalize known issues and beta feedback channels.
- Prepare rollout plan and rollback strategy.

Acceptance criteria:
- Beta quality checklist completed.
- No blocker defects remain open.
- Internal stakeholders sign off.

## Workstream-specific backlog

### A. Sensor platform and data model
- Add SensorCapability metadata: unit, axis count, expected range, interpretation hints.
- Normalize single-axis versus three-axis handling.
- Add sensor health status: available, unavailable, degraded.

### B. UI and UX
- Harmonize detail screens for axis and non-axis sensors.
- Add in-app interpretation tips per sensor card.
- Add explicit recording indicator and elapsed timer.
- Add export history and status feedback.

### C. Persistence and export
- Version CSV format with schema identifier.
- Add export retry and error mapping.
- Add optional compression for long sessions.
- Add import-ready sample files for tests.

### D. Testing and quality engineering
- Unit: use case, repository, and mapper edge cases.
- Integration: sensor stream to persistence to export roundtrip.
- Instrumentation: end-to-end user journeys.
- Non-functional: long-session battery and memory profiling.

### E. Security and privacy
- Keep biometric gate for logs and evaluate lock timeout policy.
- Verify file provider and media storage paths.
- Add permission rationale flows and denial handling.
- Document data retention behavior.

## Key metrics to track weekly

### Delivery metrics
- Sprint completion rate.
- Open blocker count.
- Change failure rate.

### Quality metrics
- Crash-free sessions percentage.
- Regression defects per sprint.
- Test pass rate and flakiness.

### Performance metrics
- Stream stability duration before degradation.
- Average frame time during live charting.
- Memory growth during 20-minute session.

### Product metrics
- Session creation to export completion rate.
- Feature usage by sensor type.
- Biometric unlock success rate.

## Dependency map
- Sampling control should be completed before chart performance tuning, because data cadence affects rendering behavior.
- Capability metadata should be completed before broad sensor expansion, to avoid repeated refactors.
- CI should be in place before heavy multi-module expansion, to control regression risk.
- Privacy and permission review should precede Bluetooth, audio, and camera module rollout.

## Risks and mitigation plan

### Risk 1: Sensor fragmentation across devices
Mitigation:
- Build feature availability checks as first-class UI state.
- Keep fallback messaging and test matrix broad.

### Risk 2: Performance degradation with more streams
Mitigation:
- Enforce profiling in each milestone.
- Gate merges that worsen baseline thresholds.

### Risk 3: Scope growth outruns quality capacity
Mitigation:
- Keep strict milestone gates.
- Do not start new modality while blocker defects remain in core flows.

### Risk 4: Privacy and permission complexity
Mitigation:
- Add review checklist per new modality.
- Keep clear user consent and transparency notes.

## Immediate next actions (this week)
1. Create milestone tickets M1 through M4 in your tracker.
2. Create Sprint 1 stories from the Sampling and data-path correctness section.
3. Assign owners and estimate each story in hours.
4. Set baseline performance numbers before first refactor.
5. Run Sprint 1 kickoff with acceptance criteria pinned.

## Suggested issue templates

### Feature issue template
- Title: feature scope and sensor/module name.
- Problem statement: user and technical need.
- Implementation notes: architecture touchpoints.
- Acceptance criteria: testable outcomes.
- Test plan: unit, integration, instrumentation.
- Risk and rollback notes.

### Bug issue template
- Title: concise defect summary.
- Reproduction steps.
- Expected versus actual behavior.
- Device and Android version.
- Logs and screenshots.
- Regression test requirement.
