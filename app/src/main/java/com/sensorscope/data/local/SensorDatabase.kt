package com.sensorscope.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sensorscope.data.local.dao.SensorReadingDao
import com.sensorscope.data.local.dao.SensorSessionDao
import com.sensorscope.data.local.entity.SensorReadingEntity
import com.sensorscope.data.local.entity.SensorSessionEntity

@Database(
    entities = [SensorSessionEntity::class, SensorReadingEntity::class],
    version = 1,
    exportSchema = true
)
abstract class SensorDatabase : RoomDatabase() {
    abstract fun sensorSessionDao(): SensorSessionDao
    abstract fun sensorReadingDao(): SensorReadingDao
}
