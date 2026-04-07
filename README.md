# SensorScope - Mobile Sensor Analytics Platform

SensorScope is a production-oriented Android app that streams hardware sensor data, visualizes multi-axis values in real time, logs sessions with Room, exports CSV files, and includes interactive Sensor Labs.

## Tech
- Kotlin
- MVVM + clean layering (core/data/domain/presentation)
- Jetpack Compose + Navigation Compose
- Coroutines + Flow
- Room
- Hilt
- MPAndroidChart
- BiometricPrompt

## Run
1. Open the project in Android Studio (Hedgehog+ recommended).
2. Set Gradle JDK to Java 17.
3. Sync and run on device (API 26+).

## Testing
- Unit tests are in `app/src/test`.
- Instrumentation tests are in `app/src/androidTest`.
- Team-wide testing expectations are defined in `TESTING.md`.

## Documentation
- Sensor graph interpretation guide: `docs/SENSOR_GRAPH_INTERPRETATION.md`
- CSV export format: `docs/CSV_EXPORT_FORMAT.md`
- Performance baseline notes: `docs/PERFORMANCE_BASELINE.md`

## Feature coverage
- Real-time accelerometer/gyroscope/magnetometer/light/proximity/pressure streams
- Real-time audio level stream (microphone)
- Real-time Bluetooth signal stream (BLE RSSI)
- Camera metadata stream (device camera capability score)
- Dashboard with cards, live values, preview charts
- Sensor details with full chart + controls
- Derived analytics: trend summaries and cross-sensor insights
- Session logging to Room
- CSV export and share intent
- Sensor Labs (Shake challenge, Magnetic north)
- Biometric lock for logs screen
- Graceful sensor-unavailable handling
