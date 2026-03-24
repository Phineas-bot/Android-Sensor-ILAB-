package com.sensorscope.presentation.sensor;

import com.sensorscope.core.model.SensorData;
import com.sensorscope.core.model.SensorType;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0016\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001Bo\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0014\b\u0002\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00030\u0005\u0012\u0014\b\u0002\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\b0\u0005\u0012\u001a\b\u0002\u0010\t\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\n0\u0005\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e\u00a2\u0006\u0002\u0010\u000fJ\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003J\u0015\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00030\u0005H\u00c6\u0003J\u0015\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\b0\u0005H\u00c6\u0003J\u001b\u0010\u001d\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\n0\u0005H\u00c6\u0003J\u0010\u0010\u001e\u001a\u0004\u0018\u00010\fH\u00c6\u0003\u00a2\u0006\u0002\u0010\u0014J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\u000eH\u00c6\u0003Jx\u0010 \u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0014\b\u0002\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00030\u00052\u0014\b\u0002\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\b0\u00052\u001a\b\u0002\u0010\t\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\n0\u00052\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000eH\u00c6\u0001\u00a2\u0006\u0002\u0010!J\u0013\u0010\"\u001a\u00020\u00032\b\u0010#\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010$\u001a\u00020%H\u00d6\u0001J\t\u0010&\u001a\u00020\u000eH\u00d6\u0001R\u001d\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00030\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R#\u0010\t\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\n0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0015\u0010\u000b\u001a\u0004\u0018\u00010\f\u00a2\u0006\n\n\u0002\u0010\u0015\u001a\u0004\b\u0013\u0010\u0014R\u0013\u0010\r\u001a\u0004\u0018\u00010\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0018R\u001d\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\b0\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0011\u00a8\u0006\'"}, d2 = {"Lcom/sensorscope/presentation/sensor/SensorUiState;", "", "isListening", "", "availableSensors", "", "Lcom/sensorscope/core/model/SensorType;", "latestValues", "Lcom/sensorscope/core/model/SensorData;", "chartSeries", "", "currentSessionId", "", "errorMessage", "", "(ZLjava/util/Map;Ljava/util/Map;Ljava/util/Map;Ljava/lang/Long;Ljava/lang/String;)V", "getAvailableSensors", "()Ljava/util/Map;", "getChartSeries", "getCurrentSessionId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getErrorMessage", "()Ljava/lang/String;", "()Z", "getLatestValues", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "(ZLjava/util/Map;Ljava/util/Map;Ljava/util/Map;Ljava/lang/Long;Ljava/lang/String;)Lcom/sensorscope/presentation/sensor/SensorUiState;", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class SensorUiState {
    private final boolean isListening = false;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<com.sensorscope.core.model.SensorType, java.lang.Boolean> availableSensors = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<com.sensorscope.core.model.SensorType, com.sensorscope.core.model.SensorData> latestValues = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<com.sensorscope.core.model.SensorType, java.util.List<com.sensorscope.core.model.SensorData>> chartSeries = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long currentSessionId = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String errorMessage = null;
    
    public SensorUiState(boolean isListening, @org.jetbrains.annotations.NotNull()
    java.util.Map<com.sensorscope.core.model.SensorType, java.lang.Boolean> availableSensors, @org.jetbrains.annotations.NotNull()
    java.util.Map<com.sensorscope.core.model.SensorType, com.sensorscope.core.model.SensorData> latestValues, @org.jetbrains.annotations.NotNull()
    java.util.Map<com.sensorscope.core.model.SensorType, ? extends java.util.List<com.sensorscope.core.model.SensorData>> chartSeries, @org.jetbrains.annotations.Nullable()
    java.lang.Long currentSessionId, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage) {
        super();
    }
    
    public final boolean isListening() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<com.sensorscope.core.model.SensorType, java.lang.Boolean> getAvailableSensors() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<com.sensorscope.core.model.SensorType, com.sensorscope.core.model.SensorData> getLatestValues() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<com.sensorscope.core.model.SensorType, java.util.List<com.sensorscope.core.model.SensorData>> getChartSeries() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getCurrentSessionId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getErrorMessage() {
        return null;
    }
    
    public SensorUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<com.sensorscope.core.model.SensorType, java.lang.Boolean> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<com.sensorscope.core.model.SensorType, com.sensorscope.core.model.SensorData> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<com.sensorscope.core.model.SensorType, java.util.List<com.sensorscope.core.model.SensorData>> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.sensorscope.presentation.sensor.SensorUiState copy(boolean isListening, @org.jetbrains.annotations.NotNull()
    java.util.Map<com.sensorscope.core.model.SensorType, java.lang.Boolean> availableSensors, @org.jetbrains.annotations.NotNull()
    java.util.Map<com.sensorscope.core.model.SensorType, com.sensorscope.core.model.SensorData> latestValues, @org.jetbrains.annotations.NotNull()
    java.util.Map<com.sensorscope.core.model.SensorType, ? extends java.util.List<com.sensorscope.core.model.SensorData>> chartSeries, @org.jetbrains.annotations.Nullable()
    java.lang.Long currentSessionId, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}