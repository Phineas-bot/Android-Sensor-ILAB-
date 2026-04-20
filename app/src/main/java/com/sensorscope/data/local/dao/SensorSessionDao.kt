package com.sensorscope.data.local.dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import com.sensorscope.data.local.entity.SensorSessionEntity
import kotlinx.coroutines.flow.Flow

/** Flat projection returned by the sessions+count JOIN query. */
data class SessionWithCount(
    @Embedded val session: SensorSessionEntity,
    @ColumnInfo(name = "readingCount") val readingCount: Int
)

@Dao
interface SensorSessionDao {
    @Insert
    suspend fun insertSession(session: SensorSessionEntity): Long

    @Query("UPDATE sensor_sessions SET endedAtMillis = :endedAt WHERE id = :sessionId")
    suspend fun closeSession(sessionId: Long, endedAt: Long)

    @Query("""
        SELECT sensor_sessions.*, COUNT(sensor_readings.id) AS readingCount
        FROM sensor_sessions
        LEFT JOIN sensor_readings ON sensor_readings.sessionId = sensor_sessions.id
        GROUP BY sensor_sessions.id
        ORDER BY sensor_sessions.startedAtMillis DESC
    """)
    fun observeSessionsWithCount(): Flow<List<SessionWithCount>>
}
