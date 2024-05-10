package org.bonk.util

object PrintUtil {
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
}