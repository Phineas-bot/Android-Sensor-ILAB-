package com.sensorscope.di

import android.content.Context
import androidx.room.Room
import com.sensorscope.data.local.SensorDatabase
import com.sensorscope.data.local.dao.SensorReadingDao
import com.sensorscope.data.local.dao.SensorSessionDao
import com.sensorscope.data.repository.SensorRepositoryImpl
import com.sensorscope.domain.repository.SensorRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideSensorDatabase(@ApplicationContext context: Context): SensorDatabase {
        return Room.databaseBuilder(
            context,
            SensorDatabase::class.java,
            "sensorscope.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideSensorSessionDao(database: SensorDatabase): SensorSessionDao = database.sensorSessionDao()

    @Provides
    fun provideSensorReadingDao(database: SensorDatabase): SensorReadingDao = database.sensorReadingDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSensorRepository(impl: SensorRepositoryImpl): SensorRepository
}
