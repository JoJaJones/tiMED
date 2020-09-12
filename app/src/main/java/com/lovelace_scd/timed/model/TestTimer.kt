package com.lovelace_scd.timed.model

import kotlin.random.Random

class TestTimer (val name: String, val refillsLeft: Int, val timeRemaining: List<String>){
    fun getRemainingRefills(): Int {
        return refillsLeft
    }

    fun getTime(): String {
        return timeRemaining[Random.nextInt(0,timeRemaining.size)]
    }
}