package com.lovelace_scd.timed.model

class Medication(var name: String, var rxFullSize: Double, var amountRemaining: Double,
                      var numRefillsRemaining: Int, var isRefillable: Boolean, var dosesPerTimePeriod: Int,
                      var daysPerTimePeriod: Int, var doseSize: Double, var takeWithFood: Boolean, var doseUnit: String ) {

    private var footTaken : Boolean = false

    fun foodTaken(){
        footTaken = true
    }

    fun takeMed(){
        if(amountRemaining == 0.0){
            throw error("You have no medication remaining")
        }
        else if (takeWithFood && !footTaken){
            throw error("You need to take food in before you take this medication")
        }
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