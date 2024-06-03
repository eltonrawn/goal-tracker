package org.bonk

import io.ktor.server.application.*
import org.bonk.plugins.configureRouting
import org.bonk.plugins.configureSerialization

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
//    val goalService: GoalService = GoalServiceImpl()
//    val logEntryService: LogEntryService = LogEntryServiceImpl()
//
//    val analyticsService: AnalyticsService = AnalyticsServiceImpl(goalService, logEntryService)
//
//    val showIntroductionText = """
//        Choose one
//        1. Show analytics
//        2. insert log
//    """.trimIndent()
//
//    while(true) {
//        try {
//            println(showIntroductionText)
//            val input = readln()
//            if(input.toInt() == 1) {
//                analyticsService.printAnalytics()
//            }
//            else if(input.toInt() == 2) {
//                println("Choose one goal")
//                val goals = goalService.getGoals().sortedBy {it.id}
//                goals.forEach {
//                    println("${it.id}: ${it.description}")
//                }
//                val goalInp = readln()
//                val goalId = goalInp.toInt()
//                if(goals.filter {it.id == goalId}.size != 1) {
//                    throw Exception("Please select correct goal")
//                }
//
//                println("Enter time")
//                val timeInp = readln()
//
//                val duration = Duration.parse("PT$timeInp".uppercase().replace(" ", ""))
//                println("duration: ${duration.toSeconds()}")
//
//                logEntryService.insertLog(
//                    LogEntry(
//                        id = 0,
//                        goalId = goalId,
//                        timeSpentSecond = duration.toSeconds().toInt(),
//                        dateCreated = TimeUtil.getCurrentTimestamp()
//                    )
//                )
//            }
//            else {
//                throw Exception("Please input only from the options")
//            }
//        } catch (e: Exception) {
//            println("Something went wrong, Select again. Error: $e")
//        }
//    }

}


fun Application.module() {
    configureRouting()
    configureSerialization()
}




