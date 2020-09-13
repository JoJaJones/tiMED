package com.lovelace_scd.timed.adaptors

import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.lovelace_scd.timed.R
import com.lovelace_scd.timed.model.Timer
import com.lovelace_scd.timed.model.TimerData
import com.lovelace_scd.timed.util.CHANNEL_ID
import com.lovelace_scd.timed.util.DELAY_AMOUNT
import com.lovelace_scd.timed.util.MS_PER_30_MIN
import java.lang.Exception

@RequiresApi(Build.VERSION_CODES.O)
class TimerAdaptor(val context: Context, val timers: TimerData) : RecyclerView.Adapter<TimerAdaptor.Holder>() {
    var notificationNumber = 0
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
        private lateinit var timer: Timer
        private lateinit var countdown: CountDownTimer
        private var foodCountdown: CountDownTimer? = null
        private val medName: TextView? = itemView.findViewById(R.id.medName)
        private var deleteMedBtn: ImageButton? = itemView.findViewById(R.id.delButton)
        private var takeMedBtn: ImageButton? = itemView.findViewById(R.id.takeBtn)
        private var skipDoseBtn: ImageButton? = itemView.findViewById(R.id.skipBtn)
        private var delayDoseBtn: ImageButton? = itemView.findViewById(R.id.delayBtn)
        private var refillBtn: Button? = itemView.findViewById(R.id.refillBtn)
        private var medTimerText: TextView? = itemView.findViewById(R.id.medTimer)
        private var refillsRemaining: TextInputEditText? = itemView.findViewById(R.id.refillsRemainingField)

        /*******************************************************************************************
         * bind the relevant UI elements to the current list item
         ******************************************************************************************/
        fun bindTimer(timer: Timer, context: Context){
            this.timer = timer
            itemView.setOnClickListener(this)
            countdown = DisplayCountdown(
                    medTimerText, timer, context, notificationNumber++,
                    timer.calculateTimeRemaining(), 1000L, ).start()
            medName?.text = timer.medication.name
            deleteMedBtn?.setOnClickListener(this)
            takeMedBtn?.setOnClickListener(this)
            skipDoseBtn?.setOnClickListener(this)
            delayDoseBtn?.setOnClickListener(this)
            refillBtn?.setOnClickListener(this)
            refillsRemaining?.setText(timer.medication.numRefillsRemaining.toString())

            if(timer.medication.takeWithFood){
                foodCountdown = FoodCountdown(timer, context, notificationNumber++, timer.calculateTimeRemaining()- MS_PER_30_MIN).start()
            }
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

                if(timer.medication.takeWithFood){
                    foodCountdown?.cancel()
                }

                flag = false
            }
            else if (view == takeMedBtn) {
                countdown.cancel()
                countdown = takeDose(timer, medTimerText)
                countdown.start()

                if(timer.medication.takeWithFood){
                    foodCountdown?.cancel()
                    foodCountdown = takeDose(timer, medTimerText, true)
                    foodCountdown?.start()
                }
            }
            else if (view == skipDoseBtn) {
                countdown.cancel()
                countdown = skipDose(timer, medTimerText)
                countdown.start()

                if(timer.medication.takeWithFood){
                    foodCountdown?.cancel()
                    foodCountdown = takeDose(timer, medTimerText, true)
                    foodCountdown?.start()
                }
            }
            else if (view == delayDoseBtn) {
                countdown.cancel()
                countdown = delayDose(timer, medTimerText)
                countdown.start()

                if(timer.medication.takeWithFood){
                    foodCountdown?.cancel()
                    foodCountdown = takeDose(timer, medTimerText, true)
                    foodCountdown?.start()
                }
            }
            else if (view == refillBtn) {
                refillMed(timer, refillsRemaining)
            } else {
                // generate a pop up message if the user clicks on the timer without touching any
                // of the buttons
                val daysToRefillNeeded: Double = timer.medication.amountRemaining /
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
    fun takeDose(timer: Timer, view: TextView?, foodTimer: Boolean = false)
            : CountDownTimer{
        try {
            timer.markTaken()
        } catch (e: Exception) {

            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }

        return if(foodTimer) {
            FoodCountdown(timer, context, notificationNumber++,
                    timer.calculateTimeRemaining() - (MS_PER_30_MIN))
        } else {
            DisplayCountdown(view, timer, context, notificationNumber++, timer.calculateTimeRemaining(), 1000L)
        }
    }

    /*******************************************************************************************
     * Function to delay the indicated medication's timer by 10 minutes
     ******************************************************************************************/
    fun delayDose(timer: Timer, view: TextView?, foodTimer: Boolean = false)
            : CountDownTimer{


        timer.adjustNextDoseTime(DELAY_AMOUNT)
        return if(foodTimer) {
            FoodCountdown(timer, context, notificationNumber++,
                    timer.calculateTimeRemaining() - (MS_PER_30_MIN))
        } else {
            DisplayCountdown(view, timer, context, notificationNumber++, timer.calculateTimeRemaining(), 1000L)
        }
    }

    /*******************************************************************************************
     * Function to skip the current dosage of the medication and adjust the timer to the next
     * scheduled time
     ******************************************************************************************/
    fun skipDose(timer: Timer, view: TextView?, foodTimer: Boolean = false)
            : CountDownTimer{
        try {
            timer.skipNextDose()
        } catch (e: Exception) {

            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
        return if(foodTimer) {
            FoodCountdown(timer, context, notificationNumber++, 
                    timer.calculateTimeRemaining() - (MS_PER_30_MIN))
        } else {
            DisplayCountdown(view, timer, context, notificationNumber++, timer.calculateTimeRemaining(), 1000L)
        }
    }

    /*******************************************************************************************
     * Function to indicate the user has refilled their prescription and update the Medication
     * data and UI to reflect the increased quantity of medication and reduced number of refills
     * remaining
     ******************************************************************************************/
    fun refillMed(timer: Timer, refillsRemaining: TextInputEditText?){
        val numRefills = refillsRemaining?.text.toString().toInt()
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
class DisplayCountdown(private val view: TextView?, private val timer: Timer, private val context: Context, private val id: Int,
                       time: Long,
                       msPerSec: Long) : CountDownTimer(time, msPerSec) {
    private var seconds = 0L
    private var mins = 0L
    private var hours = 0L
    private var days = 0L

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

        val displayString = when {
            days > 0 -> {
                "%02d:%02d:%02d:%02d".format(days, hours, mins, seconds)
            }
            hours > 0 -> {
                "%02d:%02d:%02d".format(hours, mins, seconds)
            }
            mins > 0 -> {
                "%02d:%02d".format(mins, seconds)
            }
            seconds > 0 -> {
                "%02d".format(seconds)
            }
            else -> {
                "Now!"
            }
        }

        view?.text = displayString
    }

    override fun onFinish() {
        view?.text = context.getString(R.string.timerFinishText)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.pill_clipart)
                .setContentTitle("Medication Reminder")
                .setContentText("It's time to take ${timer.medication.name}.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(id, builder.build())
        }
    }
}

class FoodCountdown(private val timer: Timer, private var context: Context, private val id: Int, private var timeRemaining: Long) : CountDownTimer(timeRemaining, 1000L) {

    override fun onTick(millisUntilFinished: Long) {

    }

    override fun onFinish() {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.pill_clipart)
                .setContentTitle("Meal Reminder")
                .setContentText("It's time to eat for ${timer.medication.name}, dose scheduled for 30 min from now.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(id, builder.build())
        }
    }
}