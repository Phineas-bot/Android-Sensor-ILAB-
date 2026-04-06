# Sensor Graph Interpretation Guide

This guide explains how to read the live sensor charts in SensorScope and how to interpret common patterns for each sensor.

## 1) How the chart is built in this app

- The chart always shows three lines: X, Y, and Z axes.
- Colors are fixed:
  - X axis: cyan
  - Y axis: mint/green
  - Z axis: orange
- The X-axis of the graph is sample index, not wall-clock time.
- The app keeps a rolling window of up to 180 most recent samples per sensor.
- When new samples arrive after the window is full, oldest points are dropped.

Practical meaning:
- You are seeing recent history, not the entire session.
- The visual width of an event depends on sampling frequency and motion duration.

## 2) Units and axis conventions

### Accelerometer
- Unit: m/s^2
- Measures acceleration on each phone axis, including gravity.
- At rest, the vector magnitude is often close to about 9.81 m/s^2.

### Gyroscope
- Unit: rad/s
- Measures angular velocity around each phone axis.
- At rest, values are usually close to 0.

### Magnetometer
- Unit: uT (microtesla)
- Measures ambient magnetic field components.
- Readings vary with orientation and nearby magnetic materials.

## 3) Interpreting graph shapes

### Stable flat lines
Usually means little movement or stable orientation.

### Smooth slope or curve
Usually means gradual tilt or rotation.

### Sudden sharp spikes
Usually means abrupt movement, taps, shakes, or quick turns.

### High-frequency jitter
Could indicate vibration, sensor noise, or unstable hand position.

### Clipping-like behavior
If values seem to hit similar extremes repeatedly, movement may be saturating practical range or the device is being moved aggressively.

## 4) Sensor-specific interpretation

## Accelerometer graph
- Large transient peaks on one or more axes: sudden linear acceleration or impact.
- Slow transfer of energy between axes: orientation change relative to gravity.
- Near-static with one dominant axis around +/-9.8 and others near 0: device is stationary with gravity aligned mostly to that axis.

Interpretation tip:
- Distinguish gravity-driven baseline from dynamic motion by watching how quickly lines change.

## Gyroscope graph
- Values near zero on all axes: no significant rotation.
- Positive/negative deflections on an axis: rotation direction around that axis.
- Burst-like patterns: quick twists.

Interpretation tip:
- Gyroscope reacts to rotation, not linear movement. A straight-line move with minimal rotation may not produce large gyro changes.

## Magnetometer graph
- Slowly varying baseline as you rotate phone: expected heading-related change.
- Abrupt jumps or unstable noise: nearby ferromagnetic objects, electronics, or magnetic interference.

Interpretation tip:
- For heading-like use, move away from laptops, speakers, metal desks, and magnetic cases.

## 5) Labs linkage to graph meaning

The Labs feature derives interpretations from live sensor data:

- Shake Challenge uses accelerometer magnitude:
  magnitude = sqrt(x^2 + y^2 + z^2)
  Completion threshold is >= 15 m/s^2.

- Magnetic North lab computes heading from magnetometer:
  headingDeg = atan2(y, x) converted to degrees and normalized to [0, 360).
  Completion is near north (0 deg +/- 20 deg).

How to use this with charts:
- In Shake, look for clear multi-axis acceleration spikes that push magnitude over threshold.
- In Magnetic North, rotate slowly and look for stable magnetometer behavior before fine alignment.

## 6) Sampling and timing caveats

- The chart X-axis is sample count, so equal horizontal spacing does not guarantee equal real-time spacing under all runtime conditions.
- The current sensor listener registration uses normal delay mode from Android SensorManager.
- The Fast sampling switch in the details UI currently shows an instruction message and requires stream restart to apply; it does not yet directly change listener delay in code.

## 7) Practical reading workflow

1. Start stream and keep phone still for 2 to 3 seconds.
2. Observe baseline for each axis.
3. Perform one controlled motion (tilt, rotate, shake).
4. Correlate which axis responds most.
5. Repeat with another motion and compare patterns.
6. If needed, log a session and export CSV for offline analysis.

## 8) Common troubleshooting while interpreting graphs

- No lines shown:
  - Confirm sensor is available on device.
  - Confirm stream is started.

- Values look noisy:
  - Hold device steady.
  - Reduce nearby magnetic/electronic interference (for magnetometer).

- Patterns seem inconsistent between runs:
  - Keep motion protocol consistent.
  - Re-test with similar orientation and speed.

## 9) Quick reference

- X line: first sensor axis
- Y line: second sensor axis
- Z line: third sensor axis
- Big spike: sudden event
- Flat line: stable state
- Gradual slope: slow orientation/rotation change
- Accelerometer near 9.81 magnitude at rest is normal
- Gyroscope near zero at rest is normal
- Magnetometer is environment-sensitive
