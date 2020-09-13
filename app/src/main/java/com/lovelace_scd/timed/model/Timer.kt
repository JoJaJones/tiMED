/// filename: Timer.kt
/// description: CountDownTimer subclass which manages time to next dose of medication. We must
///     pass in an instance of Medication, and Timer provides interface for UI to interact with
///     when determining time to notify user of next dose, mark dose as taken and decrement
///     Medication.amountRemaining, reschedule next dose notification, etc.

package com.lovelace_scd.timed.model

import android.os.Build
import android.os.CountDownTimer
import androidx.annotation.RequiresApi
import com.lovelace_scd.timed.util.MS_PER_DAY
import com.lovelace_scd.timed.util.MS_PER_HOUR
import com.lovelace_scd.timed.util.MS_PER_SECOND
import java.lang.Exception
//import com.lovelace_scd.timed.model.Medication
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
class Timer {

    lateinit var countingTimer : CountDownTimer;
    var nextDoseDue: Long = 0;
    var medication: Medication;

    // Keep millisecondsNextDoseAdjusted separate from millisecondsToNextDose to allow
    //  user option to continue to adjust all future dose times. For example, if user selects from
    //  UI "Delay next dose 2 hours", when user eventually marks dose as taken, we can easily allow
    //  user to schedule all next doses for 2 hours later.
    var nextDoseDueAdjusted: Long = 0;
    var skipNextDose: Boolean;
    var nextDoseReady: Boolean;

    constructor(medication: Medication, date: Date, skipNextDose: Boolean = false, nextDoseReady: Boolean = true) {

        this.medication = medication;
//        this.baseDate = date;
        this.skipNextDose = skipNextDose;
        this.nextDoseReady = nextDoseReady;
        this.nextDoseDue = date.toInstant().toEpochMilli();
        this.nextDoseDueAdjusted = this.nextDoseDue

//        startTimer();
    }

    constructor(medication: Medication, doseDueTime: Long, skipNextDose: Boolean = false, nextDoseReady: Boolean = true) {

        this.medication = medication;
//        this.baseDate = date;
        this.skipNextDose = skipNextDose;
        this.nextDoseReady = nextDoseReady;
        this.nextDoseDue = doseDueTime;
        this.nextDoseDueAdjusted = this.nextDoseDue
//        startTimer();
    }

    fun startTimer() {

        var now = Date().toInstant().toEpochMilli();
        var timerAdjustment = (now - nextDoseDue) % MS_PER_DAY;


        nextDoseDue = ((medication.daysPerTimePeriod.toDouble() / medication.dosesPerTimePeriod.toDouble() ) * MS_PER_DAY).toLong() - timerAdjustment;
        nextDoseDueAdjusted = 0;

        countingTimer = CountingTimer(this, nextDoseDue);
    }

    fun calculateTimeRemaining(): Long {
        return nextDoseDueAdjusted - Date().toInstant().toEpochMilli()
    }

    fun restartTimer() {
        startTimer();
    }

    fun getSecondsToNextDose(): Long {
        return nextDoseDue / MS_PER_SECOND;
    }

    fun skipNextDose() {
        calculateNextDoseTime()
    }

    fun markTaken() {
        if(calculateTimeRemaining() < MS_PER_HOUR /4 && medication.amountRemaining > 0){
            nextDoseReady = true
            medication.takeMed();
            calculateNextDoseTime()

        } else {
            nextDoseReady = false
            var errorMsg = ""
            if (medication.amountRemaining > 0) {
                errorMsg = "It's too far from your scheduled time, please wait longer."
            } else {
                errorMsg = "You're out of ${medication.name}, refill it " +
                        "before trying to take more"
            }
            throw Exception(errorMsg)
        }

    }

    fun calculateNextDoseTime(){
        this.nextDoseDue += ((medication.daysPerTimePeriod.toDouble() /
                            medication.dosesPerTimePeriod.toDouble() ) *
                            MS_PER_DAY).toLong()
        if(this.nextDoseDue < Date().toInstant().toEpochMilli()) {
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


    fun notification() {
        print("Med timer: ${medication.name} ready...");
    }

    fun toMap(): Map<String, Any> {
        var map = medication.toMap();
        map["baseDate"] = nextDoseDue;
        map["skipNextDose"] = skipNextDose;
        map["nextDoseReady"] = nextDoseReady;
        map["millisecondsToNextDose"] = nextDoseDue;
        map["millisecondsNextDoseAdjusted"] = nextDoseDueAdjusted;

        return map;
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

class CountingTimer : CountDownTimer{
    val timer : Timer; // Reference so we can call notify, or something similar.

    constructor(timer: Timer, milliseconds: Long) :
        super(milliseconds, MS_PER_SECOND.toLong()) {
        this.timer = timer;

    }

    override fun onTick(p0: Long) {
        timer.nextDoseDue = p0;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onFinish() {
        if (!timer.skipNextDose) {
            timer.nextDoseReady = true;
            timer.notification();
        }

        timer.skipNextDose = false;
        timer.restartTimer();
    }
}