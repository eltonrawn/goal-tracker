package org.bonk.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bonk.org.bonk.model.Goal
import java.time.LocalDateTime

fun Application.configureRouting() {
    routing {
        route("/goals") {
            get {
                val goal = Goal(
                    id = 1,
                    description = "yo",
                    timeGoalSecond = 10,
                    dateCreated = LocalDateTime.now(),
                    dateEnd = LocalDateTime.now()
                )
                call.respond(listOf(goal, goal))
            }
            post {
                val goal = call.receive<Goal>()
                call.respond(listOf(goal))
            }
        }
    }
}