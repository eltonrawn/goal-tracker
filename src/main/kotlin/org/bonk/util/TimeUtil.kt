package org.bonk.util

import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object TimeUtil {
    fun secondsToHours(seconds: Int): Double {
        return seconds.toDouble() / 3600.0
    }

    fun getCurrentTimestamp(): Timestamp {
        return Timestamp.valueOf(LocalDateTime.now())
    }

//    fun hoursToSeconds(hours: Int): Int {
//        return hours * 3600
//    }
//
//    fun minutesToSeconds(minutes: Int): Int {
//        return minutes * 60
//    }

    fun toLocalDateFromTimestamp(timestamp: Timestamp): LocalDate {
        return timestamp.toLocalDateTime().toLocalDate()
    }

    fun getDaysBetweenInclusive(startDate: LocalDate, endDate: LocalDate): Long {
        return getDaysBetween(startDate, endDate) + 1
    }

    fun getDaysBetween(startDate: LocalDate, endDate: LocalDate): Long {
        return ChronoUnit.DAYS.between(startDate, endDate)
    }
}