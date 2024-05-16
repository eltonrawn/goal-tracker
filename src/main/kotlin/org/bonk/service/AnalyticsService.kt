package org.bonk.service

import org.bonk.model.Analytics
import org.bonk.org.bonk.model.Goal
import org.bonk.model.LogEntry
import org.bonk.util.PrintUtil
import org.bonk.util.TimeUtil
import java.sql.Timestamp
import java.time.LocalDate

interface AnalyticsService {
    fun printAnalytics()
}

class AnalyticsServiceImpl(
    val goalService: GoalService,
    val logEntryService: LogEntryService,
): AnalyticsService {
    override fun printAnalytics() {
        /*
        * getGoals().forEach(::println)
        * goalService.getGoals().forEach {println(it)}
        * println()
        * LogEntryService.getLogs().forEach {println(it)}
        * */
        val logs = logEntryService.getLogs()
        val goals = goalService.getGoals()
        var totalHourNeededPerDay = 0.0

        goals.map {goal: Goal ->
            val analytics = calculateAnalyticsForGoal(goal, logs)

            totalHourNeededPerDay += analytics.totalHourNeededPerDay

            println()
            println("***********Goal: ${goal.description}************")
            println("Goal is to spend ${analytics.timeGoalHour} hours in ${analytics.totalDays} days (${analytics.totalHourNeededPerDay} hours per day)")
            println("Spent ${analytics.totalHoursSpent} hours in ${analytics.daysPassed} days (${analytics.completePercentage}% complete with ${analytics.hoursSpentPerDay} hours per day)")
            println("Need to spend ${analytics.hoursNeedToSpend} hours in ${analytics.daysLeft} days (${analytics.hoursNeedToSpendPerDay} hours per day)")
            PrintUtil.printProgressBar(analytics.completePercentage)
            analytics.hoursSpendEachDay.forEach {
                println("${it.first} : ${TimeUtil.secondsToHours(it.second)} hours")
            }
        }

        println("*********** Total ************")

        val totalHoursSpentEachDay = getSecondsSpentEachDay(logs + populateLogsWithEmptyHour(goals.minOf {it.dateCreated}))
        totalHoursSpentEachDay.forEach {
            println("${it.first} : ${TimeUtil.secondsToHours(it.second)} hours")
        }

        val totalTimeGoalHour = goals.map { it.timeGoalSecond }.fold(0.0) {acc, next -> acc + next} / 3600.0
        println()
        println("Goal is to spend total $totalTimeGoalHour hours ($totalHourNeededPerDay hours per day)")
        println("Total hours spent ${TimeUtil.secondsToHours(totalHoursSpentEachDay.sumOf {it.second})} hours")
    }

    private fun calculateAnalyticsForGoal(goal: Goal, logs: List<LogEntry>): Analytics {
        val logsForGoal = logs.filter {
            it.goalId == goal.id
        }

        return calculateAnalytics(
            logsForGoal, //+ populateLogsWithEmptyHour(goal.dateCreated),
            goal.timeGoalSecond,
            goal.dateCreated,
            goal.dateEnd
        )
    }

    private fun calculateAnalytics(
        logs: List<LogEntry>,
        timeGoalSecond: Int,
        dateCreated: Timestamp,
        dateEnd: Timestamp
    ): Analytics {
        val totalDays = TimeUtil.getDaysBetweenInclusive(TimeUtil.toLocalDateFromTimestamp(dateCreated), TimeUtil.toLocalDateFromTimestamp(dateEnd))
        val daysLeft = TimeUtil.getDaysBetween(LocalDate.now(), TimeUtil.toLocalDateFromTimestamp(dateEnd))
        val daysPassed = totalDays - daysLeft

        // analytics for total goal
        val timeGoalHour = TimeUtil.secondsToHours(timeGoalSecond)
        val totalHourNeededPerDay = (timeGoalSecond.toDouble() / totalDays.toDouble()) / 3600.0

        // analytics for time spent
        val totalSecondsSpent = logs.sumOf {
            it.timeSpentSecond
        }
        val completePercentage = (totalSecondsSpent.toDouble() / timeGoalSecond.toDouble()) * 100.0
        val totalHoursSpent = TimeUtil.secondsToHours(totalSecondsSpent)
        val hoursSpentPerDay = (totalSecondsSpent.toDouble() / (if(daysPassed == 0L) 1.0 else daysPassed.toDouble())) / 3600.0

        // analytics for time left
        val secondsNeedToSpend = timeGoalSecond - totalSecondsSpent
        val hoursNeedToSpend = secondsNeedToSpend.toDouble() / 3600
        val hoursNeedToSpendPerDay = (secondsNeedToSpend.toDouble() / (if(daysLeft == 0L) 1.0 else daysLeft.toDouble())) / 3600.0


        val analytics = Analytics(
            timeGoalHour,
            totalDays,
            totalHourNeededPerDay,
            totalSecondsSpent,
            totalHoursSpent,
            completePercentage,
            daysLeft,
            daysPassed,
            hoursSpentPerDay,
            secondsNeedToSpend,
            hoursNeedToSpend,
            hoursNeedToSpendPerDay,
            getSecondsSpentEachDay(logs)
        )

        return analytics
    }

    private fun populateLogsWithEmptyHour(dateCreated: Timestamp): List<LogEntry> {
        var date = dateCreated.toLocalDateTime().toLocalDate()
        val logs = mutableListOf<LogEntry>()
        val today = LocalDate.now()
        while(date <= today) {
            logs.add(
                LogEntry(
                    id = -1,
                    goalId = -1,
                    timeSpentSecond = 0,
                    dateCreated = Timestamp.valueOf(date.atStartOfDay())
                )
            )
            date = date.plusDays(1)
        }
        return logs
    }

    private fun getSecondsSpentEachDay(logs: List<LogEntry>): List<Pair<LocalDate, Int>> {
        return logs
            .sortedBy { it.dateCreated }
            .groupBy { it.dateCreated.toLocalDateTime().toLocalDate() }
            .mapValues { it.value.sumOf { it.timeSpentSecond } }
            .toList()
    }
}