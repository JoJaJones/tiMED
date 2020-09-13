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
import java.util.concurrent.CountDownLatch

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
        val medName: TextView? = itemView.findViewById(R.id.medName)
        var deleteMedBtn: ImageButton? = itemView.findViewById(R.id.delButton)
        var takeMedBtn: ImageButton? = itemView.findViewById(R.id.takeBtn)
        var skipDoseBtn: ImageButton? = itemView.findViewById(R.id.skipBtn)
        var delayDoseBtn: ImageButton? = itemView.findViewById(R.id.delayBtn)
        var refillBtn: Button? = itemView.findViewById(R.id.refillBtn)
        var medTimerText: TextView? = itemView.findViewById(R.id.medTimer)
        var refillsRemaining: TextInputEditText? = itemView.findViewById(R.id.refillsRemainingField)


        fun bindTimer(timer: Timer, context: Context){
            this.timer = timer
            itemView.setOnClickListener(this)
            medName?.text = timer.medication.name
            deleteMedBtn?.setOnClickListener(this)
            takeMedBtn?.setOnClickListener(this)
            skipDoseBtn?.setOnClickListener(this)
            delayDoseBtn?.setOnClickListener(this)
            refillBtn?.setOnClickListener(this)
            refillsRemaining?.setText(timer.medication.numRefillsRemaining.toString())
            DisplayCountdown(medTimerText, timer.calculateTimeRemaining(), 1000L).start()
        }

        override fun onClick(view: View){
            if (view == deleteMedBtn) {
                removeTimer(adapterPosition)
            } else if (view == takeMedBtn){
                takeDose(adapterPosition)
            } else if (view == skipDoseBtn){
                skipDose(adapterPosition)
            } else if (view == delayDoseBtn){
                delayDose(adapterPosition)
            } else if (view == refillBtn){
                refillMed(adapterPosition)
            } else {
                Toast.makeText(context, "You clicked on ${timer.medication.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun removeTimer(position: Int){
        timers.deleteTimer(position, context)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, timers.timerData.size)
    }

    fun takeDose(position: Int){
//        Log.d("Med Btn Test: ", "take ${timers[position].name}")
//        timers[position].makeTaken()
    }

    fun delayDose(position: Int){
//        Log.d("Med Btn Test: ", "delay ${timers[position].name}")
//        timers[position].adjustNextDoseTime()
    }

    fun skipDose(position: Int){
//        Log.d("Med Btn Test: ", "skip ${timers[position].name}")
//        timers[position].skipDose()
    }

    fun refillMed(position: Int){
//        Log.d("Med Btn Test: ", "refill ${timers[position].name}")
//        timers[position].refillMed()
    }
}

class DisplayCountdown (val view: TextView?, time: Long, msPerSec: Long) : CountDownTimer(time, msPerSec) {
    var seconds = 0L
    var mins = 0L
    var hours = 0L
    var days = 0L

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