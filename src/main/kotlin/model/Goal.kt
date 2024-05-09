package org.bonk.model

import org.bonk.util.TimeUtil
import java.sql.Timestamp

data class Goal(
    val id: Int,
    val description: String,
    val timeGoalSecond: Int,
    val dateCreated: Timestamp,
    val dateEnd: Timestamp,
    var timeGoalHour: Double = 0.0,
    var totalDays: Long = 0,
    var totalHourNeededPerDay: Double = 0.0
) {
    init {
        timeGoalHour = TimeUtil.secondsToHours(timeGoalSecond)
        totalDays = TimeUtil.getDaysBetween(TimeUtil.toLocalDateFromTimestamp(dateCreated), TimeUtil.toLocalDateFromTimestamp(dateEnd))
        totalHourNeededPerDay = (timeGoalSecond.toDouble() / totalDays.toDouble()) / 3600.0
    }
}
