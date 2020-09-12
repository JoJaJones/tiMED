package com.lovelace_scd.timed.Adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.lovelace_scd.timed.R
import com.lovelace_scd.timed.model.Timer

class TimerAdaptor(val context: Context, val timers: List<Timer>) : RecyclerView.Adapter<TimerAdaptor.Holder>() {
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

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val medName: TextView? = itemView.findViewById<TextView>(R.id.medName)
//        var deleteMedBtn: Button? = itemView.findViewById<Button>(R.id.delButton)
//        var takeMedBtn: Button? = itemView.findViewById<Button>(R.id.takeBtn)
//        var skipDoseBtn: Button? = itemView.findViewById<Button>(R.id.skipBtn)
//        var delayDoseBtn: Button? = itemView.findViewById<Button>(R.id.delayBtn)
//        var medTimerText: TextView? = itemView.findViewById<TextView>(R.id.medTimer)
        var refillsRemaining: TextInputEditText? = itemView.findViewById<TextInputEditText>(R.id.refillsRemainingField)


        fun bindTimer(timer: Timer, context: Context){
            medName?.text = timer.getName()
            refillsRemaining?.text = timer.getRemainingRefills()
        }
    }
}