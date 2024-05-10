package org.bonk

import org.bonk.service.*

fun main() {
    val goalService: GoalService = GoalServiceImpl()
    val logEntryService: LogEntryService = LogEntryServiceImpl()

    val analyticsService: AnalyticsService = AnalyticsServiceImpl(goalService, logEntryService)
    analyticsService.printAnalytics()
}





