package com.lovelace_scd.timed.Model

open class Medication(var name: String, var rxFullSize: Double, var amountRemaining: Double,
                      var numRefillsRemaining: Int, var isRefillable: Boolean, var dosesPerTimePeriod: Int,
                      var daysPerTimePeriod: Int, var doseSize: Double, var takeWithFood: Boolean, var doseUnit: String ) {


    fun takeMed(){
        amountRemaining -= doseSize
    }

    fun refillMed(amount: Double): Boolean{
        if (!isRefillable || numRefillsRemaining == 0){
            return false
        }
        amountRemaining += amount
        return true
    }

    fun calcNextDostTime(prevDoseTime: Double): Double {
        return prevDoseTime + (daysPerTimePeriod * 24) / dosesPerTimePeriod
    }

    fun toMap() : MutableMap<String, Any> {
        var map = mutableMapOf<String, Any>();

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