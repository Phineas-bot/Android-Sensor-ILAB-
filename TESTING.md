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
  - Chart incremental-diff logic.
- Instrumentation tests (`app/src/androidTest`):
  - App shell navigation visibility.
  - Logs screen authentication gate rendering.
- Baseline benchmarks (`app/src/androidTest`):
  - Ring buffer add/snapshot throughput.
  - Chart diff engine incremental-delta throughput.

## Benchmark gate automation

- CI workflow: `.github/workflows/android-benchmark.yml`.
- Gate script: `scripts/benchmark-gate.ps1`.
- Baseline source: `benchmarks/benchmark-baseline.json`.
- Threshold configuration: `benchmarks/benchmark-thresholds.json`.
- Artifacts emitted in CI: `build/benchmark-reports/benchmark-report.json` and `build/benchmark-reports/benchmark-report.md`.

## Regression policy

- Median and p95 are both evaluated.
- Build fails when either median or p95 regression exceeds threshold.
- Default threshold is configurable via `BENCHMARK_MAX_REGRESSION_PERCENT`.
- Per-benchmark overrides can be set in `benchmarks/benchmark-thresholds.json`.
- Baseline can be updated in CI via workflow dispatch input `update_baseline=true`.

## Test design conventions

- Keep business logic in testable classes (pure Kotlin where possible).
- Prefer deterministic tests with fake dependencies and coroutines test dispatcher.
- Keep UI tests focused on user-visible outcomes rather than implementation details.
- Keep benchmark tests stable and trend-oriented; compare medians over time rather than single-run outliers.

## Expand with every new module

- New use case: at least one success-path and one failure/edge-case unit test.
- New screen: at least one instrumentation smoke test.
- Bug fix: add a regression test that would have failed before the fix.
