package org.bonk.model

import java.sql.Timestamp

data class LogEntry(
    val id: Int,
    val goalId: Int,
    val timeSpentSecond: Int,
    val dateCreated: Timestamp,
)
