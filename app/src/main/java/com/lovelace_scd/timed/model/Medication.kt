package com.lovelace_scd.timed.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.lang.Exception
import java.util.*
import kotlin.math.ceil

@RequiresApi(Build.VERSION_CODES.O)
open class Medication(var name: String, var rxFullSize: Double, var amountRemaining: Double,
                      var numRefillsRemaining: Int, var dosesPerTimePeriod: Int, var daysPerTimePeriod: Int,
                      var doseSize: Double, var takeWithFood: Boolean, var doseUnit: String, var isRefillable: Boolean = true) {


    var calendar = Calendar.getInstance()

    fun takeMed(){
        if (amountRemaining == 0.0){ throw Exception("No medication remaining")}
        amountRemaining -= doseSize
        prevDoseTime()
    }

    fun isRefillReady(): Boolean {
        return amountRemaining <= ceil(rxFullSize / 4)
    }

    fun refillMed(amount: Double){

        if (isRefillReady()) {
            if (isRefillable) {
                if (numRefillsRemaining > 0) {
                    numRefillsRemaining -= 1
                    amountRemaining += amount
                } else {
                    throw Exception("out of refills")
                }
            } else {
                throw Exception("not refillable")
            }
        } else {
            throw Exception("not ready for refilling yet")
        }



    }

    fun prevDoseTime(){
        calendar = Calendar.getInstance()
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