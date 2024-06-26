package org.bonk.service

import org.bonk.org.bonk.model.Goalz
import java.sql.DriverManager

interface GoalService {
    fun getGoals(): List<Goalz>
}

class GoalServiceImpl: GoalService {
    override fun getGoals(): List<Goalz> {
        val jdbcUrl = "jdbc:postgresql://localhost:5432/goalsdb"
        val connection = DriverManager
            .getConnection(jdbcUrl, "postgres", "postgres")

        //    println(connection.isValid(0))
        val query = connection.prepareStatement("SELECT * FROM goals")

        // the query is executed and results are fetched
        val result = query.executeQuery()

        val goals = mutableListOf<Goalz>()
        while(result.next()){
            val id = result.getInt("id")
            val description = result.getString("description")
            val timeGoalSecond = result.getInt("time_goal_second")
            val dateCreated = result.getTimestamp("date_created")
            val dateEnd = result.getTimestamp("date_end")

            val goal = Goalz(id, description, timeGoalSecond, dateCreated, dateEnd)
            goals.add(goal)
        }
        return goals
    }
}