package org.bonk.service

import org.bonk.model.Goal
import org.bonk.util.PrintUtil
import org.bonk.util.TimeUtil
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

        goalService.getGoals().map {goal: Goal ->
            val totalSecondsSpent = logs.filter {
                it.goalId == goal.id
            }.sumOf {
                it.timeSpentSecond
            }
            val totalHoursSpent = TimeUtil.secondsToHours(totalSecondsSpent)

            val secondsNeedToSpend = goal.timeGoalSecond - totalSecondsSpent
            val hoursNeedToSpend = secondsNeedToSpend.toDouble() / 3600

            val completePercentage = (totalSecondsSpent.toDouble() / goal.timeGoalSecond.toDouble()) * 100.0

            val daysLeft = TimeUtil.getDaysBetween(LocalDate.now(), TimeUtil.toLocalDateFromTimestamp(goal.dateEnd))
            val hoursNeedToSpendPerDay = (secondsNeedToSpend.toDouble() / (if(daysLeft == 0L) 1.0 else daysLeft.toDouble())) / 3600.0

            val daysPassed = goal.totalDays - daysLeft
            val hoursSpentPerDay = (totalSecondsSpent.toDouble() / (if(daysPassed == 0L) 1.0 else daysPassed.toDouble())) / 3600.0

            println("***********Goal: ${goal.description}************")
            println("Goal is to spend ${goal.timeGoalHour} hours in ${goal.totalDays} days (${goal.totalHourNeededPerDay} hours per day)")
            println("Spent $totalHoursSpent hours in $daysPassed days ($completePercentage% complete with $hoursSpentPerDay hours per day)")
            println("Need to spend $hoursNeedToSpend hours in $daysLeft days ($hoursNeedToSpendPerDay hours per day)")
            PrintUtil.printProgressBar(completePercentage)
        }
    }
}