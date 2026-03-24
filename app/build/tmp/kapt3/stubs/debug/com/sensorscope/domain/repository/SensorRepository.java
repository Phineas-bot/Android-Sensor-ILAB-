package com.sensorscope.domain.repository;

import com.sensorscope.core.model.SensorData;
import com.sensorscope.core.model.SensorType;
import com.sensorscope.domain.model.SensorSessionSummary;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003H&J\u001c\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\nH\u00a6@\u00a2\u0006\u0002\u0010\u000bJ&\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0010H\u00a6@\u00a2\u0006\u0002\u0010\u0011J\u0016\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00100\u00132\u0006\u0010\u0014\u001a\u00020\u0004H&J\u0014\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u00070\u0013H&J\u0016\u0010\u0017\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u0019H\u00a6@\u00a2\u0006\u0002\u0010\u001aJ\u0016\u0010\u001b\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\nH\u00a6@\u00a2\u0006\u0002\u0010\u000b\u00a8\u0006\u001c"}, d2 = {"Lcom/sensorscope/domain/repository/SensorRepository;", "", "availableSensors", "", "Lcom/sensorscope/core/model/SensorType;", "", "getReadingsForSession", "", "Lcom/sensorscope/domain/repository/LoggedReading;", "sessionId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logReading", "", "sensorType", "data", "Lcom/sensorscope/core/model/SensorData;", "(JLcom/sensorscope/core/model/SensorType;Lcom/sensorscope/core/model/SensorData;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeSensor", "Lkotlinx/coroutines/flow/Flow;", "type", "observeSessions", "Lcom/sensorscope/domain/model/SensorSessionSummary;", "startSession", "name", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "stopSession", "app_debug"})
public abstract interface SensorRepository {
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.Map<com.sensorscope.core.model.SensorType, java.lang.Boolean> availableSensors();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.sensorscope.core.model.SensorData> observeSensor(@org.jetbrains.annotations.NotNull()
    com.sensorscope.core.model.SensorType type);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object startSession(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object stopSession(long sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object logReading(long sessionId, @org.jetbrains.annotations.NotNull()
    com.sensorscope.core.model.SensorType sensorType, @org.jetbrains.annotations.NotNull()
    com.sensorscope.core.model.SensorData data, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.sensorscope.domain.model.SensorSessionSummary>> observeSessions();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReadingsForSession(long sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sensorscope.domain.repository.LoggedReading>> $completion);
}