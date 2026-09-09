package com.example.eSewaMarket.utils

import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

suspend fun <T> minimumLoadingTime(
    minimumTime: Long = 1000L,
    operation: suspend () -> T
): T {

    val startTime = System.currentTimeMillis()

    val result = operation()

    val elapsedTime = System.currentTimeMillis() - startTime
    val remainingTime = minimumTime - elapsedTime

    if (remainingTime > 0) {
        delay(remainingTime.milliseconds)
    }

    return result
}