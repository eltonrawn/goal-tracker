package org.bonk

import java.sql.DriverManager
import java.sql.Timestamp
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

        val completePercentage = (totalSecondsSpent.toDouble() / goal.timeGoalSecond.toDouble()) * 100.0

        println("***********Goal: ${goal.description}************")
        println("Goal is ${goal.timeGoalHour} hours")
        println("Total $totalHoursSpent hours spent")
        println("$completePercentage % complete")
        printProgressBar(completePercentage)
    }
}

fun printProgressBar(percentage: Double)  {
    val progress = ((percentage) / 10).toInt()
    for(i in 1 .. progress) {
        print(">")
    }
    for(i in progress .. 10) {
        print(" -")
    }
    println()
}

fun secondsToHours(seconds: Int): Double {
    return seconds.toDouble() / 3600.0
}

data class Goal(
    val id: Int,
    val description: String,
    val timeGoalSecond: Int,
    val dateCreated: Timestamp,
    val dateEnd: Timestamp,
    var timeGoalHour: Double = 0.0,
    var daysBetween: Long = 0,
    var hourNeededPerDay: Double = 0.0
) {
    init {
        timeGoalHour = secondsToHours(timeGoalSecond)
        daysBetween = ChronoUnit.DAYS.between(dateCreated.toLocalDateTime().toLocalDate(), dateEnd.toLocalDateTime().toLocalDate()) + 1
        hourNeededPerDay = (timeGoalSecond.toDouble() / daysBetween.toDouble()) / 3600.0
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

