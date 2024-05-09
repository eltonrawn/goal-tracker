package org.bonk

import java.sql.DriverManager
import java.sql.Timestamp
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun main() {
    /*
    * equivalent to
    * getGoals().forEach(::println)
    * */
    getGoals().forEach {println(it)}

    println()

    getLogs().forEach {println(it)}

    val logs = getLogs()


    getGoals().map {goal: Goal ->
        val totalSecondsSpent = logs.filter {
            it.goalId == goal.id
        }.sumOf {
            it.timeSpentSecond
        }
        val totalHoursSpent = secondsToHours(totalSecondsSpent)

        val secondsNeedToSpend = goal.timeGoalSecond - totalSecondsSpent
        val hoursNeedToSpend = secondsNeedToSpend.toDouble() / 3600

        val completePercentage = (totalSecondsSpent.toDouble() / goal.timeGoalSecond.toDouble()) * 100.0

        val daysLeft = getDaysBetween(LocalDate.now(), toLocalDateFromTimestamp(goal.dateEnd))
        val hoursNeedToSpendPerDay = (secondsNeedToSpend.toDouble() / (if(daysLeft == 0L) 1.0 else daysLeft.toDouble())) / 3600.0

        val daysPassed = goal.totalDays - daysLeft
        val hoursSpentPerDay = (totalSecondsSpent.toDouble() / (if(daysPassed == 0L) 1.0 else daysPassed.toDouble())) / 3600.0

//        val remainingHoursNeededPerDay = ()

        println("***********Goal: ${goal.description}************")
        println("Goal is to spend ${goal.timeGoalHour} hours in ${goal.totalDays} days (${goal.totalHourNeededPerDay} hours per day)")
//        println("$completePercentage % complete with $totalHoursSpent hours spent in ... days")
//        println("$daysLeft days left with $hoursNeedToSpend hours need to spend")
        println("Spent $totalHoursSpent hours in $daysPassed days ($completePercentage% complete with $hoursSpentPerDay hours per day)")
        println("Need to spend $hoursNeedToSpend hours in $daysLeft days ($hoursNeedToSpendPerDay hours per day)")
        printProgressBar(completePercentage)
    }
}

fun printProgressBar(percentage: Double)  {
    val progress = ((percentage) / 10).toInt()
    print("progress:")
    for(i in 1 .. progress) {
        print(" >")
    }
    for(i in progress .. 10) {
        print(" -")
    }
    println()
}

fun secondsToHours(seconds: Int): Double {
    return seconds.toDouble() / 3600.0
}

fun toLocalDateFromTimestamp(timestamp: Timestamp): LocalDate {
    return timestamp.toLocalDateTime().toLocalDate()
}

fun getDaysBetween(startDate: LocalDate, endDate: LocalDate): Long {
    return ChronoUnit.DAYS.between(startDate, endDate) + 1
}

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
        timeGoalHour = secondsToHours(timeGoalSecond)
        totalDays = getDaysBetween(toLocalDateFromTimestamp(dateCreated), toLocalDateFromTimestamp(dateEnd))
        totalHourNeededPerDay = (timeGoalSecond.toDouble() / totalDays.toDouble()) / 3600.0
    }
}

fun getGoals(): List<Goal> {
    val jdbcUrl = "jdbc:postgresql://localhost:5432/goalsdb"
    val connection = DriverManager
        .getConnection(jdbcUrl, "postgres", "postgres")

    //    println(connection.isValid(0))
    val query = connection.prepareStatement("SELECT * FROM goals")

    // the query is executed and results are fetched
    val result = query.executeQuery()

    val goals = mutableListOf<Goal>()
    while(result.next()){
        val id = result.getInt("id")
        val description = result.getString("description")
        val timeGoalSecond = result.getInt("time_goal_second")
        val dateCreated = result.getTimestamp("date_created")
        val dateEnd = result.getTimestamp("date_end")

        val goal = Goal(id, description, timeGoalSecond, dateCreated, dateEnd)
        goals.add(goal)
    }
    return goals
}

data class LogEntry(
    val id: Int,
    val goalId: Int,
    val timeSpentSecond: Int,
    val dateCreated: Timestamp,
    var timeSpentHour: Double = 0.0,
) {
    init {
        timeSpentHour = secondsToHours(timeSpentSecond)
    }
}


fun getLogs(): List<LogEntry> {
    val jdbcUrl = "jdbc:postgresql://localhost:5432/goalsdb"
    val connection = DriverManager
        .getConnection(jdbcUrl, "postgres", "postgres")

    //    println(connection.isValid(0))
    val query = connection.prepareStatement("SELECT * FROM log_entry")

    // the query is executed and results are fetched
    val result = query.executeQuery()

    val logEntries = mutableListOf<LogEntry>()
    while(result.next()){
        val id = result.getInt("id")
        val goalId = result.getInt("goal_id")
        val timeSpentSecond = result.getInt("time_spent_second")
        val dateCreated = result.getTimestamp("date_created")

        val logEntry = LogEntry(id, goalId, timeSpentSecond, dateCreated)
        logEntries.add(logEntry)
    }
    return logEntries
}

