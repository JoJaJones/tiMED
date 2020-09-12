package com.lovelace_scd.timed.Controller

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lovelace_scd.timed.Adaptors.TimerAdaptor
import com.lovelace_scd.timed.R
import com.lovelace_scd.timed.model.Timer
import com.lovelace_scd.timed.service.TestTimerObjects
import com.lovelace_scd.timed.services.TimerList

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    private val TAG = "ActivityLifeCycle(MA): "
    private val timers = TestTimerObjects.testTimers
    private lateinit var adapter: TimerAdaptor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (timers.size > 0) {
            adapter = TimerAdaptor(this, timers)
            val timerListView = findViewById<RecyclerView>(R.id.timerListView)
            timerListView.adapter = this.adapter

            timerListView.layoutManager = LinearLayoutManager(this)
        }
    }

    fun addMed(view: View) {
        val addIntent = Intent(this, AddActivity::class.java)
        startActivity(addIntent)
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