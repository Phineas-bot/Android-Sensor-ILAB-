package com.sensorscope.data.repository;

import com.sensorscope.core.model.SensorData;
import com.sensorscope.core.model.SensorType;
import com.sensorscope.core.sensor.SensorManagerHelper;
import com.sensorscope.data.local.dao.SensorReadingDao;
import com.sensorscope.data.local.dao.SensorSessionDao;
import com.sensorscope.data.local.entity.SensorReadingEntity;
import com.sensorscope.data.local.entity.SensorSessionEntity;
import com.sensorscope.domain.model.SensorSessionSummary;
import com.sensorscope.domain.repository.LoggedReading;
import com.sensorscope.domain.repository.SensorRepository;
import javax.inject.Inject;
import javax.inject.Singleton;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.Flow;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0014\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f0\nH\u0016J\u001c\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0006\u0010\u0010\u001a\u00020\u0011H\u0096@\u00a2\u0006\u0002\u0010\u0012J&\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u000b2\u0006\u0010\u0016\u001a\u00020\u0017H\u0096@\u00a2\u0006\u0002\u0010\u0018J\u0016\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00170\u001a2\u0006\u0010\u001b\u001a\u00020\u000bH\u0016J\u0014\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001d0\u000e0\u001aH\u0016J\u0016\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020 H\u0096@\u00a2\u0006\u0002\u0010!J\u0016\u0010\"\u001a\u00020\u00142\u0006\u0010\u0010\u001a\u00020\u0011H\u0096@\u00a2\u0006\u0002\u0010\u0012R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/sensorscope/data/repository/SensorRepositoryImpl;", "Lcom/sensorscope/domain/repository/SensorRepository;", "sensorManagerHelper", "Lcom/sensorscope/core/sensor/SensorManagerHelper;", "sessionDao", "Lcom/sensorscope/data/local/dao/SensorSessionDao;", "readingDao", "Lcom/sensorscope/data/local/dao/SensorReadingDao;", "(Lcom/sensorscope/core/sensor/SensorManagerHelper;Lcom/sensorscope/data/local/dao/SensorSessionDao;Lcom/sensorscope/data/local/dao/SensorReadingDao;)V", "availableSensors", "", "Lcom/sensorscope/core/model/SensorType;", "", "getReadingsForSession", "", "Lcom/sensorscope/domain/repository/LoggedReading;", "sessionId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logReading", "", "sensorType", "data", "Lcom/sensorscope/core/model/SensorData;", "(JLcom/sensorscope/core/model/SensorType;Lcom/sensorscope/core/model/SensorData;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeSensor", "Lkotlinx/coroutines/flow/Flow;", "type", "observeSessions", "Lcom/sensorscope/domain/model/SensorSessionSummary;", "startSession", "name", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "stopSession", "app_debug"})
public final class SensorRepositoryImpl implements com.sensorscope.domain.repository.SensorRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.sensorscope.core.sensor.SensorManagerHelper sensorManagerHelper = null;
    @org.jetbrains.annotations.NotNull()
    private final com.sensorscope.data.local.dao.SensorSessionDao sessionDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.sensorscope.data.local.dao.SensorReadingDao readingDao = null;
    
    @javax.inject.Inject()
    public SensorRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.sensorscope.core.sensor.SensorManagerHelper sensorManagerHelper, @org.jetbrains.annotations.NotNull()
    com.sensorscope.data.local.dao.SensorSessionDao sessionDao, @org.jetbrains.annotations.NotNull()
    com.sensorscope.data.local.dao.SensorReadingDao readingDao) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.util.Map<com.sensorscope.core.model.SensorType, java.lang.Boolean> availableSensors() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.sensorscope.core.model.SensorData> observeSensor(@org.jetbrains.annotations.NotNull()
    com.sensorscope.core.model.SensorType type) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object startSession(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object stopSession(long sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object logReading(long sessionId, @org.jetbrains.annotations.NotNull()
    com.sensorscope.core.model.SensorType sensorType, @org.jetbrains.annotations.NotNull()
    com.sensorscope.core.model.SensorData data, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.sensorscope.domain.model.SensorSessionSummary>> observeSessions() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getReadingsForSession(long sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sensorscope.domain.repository.LoggedReading>> $completion) {
        return null;
    }
}