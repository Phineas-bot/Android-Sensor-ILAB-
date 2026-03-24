package com.sensorscope.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sensor_readings",
    foreignKeys = [
        ForeignKey(
            entity = SensorSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["sessionId"]), Index(value = ["sensorType"])]
)
data class SensorReadingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: Long,
    val sensorType: String,
    val x: Float,
    val y: Float,
    val z: Float,
    val timestampNanos: Long,
    val recordedAtMillis: Long
)
