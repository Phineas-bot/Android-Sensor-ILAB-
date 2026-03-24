package com.sensorscope.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.sensorscope.data.local.dao.SensorReadingDao;
import com.sensorscope.data.local.dao.SensorSessionDao;
import com.sensorscope.data.local.entity.SensorReadingEntity;
import com.sensorscope.data.local.entity.SensorSessionEntity;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\u0007"}, d2 = {"Lcom/sensorscope/data/local/SensorDatabase;", "Landroidx/room/RoomDatabase;", "()V", "sensorReadingDao", "Lcom/sensorscope/data/local/dao/SensorReadingDao;", "sensorSessionDao", "Lcom/sensorscope/data/local/dao/SensorSessionDao;", "app_debug"})
@androidx.room.Database(entities = {com.sensorscope.data.local.entity.SensorSessionEntity.class, com.sensorscope.data.local.entity.SensorReadingEntity.class}, version = 1, exportSchema = true)
public abstract class SensorDatabase extends androidx.room.RoomDatabase {
    
    public SensorDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.sensorscope.data.local.dao.SensorSessionDao sensorSessionDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.sensorscope.data.local.dao.SensorReadingDao sensorReadingDao();
}