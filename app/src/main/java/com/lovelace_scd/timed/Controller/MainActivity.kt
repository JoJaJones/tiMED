package com.lovelace_scd.timed.Controller

import android.R.attr
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lovelace_scd.timed.Adaptors.TimerAdaptor
import com.lovelace_scd.timed.R
import com.lovelace_scd.timed.services.TimerList


@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    private val TAG = "ActivityLifeCycle(MA): "
    private val timers = TimerList.data
    private lateinit var adapter: TimerAdaptor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timers.loadTimers(this)
        adapter = TimerAdaptor(this, timers)
        val timerListView = findViewById<RecyclerView>(R.id.timerListView)
        timerListView.adapter = this.adapter

        timerListView.layoutManager = LinearLayoutManager(this)

    }

    fun addMed(view: View) {
        val addIntent = Intent(this, AddActivity::class.java)
        startActivityForResult(addIntent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0) {
            if(resultCode == 1) {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onStart() {
        Log.d(TAG, "onStart called")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume called")
        Log.d("File", "${TimerList.data.getTimers()?.size}")
        super.onResume()
    }

    override fun onRestart() {
        Log.d(TAG, "onRestart called")
        super.onRestart()
    }

    override fun onPause() {
        Log.d(TAG, "onPause called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop called")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy caled")
        super.onDestroy()
    }
}