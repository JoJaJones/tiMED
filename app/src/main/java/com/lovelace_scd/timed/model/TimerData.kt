package com.lovelace_scd.timed.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Build
import androidx.annotation.RequiresApi
import com.lovelace_scd.timed.io.MedJsonReader
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
class TimerData {
    var timerData : ArrayList<Timer>;
//    val fileName = "sample.json";
        val fileName = "timers.json"
    private val jsonReader = MedJsonReader();

    constructor () {
        timerData = ArrayList()
    }

    fun getTimers() : ArrayList<Timer> {
        return timerData;
    }

    fun getTimer(position: Int): Timer? {
        return timerData.get(position)
    }

    fun loadTimers(context: Context){
        timerData = jsonReader.read(File(context.filesDir, fileName)) ?: ArrayList()
    }

    fun addTimer(timer: Timer, context: Context) {
        timerData.add(timer);
        jsonReader.write(timerData, File(context.filesDir, fileName));
    }

    fun updateTimers(context: Context) {
        jsonReader.write(timerData, File(context.filesDir, fileName))
    }

    fun deleteTimer(timer : Timer, context: Context) {
        timerData = timerData.filter() { it != timer } as ArrayList<Timer>;
        jsonReader.write(timerData, File(context.filesDir, fileName));
    }

    fun deleteTimer(position: Int, context: Context) {
        timerData.removeAt(position)
        jsonReader.write(timerData, File(context.filesDir, fileName));
    }
}