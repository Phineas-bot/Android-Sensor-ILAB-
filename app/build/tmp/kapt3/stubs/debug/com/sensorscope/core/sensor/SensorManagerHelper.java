package com.sensorscope.core.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.sensorscope.core.model.SensorData;
import com.sensorscope.core.model.SensorSample;
import com.sensorscope.core.model.SensorType;
import dagger.hilt.android.qualifiers.ApplicationContext;
import javax.inject.Inject;
import javax.inject.Singleton;
import kotlinx.coroutines.flow.Flow;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\f0\bJ\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eJ\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u000e2\u0006\u0010\u0012\u001a\u00020\tR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0006\u0012\u0004\u0018\u00010\n0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/sensorscope/core/sensor/SensorManagerHelper;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "sensorManager", "Landroid/hardware/SensorManager;", "sensors", "", "Lcom/sensorscope/core/model/SensorType;", "Landroid/hardware/Sensor;", "availableSensors", "", "observeAllSensors", "Lkotlinx/coroutines/flow/Flow;", "Lcom/sensorscope/core/model/SensorSample;", "observeSensor", "Lcom/sensorscope/core/model/SensorData;", "sensorType", "app_debug"})
public final class SensorManagerHelper {
    @org.jetbrains.annotations.NotNull()
    private final android.hardware.SensorManager sensorManager = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<com.sensorscope.core.model.SensorType, android.hardware.Sensor> sensors = null;
    
    @javax.inject.Inject()
    public SensorManagerHelper(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<com.sensorscope.core.model.SensorType, java.lang.Boolean> availableSensors() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.sensorscope.core.model.SensorData> observeSensor(@org.jetbrains.annotations.NotNull()
    com.sensorscope.core.model.SensorType sensorType) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.sensorscope.core.model.SensorSample> observeAllSensors() {
        return null;
    }
}