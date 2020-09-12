/// filename: Timer.kt
/// description: CountDownTimer subclass which manages time to next dose of medication. We must
///     pass in an instance of Medication, and Timer provides interface for UI to interact with
///     when determining time to notify user of next dose, mark dose as taken and decrement
///     Medication.amountRemaining, reschedule next dose notification, etc.

package com.lovelace_scd.timed.model

import android.os.Build
import android.os.CountDownTimer
import androidx.annotation.RequiresApi
import com.lovelace_scd.timed.Model.Medication
import java.util.*


const val millisecondsPerDay = 24 * 60 * 60 * 1000;
const val millisecondsPerMinute = 60 * 1000;
const val millisecondsPerSecond = 1000;

class Timer {

    lateinit var countingTimer : CountDownTimer;
    var millisecondsToNextDose: Long = 0;
    var medication: Medication;
    var baseDate : Long;

    // Keep millisecondsNextDoseAdjusted separate from millisecondsToNextDose to allow
    //  user option to continue to adjust all future dose times. For example, if user selects from
    //  UI "Delay next dose 2 hours", when user eventually marks dose as taken, we can easily allow
    //  user to schedule all next doses for 2 hours later.
    var millisecondsNextDoseAdjusted: Long = 0;
    var skipNextDose: Boolean;
    var nextDoseReady: Boolean;

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(medication: Medication, date: Date, skipNextDose: Boolean = false, nextDoseReady: Boolean = false) {

        this.medication = medication;
//        this.baseDate = date;
        this.skipNextDose = skipNextDose;
        this.nextDoseReady = nextDoseReady;
        this.baseDate = date.toInstant().toEpochMilli();
        startTimer();
    }
    @RequiresApi(Build.VERSION_CODES.O)
    constructor(medication: Medication, baseDate: Long, skipNextDose: Boolean = false, nextDoseReady: Boolean = false) {

        this.medication = medication;
//        this.baseDate = date;
        this.skipNextDose = skipNextDose;
        this.nextDoseReady = nextDoseReady;
        this.baseDate = baseDate;
        startTimer();
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun startTimer() {

        var now = Date().toInstant().toEpochMilli();
        var timerAdjustment = (now - baseDate) % millisecondsPerDay;


        millisecondsToNextDose = (medication.dosesPerTimePeriod / medication.daysPerTimePeriod * millisecondsPerDay).toLong() - timerAdjustment;
        millisecondsNextDoseAdjusted = 0;

        countingTimer = CountingTimer(this, millisecondsToNextDose);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun restartTimer() {
        startTimer();
    }

    fun getSecondsToNextDose(): Long {
        return millisecondsToNextDose / millisecondsPerSecond;
    }

    fun skipNextDose() {
        skipNextDose = true;
    }

    fun markTaken() {
        if (!nextDoseReady) throw Error("Next dose not ready.");
        nextDoseReady = false;
        medication.takeMed();
    }

    fun adjustNextDoseTime(addTime: Long) {
        millisecondsNextDoseAdjusted += addTime;

    }

    fun notification() {
        print("Med timer: ${medication.name} ready...");
    }

    fun toMap(): Map<String, Any> {
        var map = medication.toMap();
        map["baseDate"] = baseDate;
        map["skipNextDose"] = skipNextDose;
        map["nextDoseReady"] = nextDoseReady;
        map["millisecondsToNextDose"] = millisecondsToNextDose;
        map["millisecondsNextDoseAdjusted"] = millisecondsNextDoseAdjusted;

        return map;
    }
    
}

class CountingTimer : CountDownTimer{
    val timer : Timer; // Reference so we can call notify, or something similar.

    constructor(timer: Timer, milliseconds: Long) :
        super(milliseconds, millisecondsPerSecond.toLong()) {
        this.timer = timer;

    }

    override fun onTick(p0: Long) {
        timer.millisecondsToNextDose = p0;
    }

    override fun onFinish() {
        if (!timer.skipNextDose) {
            timer.nextDoseReady = true;
            timer.notification();
        }

        timer.skipNextDose = false;
        timer.restartTimer();
    }
}