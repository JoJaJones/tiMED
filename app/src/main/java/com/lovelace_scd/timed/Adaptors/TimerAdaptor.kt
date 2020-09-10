package com.lovelace_scd.timed.Adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        val productImage: ImageView? = itemView.findViewById<ImageView>(R.id.productImage)
        val productName: TextView? = itemView.findViewById<TextView>(R.id.productDescription)
        val productPrice: TextView? = itemView.findViewById<TextView>(R.id.priceText)

        fun bindTimer(timer: Timer, context: Context){
            productImage?.setImageResource(context.resources.getIdentifier(product.image, "drawable", context.packageName)) //converts string resource name to the appropriate resource file
            productName?.text = product.title
            productPrice?.text = product.price
//            itemView.setOnClickListener { itemClick(product) }
        }
    }
}