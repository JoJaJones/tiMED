package com.lovelace_scd.timed.model

import com.lovelace_scd.timed.io.MedJsonReader

class TimerData {
    var timerData : ArrayList<Timer>?;
    val jsonReader = MedJsonReader();

    constructor () {
        timerData = jsonReader.read();
    }

    fun addTimer(timer: Timer) {
        timerData?.add(timer);
    }

    fun deleteTimer(timer : Timer) {
        timerData = timerData?.filter() { it != timer } as ArrayList<Timer>;
    }
}