package com.lovelace_scd.timed.Adaptors

import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.lovelace_scd.timed.R
import com.lovelace_scd.timed.model.TestTimer
import com.lovelace_scd.timed.model.Timer
import com.lovelace_scd.timed.model.TimerData
import com.lovelace_scd.timed.test_code.Test
import java.lang.Exception
import java.util.*
import java.util.concurrent.CountDownLatch

const val DELAY_AMOUNT = 10 * 60 //10 min snooze

@RequiresApi(Build.VERSION_CODES.O)
class TimerAdaptor(val context: Context, val timers: TimerData) : RecyclerView.Adapter<TimerAdaptor.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.timer_list_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return timers.timerData.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindTimer(timers.timerData[position], context)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        lateinit var timer: Timer
        lateinit var countdown: CountDownTimer
        val medName: TextView? = itemView.findViewById(R.id.medName)
        var deleteMedBtn: ImageButton? = itemView.findViewById(R.id.delButton)
        var takeMedBtn: ImageButton? = itemView.findViewById(R.id.takeBtn)
        var skipDoseBtn: ImageButton? = itemView.findViewById(R.id.skipBtn)
        var delayDoseBtn: ImageButton? = itemView.findViewById(R.id.delayBtn)
        var refillBtn: Button? = itemView.findViewById(R.id.refillBtn)
        var medTimerText: TextView? = itemView.findViewById(R.id.medTimer)
        var refillsRemaining: TextInputEditText? = itemView.findViewById(R.id.refillsRemainingField)

        /*******************************************************************************************
         * bind the relevant UI elements to the current list item
         ******************************************************************************************/
        fun bindTimer(timer: Timer, context: Context){
            this.timer = timer
            itemView.setOnClickListener(this)
            countdown = DisplayCountdown(medTimerText, timer.calculateTimeRemaining(), 1000L).start()
            medName?.text = timer.medication.name
            deleteMedBtn?.setOnClickListener(this)
            takeMedBtn?.setOnClickListener(this)
            skipDoseBtn?.setOnClickListener(this)
            delayDoseBtn?.setOnClickListener(this)
            refillBtn?.setOnClickListener(this)
            refillsRemaining?.setText(timer.medication.numRefillsRemaining.toString())
        }

        /*******************************************************************************************
         * Override for the onClick method to direct the various UI elements that are clickable to
         * the appropriate function to implement their intended behaviors
         ******************************************************************************************/
        override fun onClick(view: View){
            var flag = true
            if (view == deleteMedBtn) {
                removeTimer(adapterPosition)
                countdown.cancel()
                flag = false
            } else if (view == takeMedBtn){
                countdown.cancel()
                countdown = takeDose(countdown, timer, medTimerText)
                countdown.start()

            } else if (view == skipDoseBtn){
                countdown.cancel()
                countdown = skipDose(countdown, timer, medTimerText)
                countdown.start()

            } else if (view == delayDoseBtn){
                countdown.cancel()
                countdown = delayDose(countdown, timer, medTimerText)
                countdown.start()

            } else if (view == refillBtn){
                refillMed(timer, refillsRemaining)
            } else {
                // generate a pop up message if the user clicks on the timer without touching any
                // of the buttons
                var daysToRefillNeeded: Double = timer.medication.amountRemaining /
                                                 timer.medication.doseSize *
                                                 timer.medication.daysPerTimePeriod.toDouble() /
                                                 timer.medication.dosesPerTimePeriod.toDouble()

                val toastDoseUnit = if (timer.medication.doseUnit == "N/A") "" else timer.medication.doseUnit + "s "
                Toast.makeText(context, "${timer.medication.name}: you have " +
                        "${timer.medication.amountRemaining} ${toastDoseUnit}left. " +
                        "You need a refill in ${daysToRefillNeeded.toInt()} days",
                        Toast.LENGTH_SHORT).show()
                flag = false
            }
            if(flag) {
                timers.updateTimers(context)
            }
        }
    }

    /*******************************************************************************************
     * Function to remove one of the timers from the dataset and update the UI to reflect its
     * removal
     ******************************************************************************************/
    fun removeTimer(position: Int){
        timers.deleteTimer(position, context)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, timers.timerData.size)
    }

    /*******************************************************************************************
     * Function to indicate that the user has taken a dose of the specified medication and
     * cause relevant data changes to the Medication and Timer objects
     ******************************************************************************************/
    fun takeDose(countdown: CountDownTimer, timer: Timer, view: TextView?)
            : CountDownTimer{
        try {
            timer.markTaken()
        } catch (e: Exception) {

            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
        return DisplayCountdown(view, timer.calculateTimeRemaining(), 1000L)
    }

    /*******************************************************************************************
     * Function to delay the indicated medication's timer by 10 minutes
     ******************************************************************************************/
    fun delayDose(countdown: CountDownTimer, timer: Timer, view: TextView?)
            : CountDownTimer{

        timer.adjustNextDoseTime(DELAY_AMOUNT*1000L)
        return DisplayCountdown(view, timer.calculateTimeRemaining(), 1000L)
    }

    /*******************************************************************************************
     * Function to skip the current dosage of the medication and adjust the timer to the next
     * scheduled time
     ******************************************************************************************/
    fun skipDose(countdown: CountDownTimer, timer: Timer, view: TextView?)
            : CountDownTimer{
        timer.skipNextDose()
        return DisplayCountdown(view, timer.calculateTimeRemaining(), 1000L)
    }

    /*******************************************************************************************
     * Function to indicate the user has refilled their prescription and update the Medication
     * data and UI to reflect the increased quantity of medication and reduced number of refills
     * remaining
     ******************************************************************************************/
    fun refillMed(timer: Timer, refillsRemaining: TextInputEditText?){
        var numRefills = refillsRemaining?.text.toString().toInt()
        try{
            timer.refillMed(numRefills)
            refillsRemaining?.setText(timer.medication.numRefillsRemaining.toString())
        } catch (e: Exception) {
            Toast.makeText(context, "${timer.medication.name} is ${e.message}",
                    Toast.LENGTH_SHORT).show()
        }
    }
}

/*******************************************************************************************
 * Countdown class to format the time until the next dose is due in DD:HH:MM:SS format
 ******************************************************************************************/
class DisplayCountdown (val view: TextView?, time: Long, msPerSec: Long) : CountDownTimer(time, msPerSec) {
    var seconds = 0L
    var mins = 0L
    var hours = 0L
    var days = 0L

    /*******************************************************************************************
     * Function to take the raw time in milliseconds and calculate the formatted time until the
     * dose needs to be taken
     *
     * Note: DateUtils.getRelativeTimeSpan might be an option to prevent issues with non 24hr days
     ******************************************************************************************/
    override fun onTick(millisUntilFinished: Long) {
        seconds = millisUntilFinished / 1000
        mins = seconds / 60
        seconds %= 60
        hours = mins / 60
        mins %= 60
        days = hours / 24
        hours %= 24

        var displayString = ""
        if(days > 0) {
            displayString = "%02d:%02d:%02d:%02d".format(days, hours, mins, seconds)
        } else if( hours > 0) {
            displayString = "%02d:%02d:%02d".format(hours, mins, seconds)
        } else if (mins > 0) {
            displayString = "%02d:%02d".format(mins, seconds)
        } else if (seconds > 0) {
            displayString = "%02d".format(seconds)
        } else {
            displayString = "Now!"
        }

        view?.text = displayString
    }

    override fun onFinish() {
        view?.text = "Now!"
    }
}