package org.bonk.org.bonk.model

import kotlinx.serialization.Serializable
import org.bonk.serializer.LocalDateTimeSerializer
import java.time.LocalDateTime

@Serializable
data class Goal(
    val id: Int,
    val description: String,
    val timeGoalSecond: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val dateCreated: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val dateEnd: LocalDateTime,
)
