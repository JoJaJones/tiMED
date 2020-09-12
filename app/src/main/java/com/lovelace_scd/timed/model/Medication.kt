package com.lovelace_scd.timed.Model

import android.os.Build
import androidx.annotation.RequiresApi
import com.lovelace_scd.timed.model.Timer
import com.lovelace_scd.timed.model.TimerData
import java.util.*

open class Medication(var name: String, var rxFullSize: Double, var amountRemaining: Double,
                      var numRefillsRemaining: Int, var isRefillable: Boolean, var dosesPerTimePeriod: Int,
                      var daysPerTimePeriod: Int, var doseSize: Double, var takeWithFood: Boolean, var doseUnit: String) {

    @RequiresApi(Build.VERSION_CODES.O)
    val timerData: TimerData = TimerData()
    var calendar = Calendar.getInstance()

    fun addTimer(timer: Timer){
        timerData.addTimer(timer)
    }

    fun deleteTimer(timer: Timer){
        timerData.deleteTimer(timer)
    }

    fun getTimers(){
        timerData.getTimers()
    }

    fun takeMed(){
        if (amountRemaining == 0.0){ throw error("No medication remaining")}
        amountRemaining -= doseSize
        prevDoseTime()
    }

    fun refillMed(amount: Double){
        if (!isRefillable){throw error("Medication is not refillable")}
        if (numRefillsRemaining == 0){throw error("No refills remaining")}
        numRefillsRemaining - 1
        amountRemaining += amount
    }

    fun prevDoseTime(){
        calendar = Calendar.getInstance()
    }

    fun minutesBtwnDose(): Int {
        return ((daysPerTimePeriod * 24) / dosesPerTimePeriod) * 60
    }

    fun calcNextDostTime(): Date? {
        calendar.add(Calendar.MINUTE, minutesBtwnDose())
        return calendar.time
    }

    fun toMap() : MutableMap<String, Any> {
        var map = mutableMapOf<String, Any>()

        map["medicationName"] = name;
        map["rxFullSize"] = rxFullSize;
        map["amountRemaining"] = amountRemaining;
        map["numRefillsRemaining"] = numRefillsRemaining;
        map["isRefillable"] = isRefillable;
        map["dosesPerTimePeriod"] = dosesPerTimePeriod;
        map["daysPerTimePeriod"] = daysPerTimePeriod;
        map["doseSize"] = doseSize;
        map["takeWithFood"] = takeWithFood;
        map["doseUnit"] = doseUnit;

        return map;
    }
}