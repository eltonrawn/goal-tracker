package org.bonk.org.bonk.model

import java.sql.Timestamp

data class Goalz(
    val id: Int,
    val description: String,
    val timeGoalSecond: Int,
    val dateCreated: Timestamp,
    val dateEnd: Timestamp,
)
