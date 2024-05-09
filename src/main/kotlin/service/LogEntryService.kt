package org.bonk.service

import org.bonk.model.LogEntry
import java.sql.DriverManager

interface LogEntryService {
    fun getLogs(): List<LogEntry>
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
}