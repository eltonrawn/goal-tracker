package org.bonk.util

import java.sql.Timestamp
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object TimeUtil {
    fun secondsToHours(seconds: Int): Double {
        return seconds.toDouble() / 3600.0
    }

    fun toLocalDateFromTimestamp(timestamp: Timestamp): LocalDate {
        return timestamp.toLocalDateTime().toLocalDate()
    }

    fun getDaysBetweenInclusive(startDate: LocalDate, endDate: LocalDate): Long {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1
    }
}