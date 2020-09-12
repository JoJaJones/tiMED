package com.lovelace_scd.timed.Adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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

//        var takeMedBtn: ImageButton? = itemView.findViewById<Button>(R.id.takeBtn)
//        var skipDoseBtn: ImageButton? = itemView.findViewById<Button>(R.id.skipBtn)
//        var delayDoseBtn: ImageButton? = itemView.findViewById<Button>(R.id.delayBtn)
//        var refillBtn: Button? = itemView.findViewById<Button>(R.id.delayBtn)
        var medTimerText: TextView? = itemView.findViewById(R.id.medTimer)
//        var refillsRemaining: TextInputEditText? = itemView.findViewById(R.id.refillsRemainingField)


        fun bindTimer(timer: TestTimer, context: Context){
            this.timer = timer
            medName?.text = timer.name
            deleteMedBtn?.setOnClickListener(this)
            medTimerText?.text = timer.getTime()
        }

        override fun onClick(view: View){
            if (view.equals(deleteMedBtn)) {
                removeTimer(adapterPosition)
            }
        }
    }

    fun removeTimer(position: Int){
        timers.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, timers.size)
    }
}