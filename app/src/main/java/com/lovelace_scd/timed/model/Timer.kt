/// filename: Timer.kt
/// description: CountDownTimer subclass which manages time to next dose of medidation. We must
///     pass in an instance of Medication, and Timer provides interface for UI to interact with
///     when determining time to notify user of next dose, mark dose as taken and decrement
///     Medication.amountRemaining, reschedule next dose notification, etc.

package com.lovelace_scd.timed.model

import android.os.CountDownTimer


const val millisecondsPerDay = 24 * 60 * 60 * 1000;
const val millisecondsPerMinute = 60 * 1000;
const val millisecondsPerSecond = 1000;

class Timer : CountDownTimer {


    private var millisecondsToNextDose: Long;
    private var medication : Medication;

    // Keep millisecondsNextDoseAdjusted separate from millisecondsToNextDose to allow
    //  user option to continue to adjust all future dose times. For example, if user selects from
    //  UI "Delay next dose 2 hours", when user eventually marks dose as taken, we can easily allow
    //  user to schedule all next doses for 2 hours later.
    private var millisecondsNextDoseAdjusted: Long;
    private var skipNextDose: Boolean = false;
    private var nextDoseReady: Boolean = false;

    constructor(medication: Medication) :
            super(medication.dosesPerTimePeriod / medication.daysPerTimePeriod * millisecondsPerDay,
            millisecondsPerSecond as Long) {

        this.medication = medication;
        millisecondsToNextDose = medication.dosesPerTimePeriod / medication.daysPerTimePeriod * millisecondsPerDay;
        millisecondsNextDoseAdjusted = 0;
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


    override fun onTick(p0: Long) {
        millisecondsToNextDose = p0;
    }

    override fun onFinish() {
        if (!skipNextDose) {
            nextDoseReady = true;
        }

        skipNextDose = false;
        this.start();

    }
}