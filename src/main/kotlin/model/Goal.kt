package org.bonk.model

import java.sql.Timestamp

data class Goal(
    val id: Int,
    val description: String,
    val timeGoalSecond: Int,
    val dateCreated: Timestamp,
    val dateEnd: Timestamp,
)
