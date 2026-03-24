package com.sensorscope.domain.usecase;

import com.sensorscope.core.model.SensorData;
import com.sensorscope.core.model.SensorType;
import com.sensorscope.domain.repository.SensorRepository;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006J&\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00072\u0006\u0010\u000e\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u0013H\u0086@\u00a2\u0006\u0002\u0010\u0014J\u0016\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/sensorscope/domain/usecase/ManageSensorSessionUseCase;", "", "repository", "Lcom/sensorscope/domain/repository/SensorRepository;", "(Lcom/sensorscope/domain/repository/SensorRepository;)V", "availableSensors", "", "Lcom/sensorscope/core/model/SensorType;", "", "log", "", "sessionId", "", "type", "data", "Lcom/sensorscope/core/model/SensorData;", "(JLcom/sensorscope/core/model/SensorType;Lcom/sensorscope/core/model/SensorData;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startSession", "name", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "stopSession", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class ManageSensorSessionUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.sensorscope.domain.repository.SensorRepository repository = null;
    
    @javax.inject.Inject()
    public ManageSensorSessionUseCase(@org.jetbrains.annotations.NotNull()
    com.sensorscope.domain.repository.SensorRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object startSession(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object stopSession(long sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object log(long sessionId, @org.jetbrains.annotations.NotNull()
    com.sensorscope.core.model.SensorType type, @org.jetbrains.annotations.NotNull()
    com.sensorscope.core.model.SensorData data, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<com.sensorscope.core.model.SensorType, java.lang.Boolean> availableSensors() {
        return null;
    }
}