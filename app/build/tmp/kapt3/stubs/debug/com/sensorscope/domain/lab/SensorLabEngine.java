package com.sensorscope.domain.lab;

import com.sensorscope.core.model.SensorData;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tJ\u000e\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\tR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/sensorscope/domain/lab/SensorLabEngine;", "", "()V", "northThresholdDegrees", "", "shakeThreshold", "updateMagneticNorthLab", "Lcom/sensorscope/domain/lab/SensorLab;", "magnetometer", "Lcom/sensorscope/core/model/SensorData;", "updateShakeLab", "accelerometer", "app_debug"})
public final class SensorLabEngine {
    private final float shakeThreshold = 15.0F;
    private final float northThresholdDegrees = 20.0F;
    
    public SensorLabEngine() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sensorscope.domain.lab.SensorLab updateShakeLab(@org.jetbrains.annotations.NotNull()
    com.sensorscope.core.model.SensorData accelerometer) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sensorscope.domain.lab.SensorLab updateMagneticNorthLab(@org.jetbrains.annotations.NotNull()
    com.sensorscope.core.model.SensorData magnetometer) {
        return null;
    }
}