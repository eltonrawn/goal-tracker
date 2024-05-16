package org.bonk.model

import java.time.LocalDate

data class Analytics(
    val timeGoalHour: Double,
    val totalDays: Long,
    val totalHourNeededPerDay: Double,
    val totalSecondsSpent: Int,
    val totalHoursSpent: Double,
    val completePercentage: Double,
    val daysLeft: Long,
    val daysPassed: Long,
    val hoursSpentPerDay: Double,
    val secondsNeedToSpend: Int,
    val hoursNeedToSpend: Double,
    val hoursNeedToSpendPerDay: Double,
    val hoursSpendEachDay: List<Pair<LocalDate, Int>>
)
