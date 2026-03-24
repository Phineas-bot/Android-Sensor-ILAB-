package com.sensorscope.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sensorscope.data.local.entity.SensorSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SensorSessionDao {
    @Insert
    suspend fun insertSession(session: SensorSessionEntity): Long

    @Query("UPDATE sensor_sessions SET endedAtMillis = :endedAt WHERE id = :sessionId")
    suspend fun closeSession(sessionId: Long, endedAt: Long)

    @Query("SELECT * FROM sensor_sessions ORDER BY startedAtMillis DESC")
    fun observeSessions(): Flow<List<SensorSessionEntity>>
}
