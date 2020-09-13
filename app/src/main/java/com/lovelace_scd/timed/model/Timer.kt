/// filename: Timer.kt
/// description: CountDownTimer subclass which manages time to next dose of medication. We must
///     pass in an instance of Medication, and Timer provides interface for UI to interact with
///     when determining time to notify user of next dose, mark dose as taken and decrement
///     Medication.amountRemaining, reschedule next dose notification, etc.

package com.lovelace_scd.timed.model
import android.os.Build
import androidx.annotation.RequiresApi
import com.lovelace_scd.timed.util.MS_PER_DAY
import com.lovelace_scd.timed.util.MS_PER_HOUR
import java.lang.Exception
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
class Timer(var medication: Medication, doseDueTime: Long, private var skipNextDose: Boolean = false) {
    private var nextDoseDue: Long = doseDueTime

    // Keep millisecondsNextDoseAdjusted separate from millisecondsToNextDose to allow
    //  user option to continue to adjust all future dose times. For example, if user selects from
    //  UI "Delay next dose 2 hours", when user eventually marks dose as taken, we can easily allow
    //  user to schedule all next doses for 2 hours later.
    private var nextDoseDueAdjusted: Long = 0

    init {
        this.nextDoseDueAdjusted = this.nextDoseDue
    }

    fun calculateTimeRemaining(): Long {
        val timeRemaining = getNextDoseTimeRemaining()

        return if (timeRemaining > 0) timeRemaining else 0L
    }

    private fun getNextDoseTimeRemaining(): Long {
        return nextDoseDueAdjusted - Date().toInstant().toEpochMilli()
    }

    fun skipNextDose() {
        if(isDoseProximate()){
            calculateNextDoseTime()
        } else {
            val errorMsg = "It's too far from your scheduled time, please wait longer."

            throw Exception(errorMsg)
        }
    }

    private fun isDoseProximate() : Boolean {
        return getNextDoseTimeRemaining() < MS_PER_HOUR / 4
    }

    private fun isDoseReady() : Boolean {
        return isDoseProximate() && medication.amountRemaining > 0
    }

    @Throws(Exception::class)
    fun markTaken() = if(isDoseReady()){
        medication.takeMed()
        calculateNextDoseTime()
    } else {
        val errorMsg = if (medication.amountRemaining > 0) {
            "It's too far from your scheduled time, please wait longer."
        } else {
            "You're out of ${medication.name}, refill it " +
                    "before trying to take more"
        }
        throw Exception(errorMsg)
    }

    private fun calculateNextDoseTime(){
        this.nextDoseDue += ((medication.daysPerTimePeriod.toDouble() /
                            medication.dosesPerTimePeriod.toDouble() ) *
                            MS_PER_DAY).toLong()
        while(this.nextDoseDue < Date().toInstant().toEpochMilli()) {
            this.nextDoseDue = Date().toInstant().toEpochMilli() +
                               ((medication.daysPerTimePeriod.toDouble() /
                               medication.dosesPerTimePeriod.toDouble() ) *
                               MS_PER_DAY).toLong()
        }
        this.nextDoseDueAdjusted = nextDoseDue
    }

    fun adjustNextDoseTime(addTime: Long) {
        if(calculateTimeRemaining() < 0){
            nextDoseDueAdjusted = Date().toInstant().toEpochMilli() + addTime
        } else {
            nextDoseDueAdjusted += addTime
        }
    }

    fun toMap(): Map<String, Any> {
        val map = medication.toMap()
        map["baseDate"] = nextDoseDue
        map["skipNextDose"] = skipNextDose
        map["millisecondsToNextDose"] = nextDoseDue
        map["millisecondsNextDoseAdjusted"] = nextDoseDueAdjusted

        return map
    }

    fun refillMed(remainingRefills: Int){
        if(remainingRefills != medication.numRefillsRemaining
                && medication.isRefillable){
            medication.numRefillsRemaining = remainingRefills
        } else if (medication.isRefillable){
            medication.refillMed(medication.rxFullSize)
        }
    }

}