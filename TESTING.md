# SensorScope Testing Strategy

## Rule for every feature change
- Add or update unit tests for business logic and state transitions.
- Add or update instrumentation tests for critical user flows.
- Do not merge feature work without test updates unless blocked by platform constraints.

## Current test layers
- Unit tests (`app/src/test`):
  - Ring buffer behavior and data retention.
  - Sensor lab goal conditions.
  - ViewModel collection scope behavior.
- Instrumentation tests (`app/src/androidTest`):
  - App shell navigation visibility.
  - Logs screen authentication gate rendering.

## Test design conventions
- Keep business logic in testable classes (pure Kotlin where possible).
- Prefer deterministic tests with fake dependencies and coroutines test dispatcher.
- Keep UI tests focused on user-visible outcomes rather than implementation details.

## Expand with every new module
- New use case: at least one success-path and one failure/edge-case unit test.
- New screen: at least one instrumentation smoke test.
- Bug fix: add a regression test that would have failed before the fix.
