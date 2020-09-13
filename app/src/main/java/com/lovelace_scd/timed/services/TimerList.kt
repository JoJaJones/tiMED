package com.lovelace_scd.timed.services

import android.os.Build
import androidx.annotation.RequiresApi
import com.lovelace_scd.timed.model.TimerData
import java.io.File

/**************************************************************************************************
 * Singleton list of timers
 *************************************************************************************************/
object TimerList {
    @RequiresApi(Build.VERSION_CODES.O)
    var data = TimerData()
}