package com.sensorscope.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sensorscope.data.local.entity.SensorReadingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SensorReadingDao {
    @Insert
    suspend fun insertReading(reading: SensorReadingEntity)

    @Query("SELECT * FROM sensor_readings WHERE sessionId = :sessionId ORDER BY recordedAtMillis ASC")
    suspend fun getReadingsBySession(sessionId: Long): List<SensorReadingEntity>

    @Query("SELECT * FROM sensor_readings WHERE sessionId = :sessionId ORDER BY recordedAtMillis DESC")
    fun observeReadingsBySession(sessionId: Long): Flow<List<SensorReadingEntity>>
}
