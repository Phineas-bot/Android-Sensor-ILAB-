package com.sensorscope.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.sensorscope.data.local.entity.SensorReadingEntity;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0007J\u0016\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0004H\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u001c\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00030\r2\u0006\u0010\u0005\u001a\u00020\u0006H\'\u00a8\u0006\u000e"}, d2 = {"Lcom/sensorscope/data/local/dao/SensorReadingDao;", "", "getReadingsBySession", "", "Lcom/sensorscope/data/local/entity/SensorReadingEntity;", "sessionId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertReading", "", "reading", "(Lcom/sensorscope/data/local/entity/SensorReadingEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeReadingsBySession", "Lkotlinx/coroutines/flow/Flow;", "app_debug"})
@androidx.room.Dao()
public abstract interface SensorReadingDao {
    
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertReading(@org.jetbrains.annotations.NotNull()
    com.sensorscope.data.local.entity.SensorReadingEntity reading, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM sensor_readings WHERE sessionId = :sessionId ORDER BY recordedAtMillis ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReadingsBySession(long sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.sensorscope.data.local.entity.SensorReadingEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM sensor_readings WHERE sessionId = :sessionId ORDER BY recordedAtMillis DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.sensorscope.data.local.entity.SensorReadingEntity>> observeReadingsBySession(long sessionId);
}