package org.bonk.service

import org.bonk.model.LogEntry
import java.sql.DriverManager

interface LogEntryService {
    fun getLogs(): List<LogEntry>
    fun insertLog(logEntry: LogEntry)
}

class LogEntryServiceImpl: LogEntryService {
    override fun getLogs(): List<LogEntry> {
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

    override fun insertLog(logEntry: LogEntry) {
        val jdbcUrl = "jdbc:postgresql://localhost:5432/goalsdb"
        val connection = DriverManager
            .getConnection(jdbcUrl, "postgres", "postgres")

        val insertQuery = "insert into log_entry(goal_id, date_created, time_spent_second) values(?,?,?)"

        val preparedStatement = connection.prepareStatement(insertQuery)

        preparedStatement.setInt(1, logEntry.goalId)
        preparedStatement.setTimestamp(2, logEntry.dateCreated)
        preparedStatement.setInt(3, logEntry.timeSpentSecond)

        // the query is executed and results are fetched
        val result = preparedStatement.executeUpdate()
        println("rows affected $result")

    }
}