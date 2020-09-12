package com.lovelace_scd.timed.service

import com.lovelace_scd.timed.model.TestTimer

object TestTimerObjects {
    val testTimers = mutableListOf(
            TestTimer("Aspirin", 5, listOf("4:00:00", "1:23:45", "2:02:02")),
            TestTimer("Zyrtec", 2, listOf("24:00:00", "12:00:00", "5:00")),
            TestTimer("Penicillin", 0, listOf("12:00:00", "8:00:00", "30")),
            TestTimer("Adderall", 0, listOf("11:00:00", "7:00:00", "31")),
            TestTimer("Prozac", 5, listOf("4:00:00", "1:23:45", "2:02:02")),
            TestTimer("Zoloft", 2, listOf("24:00:00", "12:00:00", "5:00")),
            TestTimer("Xanax", 0, listOf("12:00:00", "8:00:00", "30")),
            TestTimer("Vitamin", 0, listOf("11:00:00", "7:00:00", "31")),
    )
}