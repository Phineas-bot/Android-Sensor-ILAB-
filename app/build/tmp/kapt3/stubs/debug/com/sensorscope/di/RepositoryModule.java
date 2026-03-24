package com.sensorscope.di;

import android.content.Context;
import androidx.room.Room;
import com.sensorscope.data.local.SensorDatabase;
import com.sensorscope.data.local.dao.SensorReadingDao;
import com.sensorscope.data.local.dao.SensorSessionDao;
import com.sensorscope.data.repository.SensorRepositoryImpl;
import com.sensorscope.domain.repository.SensorRepository;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\'\u00a8\u0006\u0007"}, d2 = {"Lcom/sensorscope/di/RepositoryModule;", "", "()V", "bindSensorRepository", "Lcom/sensorscope/domain/repository/SensorRepository;", "impl", "Lcom/sensorscope/data/repository/SensorRepositoryImpl;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public abstract class RepositoryModule {
    
    public RepositoryModule() {
        super();
    }
    
    @dagger.Binds()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public abstract com.sensorscope.domain.repository.SensorRepository bindSensorRepository(@org.jetbrains.annotations.NotNull()
    com.sensorscope.data.repository.SensorRepositoryImpl impl);
}