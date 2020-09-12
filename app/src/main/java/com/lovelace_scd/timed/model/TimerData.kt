package com.lovelace_scd.timed.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.lovelace_scd.timed.io.MedJsonReader

class TimerData {
    var timerData : ArrayList<Timer>?;
    private val jsonReader = MedJsonReader();

    @RequiresApi(Build.VERSION_CODES.O)
    constructor () {
        timerData = jsonReader.read();
    }

    fun getTimers() : ArrayList<Timer>? {
        return timerData;
    }

    fun addTimer(timer: Timer) {
        timerData?.add(timer);
        jsonReader.write(timerData);
    }

    fun deleteTimer(timer : Timer) {
        timerData = timerData?.filter() { it != timer } as ArrayList<Timer>;
        jsonReader.write(timerData);
    }
}