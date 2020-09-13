package com.lovelace_scd.timed.model

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.lovelace_scd.timed.io.MedJsonReader
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
class TimerData {
    var timerData : ArrayList<Timer>
    private val fileName = "timers.json"
    private val jsonReader = MedJsonReader()

    init {
        timerData = ArrayList()
    }

    fun getTimers() : ArrayList<Timer> {
        return timerData
    }

    fun loadTimers(context: Context){
        timerData = jsonReader.read(File(context.filesDir, fileName)) ?: ArrayList()
    }

    fun addTimer(timer: Timer, context: Context) {
        timerData.add(timer)
        jsonReader.write(timerData, File(context.filesDir, fileName))
    }

    fun updateTimers(context: Context) {
        jsonReader.write(timerData, File(context.filesDir, fileName))
    }

    fun deleteTimer(position: Int, context: Context) {
        timerData.removeAt(position)

        if(timerData.size == 0) {
            jsonReader.deleteFile(File(context.filesDir, fileName))

        }
         else {
            jsonReader.write(timerData, File(context.filesDir, fileName))
        }
    }
}