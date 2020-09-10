package com.lovelace_scd.timed

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
}

