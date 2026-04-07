# CSV Export Format

This document describes the exported CSV file structure produced by SensorScope.

## Schema version

- Current schema version: `2`
- The `schemaVersion` column is included in every row.

## Columns

`schemaVersion,sensorType,unit,x,y,z,timestampNanos,recordedAtMillis`

## Column definitions

- `schemaVersion`: Export schema identifier.
- `sensorType`: Sensor enum name (for example `ACCELEROMETER`, `LIGHT`, `PRESSURE`).
- `unit`: Sensor unit tied to `sensorType` (for example `m/s^2`, `lux`, `hPa`).
- `x`: Primary value for single-value sensors, X axis for tri-axis sensors.
- `y`: Y axis for tri-axis sensors, otherwise `0.0` placeholder.
- `z`: Z axis for tri-axis sensors, otherwise `0.0` placeholder.
- `timestampNanos`: Sensor-event timestamp from Android sensor framework.
- `recordedAtMillis`: Wall-clock capture time (`System.currentTimeMillis`).

## Notes

- Single-value sensors currently use `x` as the meaningful channel.
- Keep parser logic tolerant of future schema versions by checking `schemaVersion` first.