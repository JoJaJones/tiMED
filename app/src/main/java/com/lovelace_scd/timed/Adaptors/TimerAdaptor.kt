package com.lovelace_scd.timed.Adaptors

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.lovelace_scd.timed.R
import com.lovelace_scd.timed.model.TestTimer
import com.lovelace_scd.timed.model.Timer
import com.lovelace_scd.timed.test_code.Test

class TimerAdaptor(val context: Context, val timers: MutableList<TestTimer>) : RecyclerView.Adapter<TimerAdaptor.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.timer_list_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return timers.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindTimer(timers[position], context)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        lateinit var timer: TestTimer
        val medName: TextView? = itemView.findViewById(R.id.medName)
        var deleteMedBtn: ImageButton? = itemView.findViewById(R.id.delButton)
        var takeMedBtn: ImageButton? = itemView.findViewById(R.id.takeBtn)
        var skipDoseBtn: ImageButton? = itemView.findViewById(R.id.skipBtn)
        var delayDoseBtn: ImageButton? = itemView.findViewById(R.id.delayBtn)
        var refillBtn: Button? = itemView.findViewById(R.id.refillBtn)
        var medTimerText: TextView? = itemView.findViewById(R.id.medTimer)
        var refillsRemaining: TextInputEditText? = itemView.findViewById(R.id.refillsRemainingField)


        fun bindTimer(timer: TestTimer, context: Context){
            this.timer = timer
            itemView.setOnClickListener(this)
            medName?.text = timer.name
            deleteMedBtn?.setOnClickListener(this)
            takeMedBtn?.setOnClickListener(this)
            skipDoseBtn?.setOnClickListener(this)
            delayDoseBtn?.setOnClickListener(this)
            refillBtn?.setOnClickListener(this)
            refillsRemaining?.setText(timer.getRemainingRefills().toString())
            medTimerText?.text = timer.getTime()
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
                Toast.makeText(context, "You clicked on ${timer.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun removeTimer(position: Int){
        timers.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, timers.size)
    }

    fun takeDose(position: Int){
        Log.d("Med Btn Test: ", "take ${timers[position].name}")
//        timers[position].makeTaken()
    }

    fun delayDose(position: Int){
        Log.d("Med Btn Test: ", "delay ${timers[position].name}")
//        timers[position].adjustNextDoseTime()
    }

    fun skipDose(position: Int){
        Log.d("Med Btn Test: ", "skip ${timers[position].name}")
//        timers[position].skipDose()
    }

    fun refillMed(position: Int){
        Log.d("Med Btn Test: ", "refill ${timers[position].name}")
//        timers[position].refillMed()
    }
}