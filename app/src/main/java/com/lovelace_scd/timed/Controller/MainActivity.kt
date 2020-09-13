package com.lovelace_scd.timed.Controller

import android.R.attr
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lovelace_scd.timed.Adaptors.TimerAdaptor
import com.lovelace_scd.timed.R
import com.lovelace_scd.timed.services.TimerList


@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {
    private val TAG = "ActivityLifeCycle(MA): "  // TODO comment out or delete all lines that make use of this as they are logging lines
    private val timers = TimerList.data
    private lateinit var adapter: TimerAdaptor
    private lateinit var notificationManager: NotificationManager;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timers.loadTimers(this)

        // generate adaptor to handle the contents of the recycler view portion of the main activity
        adapter = TimerAdaptor(this, timers)
        val timerListView = findViewById<RecyclerView>(R.id.timerListView)
        timerListView.adapter = this.adapter

        timerListView.layoutManager = LinearLayoutManager(this)
        createNotificationManager()
    }

    private fun createNotificationManager() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Med notifications"
            val descriptionText = "General medication reminders"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("something about a channel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun addMed(view: View) {
        val addIntent = Intent(this, AddActivity::class.java)
        startActivityForResult(addIntent, 0)
    }

    /**********************************************************************************************
     * Function to allow receiving information from the child activity when it returns.
     *********************************************************************************************/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0) {
            if(resultCode == 1) { // if user added a medication refresh timer list
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