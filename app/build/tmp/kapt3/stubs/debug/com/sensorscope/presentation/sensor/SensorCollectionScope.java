package com.sensorscope.presentation.sensor;

import com.sensorscope.core.model.SensorType;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0003\u0006\u0007\bB\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002J\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u0082\u0001\u0003\t\n\u000b\u00a8\u0006\f"}, d2 = {"Lcom/sensorscope/presentation/sensor/SensorCollectionScope;", "", "()V", "requiredSensors", "", "Lcom/sensorscope/core/model/SensorType;", "Dashboard", "Detail", "Labs", "Lcom/sensorscope/presentation/sensor/SensorCollectionScope$Dashboard;", "Lcom/sensorscope/presentation/sensor/SensorCollectionScope$Detail;", "Lcom/sensorscope/presentation/sensor/SensorCollectionScope$Labs;", "app_debug"})
public abstract class SensorCollectionScope {
    
    private SensorCollectionScope() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<com.sensorscope.core.model.SensorType> requiredSensors() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\n\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00d6\u0003J\t\u0010\u0007\u001a\u00020\bH\u00d6\u0001J\t\u0010\t\u001a\u00020\nH\u00d6\u0001\u00a8\u0006\u000b"}, d2 = {"Lcom/sensorscope/presentation/sensor/SensorCollectionScope$Dashboard;", "Lcom/sensorscope/presentation/sensor/SensorCollectionScope;", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class Dashboard extends com.sensorscope.presentation.sensor.SensorCollectionScope {
        @org.jetbrains.annotations.NotNull()
        public static final com.sensorscope.presentation.sensor.SensorCollectionScope.Dashboard INSTANCE = null;
        
        private Dashboard() {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/sensorscope/presentation/sensor/SensorCollectionScope$Detail;", "Lcom/sensorscope/presentation/sensor/SensorCollectionScope;", "sensorType", "Lcom/sensorscope/core/model/SensorType;", "(Lcom/sensorscope/core/model/SensorType;)V", "getSensorType", "()Lcom/sensorscope/core/model/SensorType;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class Detail extends com.sensorscope.presentation.sensor.SensorCollectionScope {
        @org.jetbrains.annotations.NotNull()
        private final com.sensorscope.core.model.SensorType sensorType = null;
        
        public Detail(@org.jetbrains.annotations.NotNull()
        com.sensorscope.core.model.SensorType sensorType) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sensorscope.core.model.SensorType getSensorType() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sensorscope.core.model.SensorType component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.sensorscope.presentation.sensor.SensorCollectionScope.Detail copy(@org.jetbrains.annotations.NotNull()
        com.sensorscope.core.model.SensorType sensorType) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\n\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00d6\u0003J\t\u0010\u0007\u001a\u00020\bH\u00d6\u0001J\t\u0010\t\u001a\u00020\nH\u00d6\u0001\u00a8\u0006\u000b"}, d2 = {"Lcom/sensorscope/presentation/sensor/SensorCollectionScope$Labs;", "Lcom/sensorscope/presentation/sensor/SensorCollectionScope;", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class Labs extends com.sensorscope.presentation.sensor.SensorCollectionScope {
        @org.jetbrains.annotations.NotNull()
        public static final com.sensorscope.presentation.sensor.SensorCollectionScope.Labs INSTANCE = null;
        
        private Labs() {
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
}