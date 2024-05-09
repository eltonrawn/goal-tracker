package org.bonk.model

import org.bonk.util.TimeUtil
import java.sql.Timestamp

data class LogEntry(
    val id: Int,
    val goalId: Int,
    val timeSpentSecond: Int,
    val dateCreated: Timestamp,
    var timeSpentHour: Double = 0.0,
) {
    init {
        timeSpentHour = TimeUtil.secondsToHours(timeSpentSecond)
    }
}
