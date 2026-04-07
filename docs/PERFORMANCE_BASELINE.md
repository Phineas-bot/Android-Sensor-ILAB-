# Performance Baseline (Sprint 5)

This baseline is enforced by instrumentation tests in:
- app/src/androidTest/java/com/sensorscope/PerformanceBaselineInstrumentationTest.kt

## Metrics

- Startup baseline target: less than 5000 ms
- Memory baseline target: PSS less than 350000 KB after warmup navigation

## How to run

1. Connect a test device or emulator.
2. Run instrumentation tests:

```bash
./gradlew connectedDebugAndroidTest --tests "com.sensorscope.PerformanceBaselineInstrumentationTest"
```

## Notes

- Startup is measured from test start to first Dashboard tab render.
- Memory baseline uses Android PSS via `Debug.getPss()` after light UI warmup.
- Thresholds are conservative to reduce flaky failures across devices.
